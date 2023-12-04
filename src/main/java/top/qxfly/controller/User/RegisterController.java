package top.qxfly.controller.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;
import top.qxfly.pojo.User;
import top.qxfly.service.User.RegisterService;

import java.util.Map;

@Slf4j
@RestController
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     * 用户注册
     *
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody Map<String, Object> map) {
        String invite = (String) map.get("invite");
        if (invite.equals("qxfly")) {
            String username = (String) map.get("username");
            String password = (String) map.get("password");
            User user = new User(username, password);
            if (registerService.checkUserName(user) != null) {
                return Result.error("用户已注册!");
            }
            registerService.register(user);
            return Result.success("注册成功！");
        } else {
            return Result.error("邀请码错误！");
        }

    }
}

