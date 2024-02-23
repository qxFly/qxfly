package top.qxfly.controller.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.entity.User;
import top.qxfly.pojo.Result;
import top.qxfly.service.User.RegisterService;
import top.qxfly.utils.AliyunDysmsapi;

import java.util.Map;

@Slf4j
@RestController
@Tag(name = "用户")
public class RegisterController {

    private final AliyunDysmsapi aliyunDysmsapi;
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService, AliyunDysmsapi aliyunDysmsapi) {
        this.registerService = registerService;
        this.aliyunDysmsapi = aliyunDysmsapi;
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
        int code = aliyunDysmsapi.testCode(phone, (Integer) map.get("code"));
        if (code == 0) {
            return Result.error("验证码错误！");
        } else if (code == -1) {
            return Result.error("请获取验证码！");
        }
        if (invite.equals(SysInvite)) {
            String username = (String) map.get("username");
            String password = (String) map.get("password");
            User user = new User(username, password, phone);
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
        } else {
            return Result.error("邀请码错误！");
        }

    }
}

