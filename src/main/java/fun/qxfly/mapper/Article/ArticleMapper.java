package fun.qxfly.mapper.Article;

import fun.qxfly.entity.*;
import fun.qxfly.vo.ArticleVO;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface ArticleMapper {
    /**
     * 发布文章
     *
     * @param article
     * @return
     */
    @Insert("insert into article(title, content, preview, updateTime, createTime, tag, likes, views, authorId, author, cover,classify)VALUES (#{title},#{content},#{preview},#{updateTime},#{createTime},#{tag},#{likes},#{views},#{authorId},#{author},#{cover},#{classify})")
    boolean releaseArticle(Article article);

    /**
     * 获取文章数
     *
     * @return
     */
    int getArticleCount(String searchData, int authorId, int verify, String classify, String[] tagArr);

    /**
     * 分页获取文章
     *
     * @param start
     * @param pageSize
     * @return
     */
    List<ArticleVO> getArticlesByPage(int start, int pageSize, String searchData, String sort, int authorId, int verify, String classify, String[] tagArr);

    /**
     * 根据id获取文章
     *
     * @param id
     * @return
     */
    @Select("select * from article where id = #{id}")
    Article getArticleById(int id);

    /**
     * 编辑文章
     *
     * @param article
     * @return
     */
    @Update("update article set title = #{title}, content = #{content}, preview = #{preview}, cover = #{cover}, updateTime = #{updateTime}, tag = #{tag}, verify = #{verify}, classify = #{classify} where id = #{id}")
    boolean editArticle(Article article);

    /**
     * 删除文章
     *
     * @param article
     * @return
     */
    @Delete("delete from article where id = #{id}")
    boolean deleteArticleById(Article article);

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
    @Select("select c.*,u.avatar from comment c join `user` u on u.id = c.userId where parentCommentId = #{id} and c.verify = 3 order by c.createTime ASC")
    List<Comment> getChildCommentByCommentId(Integer id);

    /**
     * 发布评论
     *
     * @param comment
     * @return
     */
    @Insert("insert into comment(articleId, content, parentCommentId, userId, username, createTime,toUserId,toUsername)values(#{articleId},#{content},#{parentCommentId},#{userId},#{username},#{createTime},#{toUserId},#{toUsername})")
    boolean releaseComment(Comment comment);

    /**
     * 清空每日浏览量
     *
     * @return
     */
    @Update("update daily_view set daily_views = 0")
    boolean resetDailyView();

    /**
     * 清空每周浏览量
     *
     * @return
     */
    @Update("update daily_view set Weekly_views = 0")
    boolean resetWeeklyViews();

    /**
     * 清空每月浏览量
     *
     * @return
     */
    @Update("update daily_view set Monthly_views = 0")
    boolean resetMonthlyViews();

    /**
     * 增加文章总访问量
     *
     * @param articleId
     */
    @Update("update article set views = views + 1 where id = #{articleId}")
    boolean addArticleTotalViews(int articleId);

    /**
     * 获取文章浏览量
     *
     * @param articleId
     * @return
     */
    @Select("select * from daily_view where articleId = #{articleId}")
    DailyView getDailyViewByArticleId(int articleId);

    /**
     * 向表中插入文章日浏览量
     *
     * @param articleId
     */
    @Insert("insert into daily_view(articleId) VALUES (#{articleId})")
    void addDailyView(int articleId);

    /**
     * 增加文章日浏览量
     *
     * @param articleId
     */
    @Update("update daily_view set daily_views = daily_views + 1, Weekly_views = Weekly_views + 1, Monthly_views = Monthly_views + 1 where articleId = #{articleId}")
    void updateDailyView(int articleId);

    /**
     * 文章点赞
     *
     * @param articleId
     * @return
     */
    @Update("update article set likes = likes + 1 where id = #{articleId};")
    boolean articleLike(Integer articleId);

    /**
     * 文章收藏
     *
     * @param articleId
     * @return
     */
    @Update("update article set collection = article.collection + 1 where id = #{articleId};")
    boolean addarticleCollectionCount(Integer articleId);

    /**
     * 获取用户点赞收藏的文章
     *
     * @param userId
     * @return
     */
    @Select("select * from user_like_article where userId = #{userId}")
    UserLikesAndCollection getUserLikes(Integer userId);

    /**
     * 添加用户点赞的文章
     *
     * @param userLikes
     */
    @Insert("insert into user_like_article(userId, likeArticles)values(#{userId},#{likeArticles})")
    void addUserLikes(UserLikesAndCollection userLikes);

    /**
     * 更新用户点赞的文章
     *
     * @param userLikes
     */
    @Update("update user_like_article set likeArticles = #{likeArticles} where userId = #{userId}")
    void updateUserLikes(UserLikesAndCollection userLikes);

    /**
     * 获取每日热门文章数
     *
     * @return
     */
    @Select("select count(*) from daily_view where daily_views != 0")
    int getDailyArticleCount();

    /**
     * 获取每日热门文章
     *
     * @param start
     * @param pageSize
     * @return
     */
    @Select("select * from article a, daily_view dv where dv.articleId = a.id and a.verify = 3 and dv.daily_views != 0 order by dv.daily_views desc limit #{start}, #{pageSize}")
    List<ArticleVO> getDailyArticlesByPage(int start, int pageSize);

    /**
     * 分页获取收藏文章
     *
     * @param searchData
     * @param sort
     * @param uid
     * @return
     */
    List<ArticleVO> getUserCollection(String searchData, String sort, int uid);

    /**
     * 收藏文章
     *
     * @param articleId
     * @param userId
     * @param date
     */
    @Insert("insert into user_collection(uid, articleId, collectionTime)values(#{userId},#{articleId},#{date}) ")
    void updateUserCollection(Integer articleId, Integer userId, Date date);

    /**
     * 取消文章收藏
     *
     * @param articleId
     * @param userId
     */
    @Delete("delete from user_collection where uid = #{userId} and articleId = #{articleId}")
    boolean deleteUserCollection(Integer articleId, Integer userId);

    /**
     * 判断用户是否收藏文章
     *
     * @param articleId
     * @param userId
     * @return
     */
    @Select("select * from user_collection where articleId = #{articleId} and uid = #{userId}")
    Integer userIsCollArt(Integer articleId, Integer userId);

    /**
     * 获取用户今日是否浏览该文章
     *
     * @param articleId
     * @param userId
     * @return
     */
    @Select("select count(*) from user_article_daily_view where (articleId = #{articleId} and uid = #{userId}) or (articleId = #{articleId} and UserAgent = #{UA})")
    Integer getUserArticleView(Integer articleId, Integer userId, String UA);

    /**
     * 清空用户每日浏览记录
     *
     * @return
     */
    @Delete("delete from user_article_daily_view")
    boolean resetUserDailyViewTask();

    /**
     * 添加用户每日浏览记录
     *
     * @param articleId
     * @param userId
     */
    @Insert("insert into user_article_daily_view(articleId, uid, UserAgent)values(#{articleId},#{userId},#{UA}) ")
    void addUserArticleView(Integer articleId, Integer userId, String UA);

    /**
     * 获取用户对文章的点赞状态
     *
     * @param articleId
     * @param userId
     * @return
     */
    @Select("select * from user_article_daily_like where uid = #{userId} and articleId = #{articleId}")
    Integer getUserArticleLike(Integer articleId, Integer userId);

    /**
     * 添加用户每日点赞记录
     *
     * @param articleId
     * @param userId
     */
    @Insert("insert into user_article_daily_like(uid, articleId)values(#{userId},#{articleId})")
    void addUserArticleLike(Integer articleId, Integer userId);

    /**
     * 清空用户每日点赞记录
     *
     * @return
     */
    @Delete("delete from user_article_daily_like")
    boolean resetUserDailyLikeTask();

    /**
     * 分页获取所有分类
     *
     * @return
     */
    List<Classify> listClassifiesByPage(Integer id, String name);

    /**
     * 获取所有分类
     *
     * @return
     */
    @Select("select * from classify")
    List<Classify> listClassifies();

    /**
     * 分页获取所有标签
     *
     * @param id
     * @param name
     * @param uid
     * @return
     */
    List<Tag> listTagsByPage(Integer id, String name, Integer uid);

    /**
     * 获取所有标签
     *
     * @return
     */
    @Select("select * from tag")
    List<Tag> listTags();

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
     * @param aid
     * @param uid
     * @return
     */
    @Select("select cid from user_like_comment where uid = #{uid} and aid = #{aid}")
    List<Integer> getUserLikeComment(Integer aid, int uid);
}
