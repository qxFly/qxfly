package top.qxfly.mapper.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.qxfly.entity.User;

@Mapper
public interface RegisterMapper {

    /**
     * 用户注册
     *
     * @param user
     */
    @Insert("insert into user(username,password)values(#{username},#{password})")
    void register(User user);

    /**
     * 检测用户名是否被注册
     *
     * @param user
     * @return
     */
    @Select("select username from user where username = #{username}")
    User findUserByUsername(User user);

    @Select("select * from user where username = #{username}")
    User getUser(User user);

    @Insert("insert into user_card(id, username)VALUES (#{id},#{username})")
    void createUserInfo(User user1);
}
