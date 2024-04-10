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
    Integer releaseArticle(Article article);

    /**
     * 获取文章数
     *
     * @return
     */
    int getArticleCount(String searchData, int authorId, int verify, String classify, String[] tagArr, int pub);

    /**
     * 分页获取文章
     *
     * @param start
     * @param pageSize
     * @return
     */
    List<ArticleVO> getArticlesByPage(int start, int pageSize, String searchData, String sort, int authorId, int verify, String classify, String[] tagArr, int pub);

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
    @Update("update article set title = #{title}, content = #{content}, preview = #{preview}, cover = #{cover}, updateTime = #{updateTime}, tag = #{tag}, verify = #{verify}, classify = #{classify},pub = #{pub} where id = #{id}")
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
     * 保存文章图片
     *
     * @param aid
     * @param image
     * @return
     */
    @Insert("insert into article_image(aid, image)values(#{aid}, #{image})")
    boolean saveArticleImage(Integer aid, String image);

    /**
     * 更新文章图片
     *
     * @param aid
     * @param image
     */
    @Update("update article_image set image = #{image} where aid = #{aid}")
    void updateArticleImage(Integer aid, String image);

    /**
     * 获取文章图片
     *
     * @param article
     * @return
     */
    @Select("select image from article_image where aid = #{id}")
    String getArticleImage(Article article);

    /**
     * 保存文章附件
     *
     * @param aid
     * @param uid
     * @param attachment
     * @return
     */
    @Insert("insert into article_attachment(aid, uid, file_name, file_origin_name)values(#{aid}, #{uid}, #{attachment.fileName}, #{attachment.fileOriginName})")
    Integer saveAttachment(Integer aid, Integer uid, Attachment attachment);

    /**
     * 获取文章附件
     *
     * @param id
     * @return
     */
    @Select("select * from article_attachment where aid = #{id}")
    List<Attachment> getArticleAttachmentByAid(Integer id);

    /**
     * 删除文章附件
     *
     * @param aid
     * @param fileName
     */
    @Delete("delete from article_attachment where aid = #{aid} and file_name = #{fileName}")
    void deleteAttachment(Integer aid, String fileName);

    /**
     * 删除文章所有附件
     *
     * @param id
     */
    @Delete("delete from article_attachment where aid = #{id}")
    void deleteAllArticleAttachmentByAid(Integer id);

    /**
     * 如果文章公开图片，则单独保存
     *
     * @param id
     * @param imageName
     */
    @Insert("insert into image(aid, name, createTime)values(#{id}, #{imageName}, #{date})")
    void savePubImage(Integer id, String imageName, Date date);

    /**
     * 如果文章不公开图片 删除文章公开图片
     *
     * @param id
     */
    @Delete("delete from image where aid = #{id}")
    void removePubImage(Integer id);

    /**
     * 如果文章不公开图片 删除文章公开图片,根据图片名称差异删除
     * @param item
     */
    @Delete("delete from image where name = #{item}")
    void removePubImageByImageName(String item);

    /**
     * 检查文章公开图片是否重复
     * @param id
     * @param imageName
     * @return
     */
    @Select("select count(*) from image where aid = #{id} and name = #{imageName}")
    Integer checkSamePubImage(Integer id, String imageName);
}
