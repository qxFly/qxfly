package top.qxfly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Jwt;
import top.qxfly.pojo.Result;
import top.qxfly.pojo.User;
import top.qxfly.service.LoginService;
import top.qxfly.service.LogoutService;
import top.qxfly.utils.JwtUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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
            /*查看用户是否有jwt证书*/
            Jwt jwt = loginService.getJwt(user);
            int flag = 1;
            /*jwt证书不为空*/
            if (jwt != null && jwt.getToken() != null) {
                try {
                    /*验证jwt证书*/
                    JwtUtils.parseJWT(jwt.getToken());
                } catch (Exception e) {
                    flag = 0;
                    e.printStackTrace();
                    loginService.deleteJwt(jwt);
                    log.info("登入证书解析失败");
                }
                if (flag == 1) {
                    logoutService.deleteJwt(jwt);
                    return Result.success(jwt.getToken());
                }
            }
            /*生成jwt证书*/
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("username", u.getUsername());
            claims.put("password", u.getPassword());

            /*获取现在的时间，转化为毫秒*/
            Date nowdate = new Date();
            long createDate = nowdate.getTime();
            log.info("nowData:{}", createDate);

            String newJwt = JwtUtils.generateJwt(claims, nowdate);
            loginService.setJwt(user.getUsername(), newJwt, createDate);
            logoutService.deleteJwt(jwt);
            log.info("返回的jwt:" + newJwt);
            return Result.success(newJwt);
        }
        return Result.error("用户名或密码错误");
    }

    /**
     * 检查登录状态
     *
     * @param jwt
     * @return
     */
    @PostMapping("/loginStatue")
    public Result loginStatue(@RequestBody Jwt jwt) {
        log.info("loginStatue:{}", jwt);
        String loginStatue = loginService.GetLoginStatue(jwt);
        if (!loginStatue.equals("LOGIN")) return Result.error(loginStatue);
        log.info("登陆状态正常");
        return Result.success();
    }

    /**
     * 更新登入状态
     *
     * @param jwt
     * @return
     */
    @PostMapping("/updateLoginStatue")
    public Result updateLoginStatue(@RequestBody Jwt jwt) {
        long createTime = loginService.getJwtCreateTime(jwt);
        Date nowTime = new Date();
        long updateTime = nowTime.getTime();

        /*判断证书剩余时间，小于1周则发放新证书*/
        if (updateTime - createTime >= 604800000) {
            log.info("剩余一周，续期");
            /*获得用户密码*/
            String password = loginService.getUserPassword(jwt.getUsername());
            log.info("password:{}", password);
            /*生成jwt证书*/
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", jwt.getUsername());
            claims.put("password", password);
            String newJwt = JwtUtils.generateJwt(claims, nowTime);
            loginService.updateJwt(jwt.getUsername(), newJwt, nowTime.getTime());
            logoutService.updateJwtblack(jwt);
            log.info("续期的jwt:" + newJwt);
            return Result.success(newJwt);
        } else {
            log.info("无需续期");
            return Result.success("ok");
        }


    }
}
