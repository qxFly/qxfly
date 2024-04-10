package fun.qxfly.controller.Article;

import com.alibaba.fastjson2.JSONArray;
import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.*;
import fun.qxfly.pojo.PageBean;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Article.ArticleService;
import fun.qxfly.service.User.UserInfoService;
import fun.qxfly.utils.JwtUtils;
import fun.qxfly.vo.ArticleVO;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "文章")
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
        if (u == null) {
            return Result.error("用户未登录");
        }
        String image = article.getAuthor();
        article.setAuthorId(u.getId());
        article.setAuthor(u.getUsername());
        Integer aid = articleService.releaseArticle(article, image);
        return Result.success(aid);
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
        boolean f = articleService.editArticle(article);
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
     * 批量删除文章
     *
     * @param article
     * @return
     */
    @PostMapping("/batchDeleteArticle")
    @Operation(description = "批量删除文章", summary = "批量删除文章")
    public Result batchDeleteArticle(@RequestBody Article article) {
        log.info("批量删除文章：" + article.getContent());
        log.info("批量删除文章：" + article.getContent());
        String[] split1 = article.getContent().split(",");
        for (String ar : split1) {
            Article a = articleService.getArticleById(Integer.parseInt(ar));
            deleteArticle(a);
        }
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
                              @RequestParam(defaultValue = "0") int authorId,
                              @RequestParam(defaultValue = "") String searchData,
                              @RequestParam(defaultValue = "new") String sort,
                              @RequestParam(defaultValue = "") String classify,
                              @RequestParam(defaultValue = "") String tag,
                              @RequestParam(defaultValue = "false") boolean daily,
                              @RequestParam(defaultValue = "3") int verify,
                              HttpServletRequest request) {
        String token = request.getHeader("token");
        int pub = 12;
        try {
            int userId = (int) JwtUtils.parseJWT(token).get("userId");
            if (authorId != userId) {
                verify = 3;
            } else {
                pub = 0;
            }
        } catch (Exception e) {
            verify = 3;
            e.printStackTrace();
        }
        String[] tagArr;
        if (tag.equals("")) {
            tagArr = null;
        } else {
            tagArr = tag.split(",");
        }
        PageBean<ArticleVO> pageBean = articleService.getArticlesByPage(currPage, pageSize, searchData, sort, daily, authorId, verify, classify, tagArr, pub);
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
        String UA = request.getHeader("User-Agent");
        Integer userId = null;
        try {
            userId = (Integer) JwtUtils.parseJWT(token).get("userId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        articleService.addArticleView(dailyView.getArticleId(), userId, UA);
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
     * 编辑完成时删除没有选择的文章图片
     *
     * @param imageList
     * @return
     */
    @GetMapping("/deleteArticleImage")
    @Operation(description = "编辑完成时删除没有选择的文章图片", summary = "删除文章图片")
    public Result deleteArticleImage(@RequestParam String imageList) {
        String[] split = imageList.split(",");
        log.info("split:{}", split);
        articleService.deleteArticleImage(split);
        return Result.success();
    }

    /**
     * 获取文章分类
     *
     * @return
     */
    @GetMapping("/getClassifies")
    @Operation(description = "获取文章分类", summary = "获取文章分类")
    public Result getClassifies(@RequestParam(defaultValue = "0") Integer currPage,
                                @RequestParam(defaultValue = "0") Integer pageSize,
                                @RequestParam(defaultValue = "0") Integer id,
                                @RequestParam(defaultValue = "") String name) {
        if (id == 0) id = null;
        if (currPage != 0) {
            PageInfo<Classify> classifyList = articleService.listClassifiesByPage(currPage, pageSize, id, name);
            return Result.success(classifyList);
        } else {
            List<Classify> classifies = articleService.listClassifies();
            return Result.success(classifies);
        }
    }

    /**
     * 获取标签
     *
     * @return
     */
    @GetMapping("/getTags")
    @Operation(description = "获取标签", summary = "获取标签")
    public Result getTags(@RequestParam(defaultValue = "0") Integer currPage,
                          @RequestParam(defaultValue = "0") Integer pageSize,
                          @RequestParam(defaultValue = "0") Integer id,
                          @RequestParam(defaultValue = "0") Integer uid,
                          @RequestParam(defaultValue = "") String name) {
        if (id == 0) id = null;
        if (uid == 0) uid = null;
        if (currPage != 0) {
            PageInfo<Tag> tagsPageInfo = articleService.listTagsByPage(currPage, pageSize, id, name, uid);
            return Result.success(tagsPageInfo);
        } else {
            List<Tag> tagList = articleService.listTags();
            return Result.success(tagList);
        }
    }

    /**
     * 上传文章附件
     *
     * @param file
     * @return
     */
    @Operation(description = "上传文章附件", summary = "上传文章附件")
    @PostMapping("/uploadAttachment")
    public Result upload(MultipartFile file) {
        String fileName = articleService.uploadAttachment(file);
        if (fileName != null) {
            return Result.success(fileName);
        } else {
            return Result.error("上传失败");
        }
    }

    /**
     * 删除文章附件
     *
     * @param fileName
     * @return
     */
    @Operation(description = "删除文章附件", summary = "删除文章附件")
    @GetMapping("/deleteAttachment")
    public Result deleteAttachment(@RequestParam Integer aid, @RequestParam String fileName) {
        boolean f = articleService.deleteAttachment(aid, fileName);
        if (fileName != null) {
            return Result.success(fileName);
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * 获取文章附件
     *
     * @param id
     * @return
     */
    @Operation(description = "根据id获取文章附件", summary = "获取文章附件")
    @GetMapping("/getArticleAttachment")
    public Result getArticleAttachment(@RequestParam Integer id) {
        List<Attachment> attachmentList = articleService.getArticleAttachment(id);
        return Result.success(attachmentList);
    }

    /**
     * 保存文章附件
     *
     * @param
     * @return
     */
    @Operation(description = "保存文章附件", summary = "保存文章附件")
    @PostMapping("/saveAttachment")
    public Result saveAttachment(@RequestBody HashMap<String, Object> map, HttpServletRequest request) {
        List<Attachment> attachmentList = JSONArray.parseArray((String) map.get("attachment")).toJavaList(Attachment.class);
        Integer aid = (Integer) map.get("aid");
        String token = request.getHeader("token");
        Integer uid;
        try {
            uid = (Integer) JwtUtils.parseJWT(token).get("userId");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("保存失败");
        }
        Integer f = articleService.saveAttachment(aid, uid, attachmentList);
        return Result.success();
    }
}
