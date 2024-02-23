package top.qxfly.service.Admin;

import com.github.pagehelper.PageInfo;
import top.qxfly.entity.Article;

public interface ArticleManageService {
    /**
     * 文章审核
     *
     * @param articleId
     * @param verify
     * @return
     */
    boolean articleVerify(int articleId, int verify);

    /**
     * 搜索文章
     * @param currPage
     * @param pageSize
     * @param articleId
     * @param title
     * @param author
     * @param authorId
     * @param tag
     * @param createTimeStart
     * @param createTimeEnd
     * @param updateTimeStart
     * @param updateTimeEnd
     * @param verify
     * @return
     */
    PageInfo<Article> searchArticle(Integer currPage, Integer pageSize, Integer articleId, String title, String author, Integer authorId, String tag, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd, Integer verify);

    /**
     * 文章
     * @param article
     * @return
     */
    boolean deleteArticle(Article article);
}
