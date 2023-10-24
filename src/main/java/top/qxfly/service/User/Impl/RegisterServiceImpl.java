package top.qxfly.service.User.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.User.RegisterMapper;
import top.qxfly.pojo.User;
import top.qxfly.service.User.RegisterService;

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
    public User checkUserName(User user) {
        return registerMapper.findUserByUsername(user);
    }
}
