package top.qxfly.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.qxfly.pojo.User;

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
     * 查找用户
     *
     * @param user
     * @return
     */
    @Select("select * from user where username = #{username}")
    User findUserByusername(User user);
}
