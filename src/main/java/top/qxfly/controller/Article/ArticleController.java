package top.qxfly.controller.Article;

import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.qxfly.entity.*;
import top.qxfly.pojo.PageBean;
import top.qxfly.pojo.Result;
import top.qxfly.service.Article.ArticleService;
import top.qxfly.service.User.UserInfoService;
import top.qxfly.utils.JwtUtils;
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
        article.setVerify(1);
        Article article1 = articleService.getArticleById(article.getId());
        boolean delcover = true;
        if (!article.getCover().equals(article1.getCover())) {
            delcover = articleService.deletePreviousCover(article1.getCover());
        }
        if (delcover) {
            boolean f = articleService.editArticle(article);
        } else {
            return Result.error("图片上传失败，当着并不是你的问题");
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
     * @param authorId
     * @param searchData
     * @param sort
     * @param request
     * @return
     */
    @GetMapping("/getArticles")
    @Operation(description = "分页获取文章", summary = "分页获取文章")
    public Result getArticles(@RequestParam int currPage, @RequestParam int pageSize,
                              @RequestParam int authorId, @RequestParam(defaultValue = "") String searchData,
                              @RequestParam(defaultValue = "new") String sort,
                              @RequestParam(defaultValue = "false") boolean daily,
                              @RequestParam(defaultValue = "3") int verify,
                              HttpServletRequest request) {
        String token = request.getHeader("token");
        try {
            int userId = (int) JwtUtils.parseJWT(token).get("userId");
            if (authorId != userId) {
                verify = 3;
            }
        } catch (Exception e) {
            verify = 3;
            e.printStackTrace();
        }

        PageBean<ArticleVO> pageBean = articleService.getArticlesByPage(currPage, pageSize, searchData, sort, daily, authorId, verify);
        return Result.success(pageBean);
    }

    /**
     * 分页获取收藏文章
     *
     * @param currPage
     * @param pageSize
     * @param uid
     * @param searchData
     * @param sort
     * @return
     */
    @GetMapping("/getCollectionArticles")
    @Operation(description = "分页获取收藏文章", summary = "分页获取收藏文章")
    public Result getCollectionArticles(@RequestParam int currPage, @RequestParam int pageSize,
                                        @RequestParam int uid, @RequestParam(defaultValue = "") String searchData,
                                        @RequestParam(defaultValue = "newC") String sort) {
        PageInfo<ArticleVO> pageBean = articleService.getCollectionArticles(currPage, pageSize, searchData, sort, uid);
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
     * @param currPage
     * @param pageSize
     * @param sort
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
        comment.setUserId(u.getId());
        comment.setUsername(u.getUsername());
        comment.setCreateTime(new Date());
        boolean b = articleService.releaseComment(comment);
        return Result.success();
    }

    /**
     * 检测用户章是否可编辑文章
     *
     * @param request
     * @return
     */
    @PostMapping("/checkA")
    @Operation(description = "检测用户章是否可编辑文章", summary = "检测用户章是否可编辑文章")
    public Result checkArticle(HttpServletRequest request) {
        try {
            Claims claims = JwtUtils.parseJWT(request.getHeader("token"));
            User user = new User();
            user.setId((Integer) claims.get("userId"));
            user.setUsername((String) claims.get("username"));
            return Result.success(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("");
        }

    }

    /**
     * 文章浏览量
     *
     * @param dailyView
     * @return
     */
    @PostMapping("/addArticleView")
    @Operation(description = "文章浏览量", summary = "文章浏览量")
    public Result addArticleView(@RequestBody DailyView dailyView, HttpServletRequest request) {
        String token = request.getHeader("token");
        try {
            Integer userId = (Integer) JwtUtils.parseJWT(token).get("userId");
            articleService.addArticleView(dailyView.getArticleId(), userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    /**
     * 文章点赞和取消
     *
     * @param dailyView
     * @return
     */
    @PostMapping("/articleLike")
    @Operation(description = "文章点赞", summary = "文章点赞")
    public Result articleLike(@RequestBody DailyView dailyView, HttpServletRequest request) {
        try {
            /*从token中获取用户id*/
            String token = request.getHeader("token");
            Integer userId = (Integer) JwtUtils.parseJWT(token).get("userId");
            if (dailyView.getViews().equals(0))
                articleService.articleLike(dailyView.getArticleId(), userId);
            else
                articleService.cancelArticleLike(dailyView.getArticleId(), userId);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("token无效，请重新登陆！");
        }

    }

    /**
     * 文章收藏和取消
     *
     * @param Collection
     * @return
     */
    @PostMapping("/articleCollection")
    @Operation(description = "文章收藏", summary = "文章收藏")
    public Result articleCollection(@RequestBody DailyView Collection, HttpServletRequest request) {
        try {
            /*从token中获取用户id*/
            String token = request.getHeader("token");
            Integer userId = (Integer) JwtUtils.parseJWT(token).get("userId");
            /* 0为没有收藏，否则取消收藏*/
            if (Collection.getViews().equals(0))
                articleService.articleCollection(Collection.getArticleId(), userId);
            else
                articleService.cencelArticleCollection(Collection.getArticleId(), userId);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("token无效，请重新登陆！");
        }


    }

    /**
     * 判断文章是否点赞
     *
     * @param dailyView
     * @return
     */
    @PostMapping("/isArticleLike")
    @Operation(description = "判断文章是否点赞", summary = "判断文章是否点赞")
    public Result isArticleLike(@RequestBody DailyView dailyView, HttpServletRequest request) {
        try {
            /*从token中获取用户id*/
            String token = request.getHeader("token");
            Integer userId = (Integer) JwtUtils.parseJWT(token).get("userId");
            return articleService.isArticleLike(dailyView.getArticleId(), userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("token无效，请重新登陆！");
        }
    }

    /**
     * 删除文章图片
     *
     * @param imageList
     * @return
     */
    @GetMapping("/deleteArticleImage")
    @Operation(description = "删除文章图片", summary = "删除文章图片")
    public Result deleteArticleImage(@RequestParam String imageList) {
        String[] split = imageList.split(",");
        articleService.deleteArticleImage(split);
        return Result.success();
    }
}
