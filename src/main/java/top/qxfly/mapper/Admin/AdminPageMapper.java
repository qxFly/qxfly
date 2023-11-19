package top.qxfly.mapper.Admin;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.qxfly.pojo.Token;
import top.qxfly.pojo.User;

import java.util.List;

@Mapper
public interface AdminPageMapper {
    /**
     * 获取用户信息
     *
     * @return
     */
    @Select("select id, username, role, email, phone from user limit #{nextPage}, 10")
    List<User> getAllUserInfo(int nextPage);

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @Delete("delete from user where id = #{id}")
    boolean deleteUserById(Integer id);
    /**
     * 更改用户信息
     * @param userName
     * @param role
     * @param email
     * @param phone
     * @return
     */
    @Update("update user set username = #{userName}, role = #{role}, email = #{email}, phone = #{phone} where id = #{id}")
    boolean changeUserInfo(Integer id,String userName, String role, String email, String phone);

    /**
     * 根据 token 查找 用户
     * @param token
     * @return
     */
    @Select("select username from user_token where token = #{token}")
    String getUserNameByToken(Token token);

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    @Select("select role from user where username = #{username}")
    String check(String username);


    /**
     * 获得总用户数
     *
     * @return
     */
    @Select("select count(*) from user")
    int getUserCount();

    /**
     * 根据Id查找用户
     *
     * @param id
     * @return
     */
    @Select("select * from user where id = #{id}")
    List<User> searchUserById(int id);

    /**
     * 根据用户名查找用户（可模糊）
     *
     * @param username
     * @return
     */
    @Select("select * from user where username like concat('%',#{username},'%')")
    List<User> searchUserByUsername(String username);
}
