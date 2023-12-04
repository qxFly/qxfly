package top.qxfly.controller.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;
import top.qxfly.pojo.Token;
import top.qxfly.service.User.UserInfoService;
import top.qxfly.utils.JwtUtils;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/getuserinfo")
    public Result getuserinfo(@RequestBody Token token) {
        try {
            JwtUtils.parseJWT(token.getToken());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("验证失败");
        }

        String username = userInfoService.getUserByToken(token.getToken());
        if (username != null) {
            return Result.success(username);
        } else {
            return Result.error("未登录");
        }
    }
}
