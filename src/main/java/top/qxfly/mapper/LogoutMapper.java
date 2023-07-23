package top.qxfly.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import top.qxfly.pojo.Jwt;

@Mapper
public interface LogoutMapper {
    /**
     * 把退出的用户加入黑名单
     *
     * @param token
     */
    @Insert("insert into jwtblack(username,token)value(#{username},#{token})")
    void addTokenToBlack(Jwt token);

    /**
     * 删除token
     *
     * @param jwt
     */
    @Delete("delete from jwtblack where token = #{token}")
    void deleteJwt(Jwt jwt);

    @Update("update jwtblack set token = #{token} where username = #{username}")
    void updateJwtblack(Jwt jwt);
}
