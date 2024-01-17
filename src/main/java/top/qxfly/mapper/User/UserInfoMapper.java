package top.qxfly.mapper.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.qxfly.entity.Token;
import top.qxfly.entity.User;

@Mapper
public interface UserInfoMapper {
    /**
     * 根据Token获取用户
     *
     * @param token
     * @return
     */
    User getUserInfoByToken(Token token);

    /**
     * 更改用户信息
     * @param user
     * @return
     */
    boolean updateUserInfo(User user);

    /**
     * 检查用户名是否可行
     * @param user
     * @return
     */
    @Select("select * from user where username = #{username}")
    User checkUsername(User user);

    @Update("update user set avatar = #{path} where id = #{id}")
    void updateImg(String path,int id);

    /**
     * 获取用户文章数
     * @param token
     * @return
     */
    Integer getArticleCount(Token token);

    /**
     * 获取文章点赞数
     * @param token
     * @return
     */
    Integer getLike(Token token);

    /**
     * 刷新用户信息
     * @param articleCount
     * @param likeCount
     * @param i
     */
    @Update("update user_card set Articles = #{articleCount}, Likes = #{likeCount}, Tags = #{tagsCount} WHERE username = (SELECT username FROM user_token WHERE token = #{token})")
    void refreshUserInfo(int articleCount, int likeCount, int tagsCount, String token);
}
