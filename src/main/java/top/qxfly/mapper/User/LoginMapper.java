package top.qxfly.mapper.User;

import org.apache.ibatis.annotations.*;
import top.qxfly.pojo.Token;
import top.qxfly.pojo.User;

/**
 * 部门管理
 */
@Mapper
public interface LoginMapper {

    /**
     * 登陆校验
     *
     * @param user
     * @return
     */
//    @Select("select * from user where username = #{username} and password = #{password}")
    User login(User user);

    /**
     * 查询用户是否有token
     *
     * @param user
     * @return
     */
    @Select("select * from user_token where username = #{username}")
    Token getTokenByUser(User user);

    /**
     * 设置用户的token
     *
     * @param username
     * @param newJwt
     */
    @Insert("insert into user_token(username,token,create_time)value(#{username},#{newJwt},#{createDate})")
    void setTokenByUser(String username, String newJwt, long createDate);

    /**
     * 删除用户token
     *
     * @param jwt
     */
    @Delete("delete from user_token where token = #{token}")
    void deleteJwt(Token jwt);

    /**
     * 更新用户token
     *
     * @param username
     * @param newJwt
     */
    @Update("update user_token set token = #{newJwt}, create_time = #{nowDate} where username = #{username}")
    void updateJwt(String username, String newJwt, long nowDate);

    /**
     * 获取token创建时间
     *
     * @param jwt
     * @return
     */
    @Select("select create_time from user_token where username = #{username}")
    long getJwtCreateTime(Token jwt);
}
