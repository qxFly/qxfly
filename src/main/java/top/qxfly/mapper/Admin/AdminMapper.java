package top.qxfly.mapper.Admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.qxfly.pojo.Token;

@Mapper
public interface AdminMapper {

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    @Select("select role from user where username = #{username}")
    String check(String username);

    /**
     * 根据 token 查找 用户
     *
     * @param token
     * @return
     */
    @Select("select username from user_token where token = #{token}")
    String getUserNameByToken(Token token);

}
