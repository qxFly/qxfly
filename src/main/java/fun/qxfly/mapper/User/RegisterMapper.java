package fun.qxfly.mapper.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import fun.qxfly.entity.User;

@Mapper
public interface RegisterMapper {

    /**
     * 用户注册
     *
     * @param user
     */
    @Insert("insert into user(username,password,phone)values(#{username},#{password},#{phone})")
    void register(User user);

    /**
     * 检测用户名是否被注册
     *
     * @param user
     * @return
     */
    @Select("select * from user where username = #{username}")
    User findUserByUsername(User user);

    /**
     * 检测手机是否被注册
     *
     * @param user
     * @return
     */
    @Select("select * from user where phone = #{phone}")
    User findUserByPhone(User user);

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    @Select("select * from user where username = #{username}")
    User getUser(User user);

    /**
     * 创建用户信息
     *
     * @param user1
     */
    @Insert("insert into user_card(id, username)VALUES (#{id},#{username})")
    void createUserInfo(User user1);
}
