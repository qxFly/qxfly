package fun.qxfly.controller.User;

import fun.qxfly.entity.User;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.User.LoginService;
import fun.qxfly.service.User.RegisterService;
import fun.qxfly.utils.AliyunDysmsapi;
import fun.qxfly.utils.RSAEncrypt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@Tag(name = "用户")
public class RegisterController {

    private final AliyunDysmsapi aliyunDysmsapi;
    private final RegisterService registerService;
    private final LoginService loginService;

    public RegisterController(RegisterService registerService, AliyunDysmsapi aliyunDysmsapi, LoginService loginService) {
        this.registerService = registerService;
        this.aliyunDysmsapi = aliyunDysmsapi;
        this.loginService = loginService;
    }

    /**
     * 用户注册
     *
     * @return
     */
    @Operation(description = "用户注册", summary = "用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody Map<String, Object> map) {
        String invite = (String) map.get("invite");
        String SysInvite = System.getProperty("Invite");
        String phone = (String) map.get("phone");
        String ucode = (String) map.get("code");
        boolean isCode = false;
        if (invite == null) {
            isCode = true;
        } else {
            if (!invite.equals(SysInvite)) {
                isCode = true;
            }
        }
        if (isCode) {
            try {
                int code = aliyunDysmsapi.testCode(phone, Integer.valueOf(ucode));
                if (code == 0) {
                    return Result.error("验证码错误！");
                } else if (code == -1) {
                    return Result.error("请获取验证码！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("请输入正确的验证码或邀请码");
            }

        }
        String username = (String) map.get("username");
        String encodePassword = (String) map.get("password");
        String publicKey = (String) map.get("salt");
        String decodePassword;
        String privateKey = loginService.getPrivateKey(publicKey);
        try {
            decodePassword = RSAEncrypt.decrypt(encodePassword, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
        User user = new User(username, decodePassword, phone);
        if (registerService.checkUsername(user) != null) {
            return Result.error("用户名已注册");
        }
        if (registerService.checkPhone(user) != null) {
            return Result.error("手机号已注册!");
        }
        registerService.register(user);
        User user1 = registerService.getUser(user);
        registerService.createUserInfo(user1);
        return Result.success("注册成功！");
    }
}

