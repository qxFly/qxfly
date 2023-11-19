package top.qxfly.mapper.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.qxfly.pojo.Token;
import top.qxfly.pojo.User;

@Mapper
public interface UserInfoMapper {
    /**
     * 根据Token获取用户
     *
     * @param token
     * @return
     */
    @Select("select username from user_token where token = #{token}")
    String getUserByToken(String token);
}
