package top.qxfly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Jwt;
import top.qxfly.pojo.Result;
import top.qxfly.service.LogoutService;
import top.qxfly.utils.JwtUtils;

@Slf4j
@RestController
public class LogoutController {
    @Autowired
    private LogoutService logoutService;

    /**
     * 退出校验
     *
     * @param jwt
     * @return
     */
    @PostMapping("/logout")
    public Result Login(@RequestBody Jwt jwt) {
        log.info("token:{}", jwt.getToken());

        if (jwt != null) {
            try {
                JwtUtils.parseJWT(jwt.getToken());
                logoutService.logout(jwt);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("退出操作，但是token已经过期");
                logoutService.deleteJwt(jwt);
            }
        }

        return Result.success();
    }
}
