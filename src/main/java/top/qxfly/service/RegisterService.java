package top.qxfly.service;

import top.qxfly.pojo.User;

public interface RegisterService {

    /**
     * 用户注册
     *
     * @param user
     */
    void register(User user);

    /**
     * 查找用户
     *
     * @param user
     * @return
     */
    User findUser(User user);
}
