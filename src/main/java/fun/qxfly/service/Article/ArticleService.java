package fun.qxfly.service.Article;

import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Article;
import fun.qxfly.entity.Attachment;
import fun.qxfly.entity.Classify;
import fun.qxfly.entity.Tag;
import fun.qxfly.pojo.PageBean;
import fun.qxfly.pojo.Result;
import fun.qxfly.vo.ArticleVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    /**
     * 发布文章
     *
     * @param article
     * @return
     */
    Integer releaseArticle(Article article, String image);

    /**
     * 分页获取文章
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    PageBean<ArticleVO> getArticlesByPage(int currPage, int pageSize, String searchData, String sort, boolean Daily, int authorId, int verify, String classify, String[] tagArr, int pub);

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
     * 编辑完成时删除没有选择的文章图片
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
     * 上传文章附件
     *
     * @param file
     * @return
     */
    String uploadAttachment(MultipartFile file);

    /**
     * 删除文章附件
     *
     * @param fileName
     * @return
     */
    boolean deleteAttachment(Integer aid, String fileName);

    /**
     * 保存文章附件
     *
     * @param aid
     * @param attachmentList
     * @return
     */
    Integer saveAttachment(Integer aid, Integer uid, List<Attachment> attachmentList);

    /**
     * 获取文章附件
     *
     * @param id
     * @return
     */
    List<Attachment> getArticleAttachment(Integer id);
}
