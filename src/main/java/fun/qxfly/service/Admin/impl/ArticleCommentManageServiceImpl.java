package fun.qxfly.service.Admin.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import fun.qxfly.entity.Comment;
import fun.qxfly.mapper.Admin.ArticleCommentManageMapper;
import fun.qxfly.service.Admin.ArticleCommentManageService;

import java.util.List;

@Slf4j
@Service
public class ArticleCommentManageServiceImpl implements ArticleCommentManageService {
    final
    ArticleCommentManageMapper articleCommentManageMapper;

    public ArticleCommentManageServiceImpl(ArticleCommentManageMapper articleCommentManageMapper) {
        this.articleCommentManageMapper = articleCommentManageMapper;
    }

    /**
     * 删除文章评论
     *
     * @param commentId
     * @return
     */
    @Override
    public boolean deleteArticleComment(Integer commentId) {
        return articleCommentManageMapper.deleteArticleComment(commentId);
    }

    /**
     * 文章评论审核
     *
     * @param commentId
     * @param verify
     * @return
     */
    @Override
    public boolean articleCommentVerify(Integer commentId, Integer verify) {
        return articleCommentManageMapper.articleCommentVerify(commentId, verify);
    }

    /**
     * 搜索评论
     * @param comment
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    @Override
    public PageInfo<Comment> searchArticleComment(Integer currPage, Integer pageSize, Comment comment, String createTimeStart, String createTimeEnd) {
        PageHelper.startPage(currPage, pageSize);
        List<Comment> commentList =  articleCommentManageMapper.searchArticleComment(comment, createTimeStart, createTimeEnd);
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        return pageInfo;
    }
}
