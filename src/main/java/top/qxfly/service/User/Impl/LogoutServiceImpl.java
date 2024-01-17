package top.qxfly.service.User.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.User.LogoutMapper;
import top.qxfly.entity.Token;
import top.qxfly.service.User.LogoutService;

@Slf4j
@Service
public class LogoutServiceImpl implements LogoutService {
    @Autowired
    private LogoutMapper logoutMapper;

    @Override
    public void logout(Token token) {
        logoutMapper.addTokenToBlack(token);
    }

    /**
     * 删除token
     *
     * @param token
     */
    @Override
    public void deleteToken(Token token) {
        logoutMapper.deleteToken(token);
    }

    @Override
    public String getLogoutStatus(String token) {
        return logoutMapper.getLogoutStatusByToken(token);
    }

}
