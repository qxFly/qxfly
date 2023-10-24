package top.qxfly.service.User.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.User.LoginMapper;
import top.qxfly.pojo.Token;
import top.qxfly.pojo.User;
import top.qxfly.service.User.LoginService;
import top.qxfly.utils.JwtUtils;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    /**
     * 登陆校验
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return loginMapper.login(user);
    }

    /**
     * 查询用户是否有token
     *
     * @param user
     * @return
     */
    @Override
    public Token getTokenByUser(User user) {
        return loginMapper.getTokenByUser(user);
    }

    /**
     * 设置用户的token
     *
     * @param username
     * @param newToken
     */
    @Override
    public void setToken(String username, String newToken, long createDate) {
        loginMapper.setTokenByUser(username, newToken, createDate);
    }

    /**
     * 删除用户token
     *
     * @param jwt
     */
    @Override
    public void deleteToken(Token jwt) {
        loginMapper.deleteJwt(jwt);
    }

    /**
     * 更新用户token
     *
     * @param username
     * @param newJwt
     */
    @Override
    public void updateToken(String username, String newJwt, long nowDate) {
        loginMapper.updateJwt(username, newJwt, nowDate);
    }

    /**
     * 获取token创建时间
     *
     * @param jwt
     * @return
     */
    @Override
    public long getJwtCreateTime(Token jwt) {
        return loginMapper.getJwtCreateTime(jwt);
    }

    /**
     * 检查登录状态
     *
     * @param token
     * @return
     */
    @Override
    public String checkLoginStatue(Token token) {
        if (token == null || token.getToken() == null) {
            log.info("未登入");
            return "NOT_LOGIN";
        } else {
            try {
                JwtUtils.parseJWT(token.getToken());
            } catch (Exception e) {
                e.printStackTrace();
                log.info("登入状态失效");
                return "TIME_OUT";
            }
        }
        return "LOGIN";
    }

    /**
     * 获得用户密码
     *
     * @param username
     * @return
     */
    @Override
    public String getUserPassword(String username) {
        return loginMapper.getPasswordByUsername(username);
    }

}
