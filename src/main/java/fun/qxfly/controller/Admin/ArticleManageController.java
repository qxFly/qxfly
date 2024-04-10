package fun.qxfly.controller.Admin;

import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Article;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Admin.ArticleManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
     * @param map
     * @return
     */
    @Operation(description = "文章审核", summary = "文章审核")
    @PostMapping("/articleVerify")
    public Result articleVerify(@RequestBody HashMap<String, Object> map) {
        Integer aid = (Integer) map.get("aid");
        Integer verify = (Integer) map.get("verify");
        String reason = (String) map.get("reason");
        String tags = (String) map.get("tags");
        String classify = (String) map.get("classify");
        Article article = new Article();
        article.setVerify(verify);
        article.setId(aid);
        article.setTag(tags);
        article.setClassify(classify);
        if (articleManageService.articleVerify(article, reason)) {
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
