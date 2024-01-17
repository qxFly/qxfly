package top.qxfly.controller.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.entity.User;
import top.qxfly.pojo.Result;
import top.qxfly.service.User.RegisterService;

import java.util.Map;

@Slf4j
@RestController
@Tag(name = "用户")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
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
        if (invite.equals(SysInvite)) {
            String username = (String) map.get("username");
            String password = (String) map.get("password");
            User user = new User(username, password);
            if (registerService.checkUserName(user) != null) {
                return Result.error("用户已注册!");
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

