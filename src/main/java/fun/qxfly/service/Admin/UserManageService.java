package fun.qxfly.service.Admin;

import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.User;

public interface UserManageService {
    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    boolean deleteUserById(Integer id);

    /**
     * 编辑用户信息
     *
     * @param user
     * @return
     */
    boolean editUserInfo(User user);

    /**
     * 根据条件列出用户
     *
     * @param user
     * @return
     */
    PageInfo<User> listUser(Integer currPage, Integer pageSize, User user);

    /**
     * 删除用户头像
     *
     * @param user
     * @return
     */
    boolean deleteUserAvatar(User user);
}
