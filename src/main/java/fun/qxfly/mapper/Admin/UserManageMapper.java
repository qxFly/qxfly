package fun.qxfly.mapper.Admin;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import fun.qxfly.entity.User;

import java.util.List;

@Mapper
public interface UserManageMapper {
    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @Delete("delete from user where id = #{id}")
    boolean deleteUserById(Integer id);

    /**
     * 编辑用户信息
     *
     * @param user
     * @return
     */
    @Update("update user set username = #{username}, role = #{role}, email = #{email}, phone = #{phone}, introduction = #{introduction},location = #{location}, birthday = #{birthday} where id = #{id}")
    boolean editUserInfo(User user);

    /**
     * 根据条件列出用户
     *
     * @param user
     * @return
     */
    List<User> listUser(User user);

    /**
     * 删除用户头像
     *
     * @param user
     * @return
     */
    @Update("update user set avatar = null where id = #{id}")
    boolean deleteUserAvatar(User user);
}
