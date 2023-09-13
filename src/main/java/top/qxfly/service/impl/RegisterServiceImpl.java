package top.qxfly.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.RegisterMapper;
import top.qxfly.pojo.User;
import top.qxfly.service.RegisterService;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private RegisterMapper registerMapper;

    /**
     * 用户注册
     *
     * @param user
     */
    @Override
    public void register(User user) {
        registerMapper.register(user);
    }

    /**
     * 查找用户
     *
     * @param user
     * @return
     */
    @Override
    public User findUser(User user) {
        return registerMapper.findUserByusername(user);
    }
}
