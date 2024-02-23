package top.qxfly.service.Admin.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.entity.Article;
import top.qxfly.mapper.Admin.ArticleManageMapper;
import top.qxfly.service.Admin.ArticleManageService;

import java.io.File;
import java.util.List;

@Slf4j
@Service
public class ArticleManageServiceImpl implements ArticleManageService {
    @Autowired
    ArticleManageMapper articleManageMapper;

    /**
     * 文章审核
     *
     * @param articleId
     * @param verify
     * @return
     */
    @Override
    public boolean articleVerify(int articleId, int verify) {
        return articleManageMapper.articleVerify(articleId, verify);
    }

    /**
     * 搜索文章
     *
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
    @Override
    public PageInfo<Article> searchArticle(Integer currPage, Integer pageSize, Integer articleId, String title, String author, Integer authorId, String tag, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd, Integer verify) {
        PageHelper.startPage(currPage, pageSize);
        List<Article> articleList = articleManageMapper.searchArticle(articleId, title, author, authorId, tag, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd, verify);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        return pageInfo;
    }

    /**
     * 删除文章
     * @param article
     * @return
     */
    @Override
    public boolean deleteArticle(Article article) {
        String s = article.getCover().split("/")[article.getCover().split("/").length - 1];
        File cover = new File(System.getProperty("user.dir") + "/data/qxfly-articleCover/" + s);
        if (cover.exists()) cover.delete();
        return articleManageMapper.deleteArticle(article.getId());
    }
}
