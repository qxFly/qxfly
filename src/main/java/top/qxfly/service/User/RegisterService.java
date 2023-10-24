package top.qxfly.service.User;

import top.qxfly.pojo.User;

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
}
