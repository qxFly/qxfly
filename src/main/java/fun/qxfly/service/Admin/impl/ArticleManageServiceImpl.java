package fun.qxfly.service.Admin.impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Article;
import fun.qxfly.entity.Attachment;
import fun.qxfly.entity.Message;
import fun.qxfly.mapper.Admin.ArticleManageMapper;
import fun.qxfly.mapper.Article.ArticleMapper;
import fun.qxfly.mapper.User.MessageMapper;
import fun.qxfly.service.Admin.ArticleManageService;
import fun.qxfly.service.User.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ArticleManageServiceImpl implements ArticleManageService {
    final ArticleManageMapper articleManageMapper;
    private final ArticleMapper articleMapper;
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    public ArticleManageServiceImpl(ArticleManageMapper articleManageMapper, ArticleMapper articleMapper, MessageService messageService,MessageMapper messageMapper) {
        this.articleManageMapper = articleManageMapper;
        this.articleMapper = articleMapper;
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @Value("${file.articleCover.download.path}")
    private String articleCoverDownloadPath;

    /**
     * 文章审核
     *
     * @param article
     * @param reason
     * @return
     */
    @Override
    public boolean articleVerify(Article article, String reason) {
        articleManageMapper.removeNoPassedArticle(article);
        Article articleById = articleMapper.getArticleById(article.getId());
        if (article.getVerify() == 2) {
            articleManageMapper.addNoPassedArticle(article, reason);
            Message message = new Message();
            message.setSendTime(new Date());
            reason = "您的文章《" + articleById.getTitle() + "》由于" + reason + "，审核未通过，请去除违规内容后重新发布后！";
            message.setContent(reason);
            message.setUid2(articleById.getAuthorId());
            message.setUid1(1);
            String msgId = messageMapper.getMsgId(1, articleById.getAuthorId());
            message.setMsgId(msgId);
            messageService.sendMessage(message);
        }
        return articleManageMapper.articleVerify(article);
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
        for (Article article : articleList) {
            article.setCover(articleCoverDownloadPath + article.getCover());
        }
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
        /*删除文章封面*/
        String s = article.getCover().split("/")[article.getCover().split("/").length - 1];
        File cover = new File(System.getProperty("user.dir") + "/data/qxfly-articleCover/" + s);
        if (cover.exists()) cover.delete();
        /* 删除内容图片 */
        String images = articleMapper.getArticleImage(article);
        if (images != null) {
            ArrayList<String> arrayList = JSONObject.parseObject(images, ArrayList.class);
            String path = System.getProperty("user.dir") + "/data/qxfly-articleImage/";
            for (String image : arrayList) {
                File file = new File(path + image);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        /* 删除附件 */
        List<Attachment> attachmentList = articleMapper.getArticleAttachmentByAid(article.getId());
        for (Attachment attachment : attachmentList) {
            File file = new File(System.getProperty("user.dir") + "/data/qxfly-articleAttachment/" + attachment.getFileName());
            if (file.exists()) {
                file.delete();
            }
        }
        articleMapper.deleteAllArticleAttachmentByAid(article.getId());
        return articleManageMapper.deleteArticle(article.getId());
    }
}
