package top.qxfly.service.User;

import top.qxfly.entity.User;

public interface RegisterService {

    /**
     * 用户注册
     *
     * @param user
     */
    void register(User user);

    /**
     * 检测用户名是否被注册
     *
     * @param user
     * @return
     */
    User checkUserName(User user);

    /**
     * 获取用户
     * @param user
     * @return
     */
    User getUser(User user);

    /**
     * 创建用户信息
     * @param user1
     */
    void createUserInfo(User user1);
}
