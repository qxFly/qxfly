package top.qxfly.mapper.User;

import org.apache.ibatis.annotations.*;
import top.qxfly.pojo.Token;

@Mapper
public interface LogoutMapper {
    /**
     * 把退出的用户加入黑名单
     *
     * @param token
     */
    @Insert("insert into logout_users(username,token)value(#{username},#{token})")
    void addTokenToBlack(Token token);

    /**
     * 删除token
     *
     * @param jwt
     */
    @Delete("delete from logout_users where token = #{token}")
    void deleteToken(Token jwt);

    /**
     * 获取退出状态信息
     *
     * @param token
     */
    @Select("select username from logout_users where token = #{token}")
    String getLogoutStatusByToken(String token);
}
