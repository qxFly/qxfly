package fun.qxfly.controller.User;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import fun.qxfly.entity.Token;
import fun.qxfly.entity.User;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.User.LoginService;
import fun.qxfly.service.User.LogoutService;
import fun.qxfly.utils.JwtUtils;
import fun.qxfly.utils.RSAEncrypt;

import java.util.Date;
import java.util.Map;

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
        /*判断是否为手机号*/
        boolean matches = user.getUsername().matches("^[0-9]*$");
        if (matches) {
            user.setPhone(user.getUsername());
        }
        String encodePassword = user.getPassword();
        String decodePassword;
        String privateKey = loginService.getPrivateKey(user.getSalt());
        try {
            decodePassword = RSAEncrypt.decrypt(encodePassword, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("密码错误");
        }
        user.setPassword(decodePassword);
        User u = loginService.login(user);
        if (u != null) {
            /*获取用户的token*/
            Token userToken = loginService.getTokenByUser(u);

            /*token不为空*/
            if (userToken != null && userToken.getToken() != null) {
                try {
                    /*验证Token是否有效*/
                    JwtUtils.parseJWT(userToken.getToken());
                    /* token有效，登入*/
                    logoutService.deleteToken(userToken);
                    return Result.success(u.getUsername(), userToken.getToken());
                } catch (Exception e) {
                    e.printStackTrace();
                    loginService.deleteToken(userToken);
                    log.info("登入证书过期或错误！");
                }
            }
            /* 用户无token生成token */
            /* 获取现在的时间，转化为毫秒 */
            Date nowdate = new Date();
            long createDate = nowdate.getTime();

            String newToken = JwtUtils.createToken(u.getId(), u.getUsername(), nowdate, null);
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
        Long createTime = loginService.getTokenCreateTime(token);
        if (createTime == null) return Result.error("token无效！");
        Date nowTime = new Date();
        long updateTime = nowTime.getTime();
        /*判断证书剩余时间，小于1周则发放新证书*/
        if (updateTime - createTime >= 604800000) {
            log.info("剩余一周，续期");
            /*生成token*/
            try {
                Claims claims = JwtUtils.parseJWT(token.getToken());
                Integer userId = (Integer) claims.get("userId");
                String username = (String) claims.get("username");
                String newToken = JwtUtils.createToken(userId, username, nowTime, null);
                loginService.updateToken(token.getUsername(), newToken, updateTime);
                return Result.success(newToken);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.error("token无效！");
            }
        } else {
            log.info("无需续期");
            return Result.success("ok");
        }
    }

    /**
     * 获取公匙
     *
     * @return
     */
    @Operation(description = "获取公匙", summary = "获取公匙")
    @PostMapping("/gs")
    public Result gs() throws Exception {
        Map<Integer, String> keyMap = RSAEncrypt.genKeyPair();
        loginService.saveKey(keyMap.get(0), keyMap.get(1));
        return Result.success(keyMap.get(0));
    }

}