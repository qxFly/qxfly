package fun.qxfly.service.Admin.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Article;
import fun.qxfly.mapper.Admin.ArticleManageMapper;
import fun.qxfly.service.Admin.ArticleManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean articleVerify(int articleId, int verify, String reason) {
        if(verify == 2){
            articleManageMapper.removeNoPassedArticle(articleId);
            articleManageMapper.addNoPassedArticle(articleId, reason);
        }
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
    public PageInfo<Article> searchArticle(Integer currPage, Integer pageSize, Integer articleId, String title, String author, Integer authorId, String tag, String classify, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd, Integer verify) {
        PageHelper.startPage(currPage, pageSize);
        List<Article> articleList = articleManageMapper.searchArticle(articleId, title, author, authorId, tag, classify, createTimeStart, createTimeEnd, updateTimeStart, updateTimeEnd, verify);
        return new PageInfo<>(articleList);
    }

    /**
     * 删除文章
     *
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
