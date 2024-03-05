package fun.qxfly.mapper.User;

import org.apache.ibatis.annotations.*;
import fun.qxfly.entity.Token;
import fun.qxfly.entity.User;

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
     * @param token
     */
    @Delete("delete from user_token where username = #{username}")
    void deleteToken(Token token);

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
    Long getJwtCreateTime(Token jwt);

    /**
     * 保存RSA密匙
     *
     * @param publicKey
     * @param privateKey
     */
    @Insert("insert into rsa_key(publicKey,privateKey)value(#{publicKey},#{privateKey})")
    void saveKey(String publicKey, String privateKey);

    /**
     * 根据公匙获取私匙
     * @param publicKey
     * @return
     */
    @Select("select privateKey from rsa_key where publicKey = #{publicKey}")
    String getPrivateKey(String publicKey);

    /**
     * 删除RSA密匙
     * @param publicKey
     */
    @Delete("delete from rsa_key where publicKey = #{publicKey}")
    void deleteKey(String publicKey);
}
