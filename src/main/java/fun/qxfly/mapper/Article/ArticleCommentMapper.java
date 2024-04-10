package fun.qxfly.mapper.Article;

import fun.qxfly.entity.Comment;
import fun.qxfly.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleCommentMapper {


    /**
     * 获取文章评论数
     *
     * @param id
     * @return
     */
    @Select("select count(*) from comment where articleId = #{id} and verify = 3 and parentCommentId = 0")
    int getArticleCommentsCount(int id);

    /**
     * 根据文章id获取评论
     *
     * @param id
     * @return
     */
    List<Comment> getArticleCommentsByPage(int start, int pageSize, String sort, int id);

    /**
     * 获取子评论
     *
     * @param id
     * @return
     */
    List<Comment> getChildCommentByCommentId(Integer id);

    /**
     * 发布评论
     *
     * @param comment
     * @return
     */
    @Insert("insert into comment(articleId, content, parentCommentId, userId, username, createTime,toUserId,toUsername,verify)values(#{articleId},#{content},#{parentCommentId},#{user.id},#{user.username},#{createTime},#{toUserId},#{toUsername},#{verify})")
    boolean releaseComment(Comment comment);

    /**
     * 清空每日浏览量
     *
     * @return
     */
    @Update("update daily_view set daily_views = 0")
    boolean resetDailyView();


    /**
     * 添加评论点赞
     *
     * @param comment
     */
    @Update("update comment set likeCount = likeCount + 1 where id = #{id}")
    Integer addCommentLike(Comment comment);

    /**
     * 获取用户评论点赞
     *
     * @param u
     * @param comment
     * @return
     */
    @Select("select count(*) from user_like_comment where uid = #{u.id} and cid = #{comment.id}")
    Integer getUserCommentLike(User u, Comment comment);

    /**
     * 添加用户评论点赞
     *
     * @param comment
     * @param u
     */
    @Insert("insert into user_like_comment(uid, cid,aid)values(#{u.id},#{comment.id},#{comment.articleId})")
    Integer addUserCommentLike(User u, Comment comment);

    /**
     * 获取用户今日是否点赞
     *
     * @param user
     * @param comment
     * @return
     */
    @Select("select count(*) from user_comment_daily_like where uid = #{user.id} and cid = #{comment.id}")
    Integer getUserCommentDailyLike(User user, Comment comment);

    /**
     * 添加用户今日点赞
     *
     * @param user
     * @param comment
     */
    @Insert("insert into user_comment_daily_like(uid, cid)values(#{user.id},#{comment.id})")
    Integer addUserCommentDailyLike(User user, Comment comment);

    /**
     * 取消用户评论点赞
     *
     * @param user
     * @param comment
     */
    @Delete("delete from user_like_comment where uid = #{user.id} and cid = #{comment.id}")
    Integer cancelUserCommentLike(User user, Comment comment);

    /**
     * 获取用户点赞的评论
     *
     * @param aid
     * @param uid
     * @return
     */
    @Select("select cid from user_like_comment where uid = #{uid} and aid = #{aid}")
    List<Integer> getUserLikeComment(Integer aid, int uid);
}
