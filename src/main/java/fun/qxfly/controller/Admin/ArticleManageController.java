package fun.qxfly.controller.Admin;

import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Article;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Admin.ArticleManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "文章管理")
@Tag(name = "管理员")
public class ArticleManageController {
    @Autowired
    ArticleManageService articleManageService;

    /**
     * 文章审核
     *
     * @param articleId
     * @param verify
     * @return
     */
    @Operation(description = "文章审核", summary = "文章审核")
    @GetMapping("/articleVerify")
    public Result articleVerify(@RequestParam int articleId, @RequestParam int verify, @RequestParam String reason) {
        if (articleManageService.articleVerify(articleId, verify,reason)) {
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    /**
     * 删除文章
     *
     * @param article
     * @return
     */
    @Operation(description = "删除文章", summary = "删除文章")
    @PostMapping("/deleteArticle")
    public Result deleteArticle(@RequestBody Article article) {
        if (articleManageService.deleteArticle(article)) {
            return Result.success();
        } else {
            return Result.error("");
        }
    }

    /**
     * 搜索
     *
     * @param map
     * @return
     */
    @Operation(description = "搜索", summary = "搜索")
    @PostMapping("/searchArticle")
    public Result searchArticle(@RequestBody Map<String, Object> map) {
        PageInfo<Article> pageBean = articleManageService.searchArticle(
                (Integer) map.get("currPage"),
                (Integer) map.get("pageSize"),
                (Integer) map.get("articleId"),
                (String) map.get("title"),
                (String) map.get("author"),
                (Integer) map.get("authorId"),
                (String) map.get("tag"),
                (String) map.get("classify"),
                (String) map.get("createTimeStart"),
                (String) map.get("createTimeEnd"),
                (String) map.get("updateTimeStart"),
                (String) map.get("updateTimeEnd"),
                (Integer) map.get("VERIFY"));
        return Result.success(pageBean);
    }
}
