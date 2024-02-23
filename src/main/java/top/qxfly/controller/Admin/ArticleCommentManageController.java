package top.qxfly.controller.Admin;

import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.entity.Comment;
import top.qxfly.pojo.Result;
import top.qxfly.service.Admin.ArticleCommentManageService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "文章评论管理")
@Tag(name = "管理员")
public class ArticleCommentManageController {

    final
    ArticleCommentManageService articleCommentManageService;

    public ArticleCommentManageController(ArticleCommentManageService articleCommentManageService) {
        this.articleCommentManageService = articleCommentManageService;
    }

    /**
     * 文章评论审核
     * @param comment
     * @return
     */
    @Operation(description = "文章评论审核", summary = "文章评论审核")
    @PostMapping("/articleCommentVerify")
    public Result articleCommentVerify(@RequestBody Comment comment) {
        if (articleCommentManageService.articleCommentVerify(comment.getId(), comment.getVerify())) {
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    /**
     * 删除文章评论
     *
     * @param comment
     * @return
     */
    @Operation(description = "删除文章评论", summary = "删除文章评论")
    @PostMapping("/deleteArticleComment")
    public Result deleteArticleComment(@RequestBody Comment comment) {
        if (articleCommentManageService.deleteArticleComment(comment.getId())) {
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    /**
     * 搜索评论
     *
     * @param map
     * @return
     */
    @Operation(description = "搜索评论", summary = "搜索评论")
    @PostMapping("/listArticleComment")
    public Result listArticleComment(@RequestBody Map<String, Object> map) {
        map.forEach((a, b) -> {
//            log.info("{}={}", a, b);
        });
        Comment comment = new Comment();
        comment.setId((Integer) map.get("comId"));
        comment.setArticleId((Integer) map.get("articleId"));
        comment.setContent((String) map.get("content"));
        comment.setParentCommentId((Integer) map.get("parentCommentId"));
        comment.setUserId((Integer) map.get("userId"));
        comment.setUsername((String) map.get("username"));
        comment.setToUserId((Integer) map.get("toUserId"));
        comment.setToUsername((String) map.get("toUsername"));
        comment.setVerify((Integer) map.get("verify"));
        PageInfo<Comment> pageInfo = articleCommentManageService.searchArticleComment((Integer) map.get("currPage"), (Integer) map.get("pageSize"), comment, (String) map.get("createTimeStart"), (String) map.get("createTimeEnd"));
        return Result.success(pageInfo);
    }

}
