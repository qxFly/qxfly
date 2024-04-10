package fun.qxfly.controller.Article;

import fun.qxfly.entity.Comment;
import fun.qxfly.entity.Token;
import fun.qxfly.entity.User;
import fun.qxfly.pojo.PageBean;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Article.ArticleCommentService;
import fun.qxfly.service.User.UserInfoService;
import fun.qxfly.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "文章评论")
@RequestMapping("/article")
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;
    private final UserInfoService userInfoService;

    public ArticleCommentController(ArticleCommentService articleCommentService, UserInfoService userInfoService) {
        this.articleCommentService = articleCommentService;
        this.userInfoService = userInfoService;
    }

    /**
     * 根据文章id获取评论
     *
     * @param currPage
     * @param pageSize
     * @param sort
     * @param id
     * @return
     */
    @GetMapping("/getArticleComments")
    @Operation(description = "根据文章id获取评论", summary = "根据文章id获取评论")
    public Result getArticleComments(@RequestParam int currPage, @RequestParam int pageSize, @RequestParam(defaultValue = "new") String sort, @RequestParam int id) {
        PageBean<Comment> pageBean = articleCommentService.getArticleCommentsByPage(currPage, pageSize, sort, id);
        return Result.success(pageBean);
    }

    /**
     * 发布评论
     *
     * @param comment
     * @param request
     * @return
     */
    @PostMapping("/releaseComment")
    @Operation(description = "发布评论", summary = "发布评论")
    public Result releaseComment(@RequestBody Comment comment, HttpServletRequest request) {
        Token token = new Token();
        token.setToken(request.getHeader("token"));
        User u = userInfoService.getUserInfoByToken(token);
        User user = new User();
        user.setId(u.getId());
        user.setUsername(u.getUsername());
        comment.setUser(user);
        comment.setCreateTime(new Date());
        boolean b = articleCommentService.releaseComment(comment);
        if(b){
            return Result.success();
        }else{
            return Result.error("评论审核中，请耐心等待");
        }
    }

    /**
     * 获取用户评论点赞
     *
     * @param request
     * @return
     */
    @GetMapping("/getUserLikeComment")
    @Operation(description = "获取用户评论点赞", summary = "获取用户评论点赞")
    public Result getUserLikeComment(@RequestParam Integer aid, HttpServletRequest request) {
        String token = request.getHeader("token");
        int uid;
        try {
            uid = (Integer) JwtUtils.parseJWT(token).get("userId");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("未登录");
        }
        List<Integer> likeComment = articleCommentService.getUserLikeComment(aid, uid);
        return Result.success(likeComment);
    }


    /**
     * 评论点赞
     *
     * @param comment
     * @param request
     * @return
     */
    @PostMapping("/likeComment")
    @Operation(description = "评论点赞", summary = "评论点赞")
    public Result likeComment(@RequestBody Comment comment, HttpServletRequest request) {
        String token = request.getHeader("token");
        User u = new User();
        try {
            u.setId((Integer) JwtUtils.parseJWT(token).get("userId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean f = articleCommentService.likeComment(comment, u);
        return Result.success();
    }
}
