package top.qxfly.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.LogoutMapper;
import top.qxfly.pojo.Jwt;
import top.qxfly.service.LogoutService;

@Slf4j
@Service
public class LogoutServiceImpl implements LogoutService {
    @Autowired
    private LogoutMapper logoutMapper;

    @Override
    public void logout(Jwt jwt) {
        logoutMapper.deleteJwt(jwt);
        logoutMapper.addTokenToBlack(jwt);
    }

    /**
     * 删除token
     *
     * @param jwt
     */
    @Override
    public void deleteJwt(Jwt jwt) {
        logoutMapper.deleteJwt(jwt);
    }

    /**
     * 更新退出黑名单
     *
     * @param jwt
     */
    @Override
    public void updateJwtblack(Jwt jwt) {
        logoutMapper.updateJwtblack(jwt);
    }
}
