package top.qxfly.service.Article;

import org.springframework.web.multipart.MultipartFile;
import top.qxfly.entity.Article;
import top.qxfly.pojo.PageBean;
import top.qxfly.pojo.Result;

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
    PageBean<Article> getArticlesByPage(int currPage, int pageSize, String searchData, String token);

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
}
