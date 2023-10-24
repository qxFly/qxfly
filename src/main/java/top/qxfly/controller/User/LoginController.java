package top.qxfly.controller.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;
import top.qxfly.pojo.Token;
import top.qxfly.pojo.User;
import top.qxfly.service.User.LoginService;
import top.qxfly.service.User.LogoutService;
import top.qxfly.utils.JwtUtils;

import java.util.Date;


/**
 * 登入Controller
 */
@Slf4j
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private LogoutService logoutService;

    /**
     * 登陆
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result Login(@RequestBody User user) {
        log.info("user：{}", user);
        User u = loginService.login(user);

        if (u != null) {
            /*获取用户的token*/
            Token userToken = loginService.getTokenByUser(user);
            int flag = 1;
            /*token不为空*/
            if (userToken != null && userToken.getToken() != null) {
                try {
                    /*验证Token是否有效*/
                    JwtUtils.parseJWT(userToken.getToken());
                } catch (Exception e) {
                    flag = 0;
                    e.printStackTrace();
                    loginService.deleteToken(userToken);
                    log.info("登入证书解析失败");
                }
                if (flag == 1) {
                    /* token有效，登入*/
                    logoutService.deleteToken(userToken);
                    return Result.success(userToken.getToken());
                }
            }
            /* 用户无token生成token */
            /* 获取现在的时间，转化为毫秒 */
            Date nowdate = new Date();
            long createDate = nowdate.getTime();

            String newToken = JwtUtils.createToken(user, nowdate);
            loginService.setToken(user.getUsername(), newToken, createDate);
            logoutService.deleteToken(userToken);
            log.info("新的token:" + newToken);
            return Result.success(newToken);
        }
        return Result.error("用户名或密码错误");
    }

    /**
     * 检查登录状态
     *
     * @param token
     * @return
     */
    @PostMapping("/loginStatue")
    public Result loginStatue(@RequestBody Token token) {
        log.info("loginStatue:{}", token);
        String loginStatue = loginService.checkLoginStatue(token);
        if (!loginStatue.equals("LOGIN")) return Result.error(loginStatue);
        log.info("登陆状态正常");
        return Result.success();
    }

    /**
     * 更新登入状态
     *
     * @param token
     * @return
     */
    @PostMapping("/updateLoginStatue")
    public Result updateLoginStatue(@RequestBody Token token) {
        long createTime = loginService.getJwtCreateTime(token);
        Date nowTime = new Date();
        long updateTime = nowTime.getTime();

        /*判断证书剩余时间，小于1周则发放新证书*/
        if (updateTime - createTime >= 604800000) {
            log.info("剩余一周，续期");
            /*获得用户密码*/
            String password = loginService.getUserPassword(token.getUsername());
            log.info("password:{}", password);
            /*生成token*/
            User user = new User(token.getUsername(), password);
            String newToken = JwtUtils.createToken(user, nowTime);
            loginService.updateToken(token.getUsername(), newToken, nowTime.getTime());
            log.info("续期的jwt:" + newToken);
            return Result.success(newToken);
        } else {
            log.info("无需续期");
            return Result.success("ok");
        }


    }
}
