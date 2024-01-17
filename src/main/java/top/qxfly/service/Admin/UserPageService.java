package top.qxfly.service.Admin;

import top.qxfly.pojo.PageBean;
import top.qxfly.entity.User;

public interface UserPageService {
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
     * 根据Id查找用户
     * @param id
     * @return
     */
    User searchUserById(int id);

    /**
     * 根据用户名查找用户（可模糊）
     * @param currPage
     * @param pageSize
     * @param username
     * @return
     */
    PageBean<User> searchUserByUsernameByPage(int currPage, int pageSize,String username);

    /**
     * 分页查询用户数据
     * @param currPage
     * @param pageSize
     * @return
     */
    PageBean<User> getUsersByPage(int currPage, int pageSize);
}
