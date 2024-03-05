package fun.qxfly.mapper.User;

import org.apache.ibatis.annotations.*;
import fun.qxfly.entity.Token;
import fun.qxfly.entity.User;
import fun.qxfly.vo.UserVO;

import java.util.List;

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
     *
     * @param user
     * @return
     */
    boolean updateUserInfo(User user);

    /**
     * 检查用户名是否可行
     *
     * @param user
     * @return
     */
    @Select("select * from user where username = #{username}")
    User checkUsername(User user);

    @Update("update user set avatar = #{path} where id = #{id}")
    void updateImg(String path, int id);

    /**
     * 刷新用户信息
     *
     * @param articleCount
     * @param likeCount
     * @param tagsCount
     * @param id
     */
    @Update("update user_card set Articles = #{articleCount}, Likes = #{likeCount}, Tags = #{tagsCount}, Collection = #{Collection}, Views = #{views} WHERE username = (SELECT username FROM user WHERE id = #{id})")
    boolean refreshUserInfo(Integer articleCount, Integer likeCount, Integer Collection, Integer views, Integer tagsCount, int id);

    /**
     * 获取文章、点赞、收藏、浏览数
     *
     * @param token
     * @return
     */
    Integer getArticleCount(Token token);

    Integer getLikeCount(Token token);

    Integer getCollectionCount(Token token);

    Integer getViewsCount(Token token);

    /**
     * 获取所有用户
     *
     * @return
     */
    @Select("select id from user")
    List<User> listAllUser();

    /**
     * 获取推荐作者
     *
     * @return
     */
    @Select("""
            SELECT u.* FROM user_card uc, user u WHERE uc.Views >= ( SELECT floor( RAND() * ( SELECT avg(Views) FROM user_card ) ) ) and uc.Views != 0 and u.id = uc.id ORDER BY uc.Views DESC""")
    List<UserVO> getSuggestAuthor();

    /**
     * 找回密码
     * @param phone
     * @param password
     * @return
     */
    @Update("update user set password = #{password} where phone = #{phone}")
    Integer resetPassword(String phone, String password);

    /**
     *  发送验证码
     * @param phone
     * @param code
     */
    @Insert("insert into captcha(phone,code,createTime) values(#{phone},#{code},#{date})")
    int captcha(String phone, int code, long date);

    /**
     * 获取手机验证
     * @param phone
     * @return
     */
    @Select("select code from captcha where phone = #{phone}")
    List<String> getCode(String phone);

    /**
     * 根据手机号获取用户
     * @param user
     * @return
     */
    @Select("select * from user where phone = #{phone}")
    Integer getUserByPhone(User user);

    /**
     *  删除验证码
     * @param phone
     */
    @Delete("delete from captcha where phone = #{phone}")
    void deleteCode(String phone);

    /**
     * 定时清理超时验证码
     * @return
     */
    @Delete("delete from captcha where createTime < #{date} - 1000 * 60 * 5")
    Integer clearCaptchaTask(long date);
}

