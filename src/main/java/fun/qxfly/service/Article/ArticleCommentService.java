package fun.qxfly.service.Article;

import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.*;
import fun.qxfly.pojo.PageBean;
import fun.qxfly.pojo.Result;
import fun.qxfly.vo.ArticleVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleCommentService {

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
