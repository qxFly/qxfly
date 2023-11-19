package top.qxfly.service.Admin;

import top.qxfly.pojo.Token;
import top.qxfly.pojo.User;

import java.util.List;

public interface AdminPageService {
    /**
     * 获取用户信息
     *
     * @return
     */
    List<User> getAllUserInfo(int nextPage);

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    boolean deleteUserById(Integer id);

    /**
     * 更改用户信息
     *
     * @param userName
     * @param role
     * @param email
     * @param phone
     * @return
     */
    boolean changeUserInfo(Integer id, String userName, String role, String email, String phone);

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    boolean check(String username);

    /**
     * 根据 token 查找 用户
     *
     * @param token
     * @return
     */
    String getUserNameByToken(Token token);

    /**
     * 获得总用户数
     *
     * @return
     */
    int getUserCount();

    /**
     * 根据Id查找用户
     *
     * @param id
     * @return
     */
    List<User> searchUserById(int id);

    /**
     * 根据用户名查找用户（可模糊）
     *
     * @param username
     * @return
     */
    List<User> searchUserByUsername(String username);
}
