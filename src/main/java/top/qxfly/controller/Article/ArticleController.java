package top.qxfly.controller.Article;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.qxfly.entity.Article;
import top.qxfly.entity.Comment;
import top.qxfly.entity.Token;
import top.qxfly.entity.User;
import top.qxfly.pojo.PageBean;
import top.qxfly.pojo.Result;
import top.qxfly.service.Article.ArticleService;
import top.qxfly.service.User.UserInfoService;
import top.qxfly.vo.ArticleVO;

import java.util.Date;

@Slf4j
@CrossOrigin
@RestController
@Tag(name = "文章")
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;
    private final UserInfoService userInfoService;

    public ArticleController(ArticleService articleService, UserInfoService userInfoService) {
        this.articleService = articleService;
        this.userInfoService = userInfoService;
    }

    /**
     * 发布文章
     *
     * @param article
     * @return
     */
    @PostMapping("/releaseArticle")
    @Operation(description = "发布文章", summary = "发布文章")
    public Result releaseArticle(@RequestBody Article article, HttpServletRequest request) {
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        Token token = new Token();
        token.setToken(request.getHeader("token"));
        User u = userInfoService.getUserInfoByToken(token);
        article.setAuthorId(u.getId());
        article.setAuthor(u.getUsername());
        boolean f = articleService.releaseArticle(article);
        return Result.success();
    }

    /**
     * 编辑文章
     *
     * @param article
     * @return
     */
    @PostMapping("/editArticle")
    @Operation(description = "编辑文章", summary = "编辑文章")
    public Result editArticle(@RequestBody Article article) {
        article.setUpdateTime(new Date());
        Article article1 = articleService.getArticleById(article.getId());
        boolean delcover = articleService.deletePreviousCover(article1.getCover());
        if (delcover) {
            boolean f = articleService.editArticle(article);
        }

        return Result.success();
    }

    /**
     * 删除文章
     *
     * @param article
     * @return
     */
    @PostMapping("/deleteArticle")
    @Operation(description = "删除文章", summary = "删除文章")
    public Result deleteArticle(@RequestBody Article article) {
        boolean b = articleService.deleteArticle(article);

        return Result.success();
    }

    /**
     * 文章封面上传
     *
     * @param file
     * @return
     */
    @Operation(description = "封面上传", summary = "封面上传")
    @PostMapping("/updateArticleCover")
    public Result updateArticleCover(MultipartFile file) {
        return articleService.updateArticleCover(file);
    }

    /**
     * 文章内容图片上传
     *
     * @param file
     * @return
     */
    @Operation(description = "文章内容图片上传", summary = "文章内容图片上传")
    @PostMapping("/uploadArticleImage")
    public Result uploadArticleImage(MultipartFile file) {
        return articleService.uploadArticleImage(file);
    }

    /**
     * 分页获取文章
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @GetMapping("/getArticles")
    @Operation(description = "分页获取文章", summary = "分页获取文章")
    public Result getArticles(@RequestParam int currPage, @RequestParam int pageSize, @RequestParam int isUser, @RequestParam(defaultValue = "") String searchData, @RequestParam(defaultValue = "new") String sort, HttpServletRequest request) {
        String token = null;
        if (isUser == 1) {
            token = request.getHeader("token");
        }
        PageBean<ArticleVO> pageBean = articleService.getArticlesByPage(currPage, pageSize, searchData, sort, token);
        return Result.success(pageBean);
    }

    /**
     * 根据id获取文章
     *
     * @param id
     * @return
     */
    @GetMapping("/getArticleById")
    @Operation(description = "根据id获取文章", summary = "根据id获取文章")
    public Result getArticleById(@RequestParam int id) {
        Article article = articleService.getArticleById(id);
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article, articleVO);
        return Result.success(articleVO);
    }

    /**
     * 根据文章id获取评论
     *
     * @param id
     * @return
     */
    @GetMapping("/getArticleComments")
    @Operation(description = "根据文章id获取评论", summary = "根据文章id获取评论")
    public Result getArticleComments(@RequestParam int currPage, @RequestParam int pageSize, @RequestParam(defaultValue = "new") String sort, @RequestParam int id) {
        PageBean<Comment> pageBean = articleService.getArticleCommentsByPage(currPage, pageSize, sort, id);
        return Result.success(pageBean);
    }

    /**
     * 发布评论
     *
     * @param id
     * @return
     */
    @PostMapping("/releaseComment")
    @Operation(description = "发布评论", summary = "发布评论")
    public Result releaseComment(@RequestBody Comment comment, HttpServletRequest request) {
        Token token = new Token();
        token.setToken(request.getHeader("token"));
        User u = userInfoService.getUserInfoByToken(token);
        comment.setUserId(u.getId());
        comment.setUsername(u.getUsername());
        comment.setCreateTime(new Date());
        boolean b = articleService.releaseComment(comment);
        return Result.success();
    }


}
