package fun.qxfly.service.Article;

import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.*;
import org.springframework.web.multipart.MultipartFile;
import fun.qxfly.pojo.PageBean;
import fun.qxfly.pojo.Result;
import fun.qxfly.vo.ArticleVO;

import java.util.List;

public interface ArticleService {
    /**
     * 发布文章
     *
     * @param article
     * @return
     */
    boolean releaseArticle(Article article);

    /**
     * 分页获取文章
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    PageBean<ArticleVO> getArticlesByPage(int currPage, int pageSize, String searchData, String sort, boolean Daily, int authorId, int verify, String classify, String[] tagArr);

    /**
     * 根据id获取文章
     *
     * @param id
     * @return
     */
    Article getArticleById(int id);

    /**
     * 文章封面上传
     *
     * @param file
     * @return
     */
    Result updateArticleCover(MultipartFile file);

    /**
     * 删除之前的封面
     *
     * @param cover
     * @return
     */
    boolean deletePreviousCover(String cover);

    /**
     * 编辑文章
     *
     * @param article
     * @return
     */
    boolean editArticle(Article article);

    /**
     * 文章内容图片上传
     *
     * @param file
     * @return
     */
    Result uploadArticleImage(MultipartFile file);

    /**
     * 删除文章
     *
     * @param article
     * @return
     */
    boolean deleteArticle(Article article);

    /**
     * 根据文章id获取评论
     *
     * @param id
     * @return
     */
    PageBean<Comment> getArticleCommentsByPage(int currPage, int pageSize, String sort, int id);

    /**
     * 发布评论
     *
     * @param comment
     * @return
     */
    boolean releaseComment(Comment comment);

    /**
     * 清空每日浏览量
     *
     * @return
     */
    boolean resetDailyViewTask(String type);

    /**
     * 文章点赞
     *
     * @param articleId
     * @param userId
     * @return
     */
    boolean articleLike(Integer articleId, Integer userId);

    /**
     * 取消用户点赞
     *
     * @param articleId
     * @param userId
     */
    boolean cancelArticleLike(Integer articleId, Integer userId);

    /**
     * 文章收藏
     *
     * @param articleId
     * @param userId
     */
    boolean articleCollection(Integer articleId, Integer userId);

    /**
     * 取消用户收藏
     *
     * @param articleId
     * @param userId
     */
    boolean cencelArticleCollection(Integer articleId, Integer userId);

    /**
     * 判断文章是否点赞
     *
     * @param articleId
     * @param userId
     */
    Result isArticleLike(Integer articleId, Integer userId);

    /**
     * 分页获取收藏文章
     *
     * @param currPage
     * @param pageSize
     * @param searchData
     * @param sort
     * @param uid
     * @return
     */
    PageInfo<ArticleVO> getCollectionArticles(int currPage, int pageSize, String searchData, String sort, int uid);

    /**
     * 删除文章图片
     *
     * @param imageList
     */
    boolean deleteArticleImage(String[] imageList);

    /**
     * 添加文章浏览量
     *
     * @param articleId
     * @param userId
     */
    void addArticleView(Integer articleId, Integer userId, String UA);

    /**
     * 清空用户每日浏览记录
     *
     * @return
     */
    boolean resetUserDailyViewTask();

    /**
     * 清空用户每日点赞记录
     *
     * @return
     */
    boolean resetUserDailyLikeTask();

    /**
     * 分页获取所有分类
     *
     * @return
     */
    PageInfo<Classify> listClassifiesByPage(int currPage, int pageSize, Integer id, String name);

    /**
     * 获取所有分类
     *
     * @return
     */
    List<Classify> listClassifies();

    /**
     * 分页获取所有标签
     *
     * @param currPage
     * @param pageSize
     * @param id
     * @param name
     * @param uid
     * @return
     */
    PageInfo<Tag> listTagsByPage(Integer currPage, Integer pageSize, Integer id, String name, Integer uid);

    /**
     * 获取所有标签
     *
     * @return
     */
    List<Tag> listTags();

    /**
     * 评论点赞
     * @param comment
     * @param u
     * @return
     */
    boolean likeComment(Comment comment, User u);

    /**
     * 获取用户点赞的评论
     * @param aid
     * @param uid
     * @return
     */
    List<Integer> getUserLikeComment(Integer aid, int uid);
}
