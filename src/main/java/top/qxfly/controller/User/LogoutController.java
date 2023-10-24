package top.qxfly.controller.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Token;
import top.qxfly.pojo.Result;
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
        log.info("token:{}", token.getToken());

        if (token != null) {
            try {
                JwtUtils.parseJWT(token.getToken());
                logoutService.logout(token);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("退出操作，但是token已经过期");
                logoutService.deleteToken(token);
            }
        }

        return Result.success();
    }
}
