package top.qxfly.service;

import top.qxfly.pojo.Jwt;
import top.qxfly.pojo.User;

public interface LoginService {
    /**
     * 登陆校验
     *
     * @param user
     * @return
     */
    User login(User user);

    /**
     * 查询用户是否有token
     *
     * @param user
     * @return
     */
    Jwt getJwt(User user);

    /**
     * 设置用户的token
     *
     * @param username
     * @param newJwt
     */
    void setJwt(String username, String newJwt, long createDate);

    /**
     * 删除用户token
     *
     * @param jwt
     */
    void deleteJwt(Jwt jwt);

    /**
     * 更新用户token
     *
     * @param username
     * @param newJwt
     */
    void updateJwt(String username, String newJwt, long nowDate);

    /**
     * 获取token创建时间
     *
     * @param jwt
     * @return
     */
    long getJwtCreateTime(Jwt jwt);

    /**
     * 检查登录状态
     *
     * @param jwt
     * @return
     */
    String GetLoginStatue(Jwt jwt);

    /**
     * 获得用户密码
     *
     * @param username
     * @return
     */
    String getUserPassword(String username);
}
