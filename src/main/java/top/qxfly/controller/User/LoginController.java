package top.qxfly.controller.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.entity.Token;
import top.qxfly.entity.User;
import top.qxfly.pojo.Result;
import top.qxfly.service.User.LoginService;
import top.qxfly.service.User.LogoutService;
import top.qxfly.utils.JwtUtils;

import java.util.Date;

/**
 * 登入Controller
 */
@Slf4j
@CrossOrigin
@RestController
@Tag(name = "用户")
public class LoginController {
    @Resource
    private final LoginService loginService;
    private final LogoutService logoutService;

    public LoginController(LoginService loginService, LogoutService logoutService) {
        this.loginService = loginService;
        this.logoutService = logoutService;
    }

    /**
     * 登陆
     *
     * @param user
     * @return
     */
    @Operation(description = "登陆", summary = "登陆")
    @PostMapping("/login")
    public Result Login(@RequestBody User user) {
        boolean matches = user.getUsername().matches("^[0-9]*$");
        log.info("matches:{}", matches);
        if (matches) {
            user.setPhone(user.getUsername());
        }
        User u = loginService.login(user);
        log.info("u:{}", u);
        if (u != null) {
            /*获取用户的token*/
            Token userToken = loginService.getTokenByUser(u);

            /*token不为空*/
            if (userToken != null && userToken.getToken() != null) {
                userToken.setUsername(u.getUsername());
                try {
                    /*验证Token是否有效*/
                    JwtUtils.parseJWT(userToken.getToken());
                    /* token有效，登入*/
                    logoutService.deleteToken(userToken);
                    return Result.success(u.getUsername(), userToken.getToken());
                } catch (Exception e) {
                    e.printStackTrace();
                    loginService.deleteToken(userToken);
                    log.info("登入证书过期！");
                }
            }
            /* 用户无token生成token */
            /* 获取现在的时间，转化为毫秒 */
            Date nowdate = new Date();
            long createDate = nowdate.getTime();

            String newToken = JwtUtils.createToken(u.getUsername(), nowdate);
            loginService.setToken(u.getUsername(), newToken, createDate);
            logoutService.deleteToken(userToken);
            return Result.success(u.getUsername(), newToken);
        }
        return Result.error("用户名或密码错误");
    }

    /**
     * 更新登入状态
     *
     * @param token
     * @return
     */
    @Operation(description = "每次进入站点检查更新登入状态", summary = "更新登入状态")
    @PostMapping("/updateLoginStatue")
    public Result updateLoginStatue(@RequestBody Token token) {
        /* 检查token有效性 */
        String loginStatue = loginService.checkLoginStatue(token);
        if (!loginStatue.equals("LOGIN")) return Result.error(loginStatue);

        /* 有效则判断剩余时间 */
        long createTime = loginService.getTokenCreateTime(token);
        Date nowTime = new Date();
        long updateTime = nowTime.getTime();
        /*判断证书剩余时间，小于1周则发放新证书*/
        if (updateTime - createTime >= 604800000) {
            log.info("剩余一周，续期");
            /*生成token*/
            String newToken = JwtUtils.createToken(token.getUsername(), nowTime);
            loginService.updateToken(token.getUsername(), newToken, updateTime);
            return Result.success(newToken);
        } else {
            log.info("无需续期");
            return Result.success("ok");
        }
    }
}
