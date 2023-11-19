package top.qxfly.service.User.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.User.UserInfoMapper;
import top.qxfly.pojo.Token;
import top.qxfly.pojo.User;
import top.qxfly.service.User.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 根据Token获取用户
     *
     * @param token
     * @return
     */
    @Override
    public String getUserByToken(String token) {
        return userInfoMapper.getUserByToken(token);
    }
}
