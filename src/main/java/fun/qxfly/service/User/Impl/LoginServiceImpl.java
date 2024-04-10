package fun.qxfly.service.User.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fun.qxfly.entity.Token;
import fun.qxfly.entity.User;
import fun.qxfly.mapper.User.LoginMapper;
import fun.qxfly.service.User.LoginService;
import fun.qxfly.utils.JwtUtils;

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
     * @param token
     */
    @Override
    public void deleteToken(Token token) {
        loginMapper.deleteToken(token);
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
    public Long getTokenCreateTime(Token jwt) {
        return loginMapper.getJwtCreateTime(jwt);
    }

    /**
     * 保存RSA密匙
     * @param publicKey
     * @param privateKey
     */
    @Override
    public void saveKey(String publicKey, String privateKey) {
        loginMapper.saveKey(publicKey, privateKey);
    }

    /**
     * 根据公匙获取私匙
     * @param publicKey
     * @return
     */
    @Override
    public String getPrivateKey(String publicKey) {
        String privateKey = loginMapper.getPrivateKey(publicKey);
        loginMapper.deleteKey(publicKey);
        return privateKey;
    }
}
