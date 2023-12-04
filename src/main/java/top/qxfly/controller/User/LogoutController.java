package top.qxfly.controller.User;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;
import top.qxfly.pojo.Token;
import top.qxfly.service.User.LogoutService;
import top.qxfly.utils.JwtUtils;

@Slf4j
@RestController
public class LogoutController {
    @Autowired
    private LogoutService logoutService;

    /**
     * 退出校验
     *
     * @param token
     * @return
     */
    @PostMapping("/logout")
    public Result Login(@RequestBody Token token) {
        Claims claims;
        if (token != null) {
            try {
                claims = JwtUtils.parseJWT(token.getToken());
                String username = (String) claims.get("username");
                if (username.equals(token.getUsername())) {
                    logoutService.logout(token);
                } else {
                    return Result.error("验证失败!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.info("退出操作，但是token失效");
                logoutService.deleteToken(token);
                return Result.error("未登录！");
            }
            return Result.success();
        } else {
            return Result.error("验证失败！");
        }
    }
}
