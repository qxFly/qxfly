package fun.qxfly.service.User.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fun.qxfly.entity.User;
import fun.qxfly.mapper.User.RegisterMapper;
import fun.qxfly.service.User.RegisterService;

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
     * 检测用户名是否被注册
     *
     * @param user
     * @return
     */
    @Override
    public User checkUsername(User user) {
        return registerMapper.findUserByUsername(user);
    }

    /**
     * 检测手机是否被注册
     *
     * @param user
     * @return
     */
    @Override
    public User checkPhone(User user) {
        return registerMapper.findUserByPhone(user);
    }

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    @Override
    public User getUser(User user) {
        return registerMapper.getUser(user);
    }

    /**
     * 创建用户信息
     *
     * @param user1
     */
    @Override
    public void createUserInfo(User user1) {
        registerMapper.createUserInfo(user1);
    }
}
