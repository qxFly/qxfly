package top.qxfly.mapper.User;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
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
}
