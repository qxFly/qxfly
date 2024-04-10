package fun.qxfly.service.Article.Impl;

import fun.qxfly.entity.Comment;
import fun.qxfly.entity.User;
import fun.qxfly.mapper.Article.ArticleCommentMapper;
import fun.qxfly.pojo.PageBean;
import fun.qxfly.service.Article.ArticleCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {
    @Value("${file.userImg.download.path}")
    private String userAvatarPath;
    private final ArticleCommentMapper articleCommentMapper;

    public ArticleCommentServiceImpl(ArticleCommentMapper articleCommentMapper) {
        this.articleCommentMapper = articleCommentMapper;
    }


    /**
     * 评论点赞
     *
     * @param comment
     * @param user
     * @return
     */
    @Override
    public boolean likeComment(Comment comment, User user) {
        // 0 为点赞，1为取消点赞
        if (comment.getLikeCount() == 0) {
            Integer i = articleCommentMapper.getUserCommentLike(user, comment);
            // 用户没有点过赞
            if (i == null || i == 0) {
                articleCommentMapper.addUserCommentLike(user, comment);
            }
            Integer userCommentDailyLike = articleCommentMapper.getUserCommentDailyLike(user, comment);
            if (userCommentDailyLike == null || userCommentDailyLike == 0) {
                articleCommentMapper.addUserCommentDailyLike(user, comment);
                articleCommentMapper.addCommentLike(comment);
            }
        } else {
            articleCommentMapper.cancelUserCommentLike(user, comment);
        }
        return true;
    }

    /**
     * 获取用户点赞的评论
     *
     * @param aid
     * @param uid
     * @return
     */
    @Override
    public List<Integer> getUserLikeComment(Integer aid, int uid) {
        return articleCommentMapper.getUserLikeComment(aid, uid);
    }

    /**
     * 根据文章id获取评论
     *
     * @param id
     * @return
     */
    @Override
    public PageBean<Comment> getArticleCommentsByPage(int currPage, int pageSize, String sort, int id) {
        int count = articleCommentMapper.getArticleCommentsCount(id);
        PageBean<Comment> pageBean = new PageBean<>(currPage, pageSize, count);
        List<Comment> comments = articleCommentMapper.getArticleCommentsByPage(pageBean.getStart(), pageSize, sort, id);
        for (Comment comment : comments) {
            /*获取子评论*/
            List<Comment> child = articleCommentMapper.getChildCommentByCommentId(comment.getId());
            /*设置子评论用户头像*/
            for (Comment childComment : child) {
                User user = childComment.getUser();
                if (childComment.getUser().getAvatar() != null)
                    user.setAvatar(userAvatarPath + childComment.getUser().getAvatar());
                childComment.setUser(user);
            }
            comment.setChildComment(child);
            /*设置评论用户头像*/
            User user = comment.getUser();
            if (comment.getUser().getAvatar() != null)
                user.setAvatar(userAvatarPath + comment.getUser().getAvatar());
            comment.setUser(user);
        }
        pageBean.setData(comments);
        return pageBean;
    }

    /**
     * 发布评论
     *
     * @param comment
     * @return
     */
    @Override
    public boolean releaseComment(Comment comment) {
        boolean matches = comment.getContent().matches("sb|王八蛋|卖淫|嫖娼|赌博|吸食|毒品|装逼|草泥马|特么的|撕逼|玛勒戈壁|爆菊|JB|呆逼|本屌|齐B短裙|法克鱿|丢你老母|吉跋猫|妈蛋|逗比|我靠|碧莲|碧池|然并卵|日了狗|屁民|吃翔|你老母|达菲鸡|装13|逼格|蛋疼|傻逼|绿茶婊|你妈的|表砸|屌爆了|买了个表|淫家|你妹|浮尸国|滚粗");
        if (!matches) {
            comment.setVerify(3);
        } else {
            comment.setVerify(1);
        }
        articleCommentMapper.releaseComment(comment);
        return !matches;
    }
}
