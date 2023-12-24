package top.qxfly.mapper.Admin;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.qxfly.pojo.PageBean;
import top.qxfly.pojo.User;

import java.util.List;

@Mapper
public interface UserPageMapper {
    /**
     * 获取用户信息
     *
     * @return
     */
    @Select("select id, username, role, email, phone from user limit #{startPage}, #{pageSize}")
    List<User> getAllUserInfo(int startPage, int pageSize);

    /**
     * 获取所有用户数
     *
     * @return
     */
    @Select("select count(id) from user")
    int getAllUserCount();

    /**
     * 根据Id查找用户
     * @param id
     * @return
     */
    @Select("select * from user where id = #{id}")
    User searchUserById(int id);

    /**
     * 根据用户名查找用户（可模糊）
     * @param startPage
     * @param pageSize
     * @param username
     * @return
     */
    @Select("select * from user where username like concat('%',#{username},'%') limit #{startPage}, #{pageSize}")
    List<User> searchUserByUsername(int startPage, int pageSize, String username);

    /**
     * 根据用户名查找用户数
     * @return
     */
    @Select("select count(*) from user where username like concat('%',#{username},'%')")
    int getUserCountByName(String username);

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @Delete("delete from user where id = #{id}")
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
    @Update("update user set username = #{userName}, role = #{role}, email = #{email}, phone = #{phone} where id = #{id}")
    boolean changeUserInfo(Integer id, String userName, String role, String email, String phone);
}
