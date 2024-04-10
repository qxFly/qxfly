package fun.qxfly.controller.Admin;

import fun.qxfly.entity.Classify;
import fun.qxfly.entity.Tag;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Admin.ClassifyTagManageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/manage")
@io.swagger.v3.oas.annotations.tags.Tag(name = "管理员")
@io.swagger.v3.oas.annotations.tags.Tag(name = "分类、标签管理")
public class ClassifyTagManageController {
    private final ClassifyTagManageService classifyTagManageService;

    public ClassifyTagManageController(ClassifyTagManageService classifyTagManageService) {
        this.classifyTagManageService = classifyTagManageService;
    }
    // 分类、标签的获取在 ArticleController 中

    /**
     * 删除分类
     *
     * @return
     */
    @Operation(description = "删除分类", summary = "删除分类")
    @PostMapping("/deleteClassify")
    public Result deleteClassify(@RequestBody Classify classify) {
        boolean f = classifyTagManageService.deleteClassify(classify);
        return Result.success();
    }

    /**
     * 添加分类
     *
     * @return
     */
    @Operation(description = "添加分类", summary = "添加分类")
    @PostMapping("/addClassify")
    public Result addClassify(@RequestBody Classify classify) {
        boolean f = classifyTagManageService.addClassify(classify);
        if (f) return Result.success();
        else return Result.error("分类已存在");
    }

    /**
     * 修改分类
     *
     * @return
     */
    @Operation(description = "修改分类", summary = "修改分类")
    @PostMapping("/updateClassify")
    public Result updateClassify(@RequestBody Classify classify) {
        boolean f = classifyTagManageService.updateClassify(classify);
        return Result.success();
    }

    /**
     * 删除标签
     *
     * @return
     */
    @Operation(description = "删除标签", summary = "删除标签")
    @PostMapping("/deleteTag")
    public Result deleteTag(@RequestBody Tag tag) {
        boolean f = classifyTagManageService.deleteTag(tag);
        return Result.success();
    }

    /**
     * 添加标签
     *
     * @return
     */
    @Operation(description = "添加标签", summary = "添加标签")
    @PostMapping("/addTag")
    public Result addTag(@RequestBody Tag tag) {
        boolean f = classifyTagManageService.addTag(tag);
        if (f) return Result.success();
        else return Result.error("标签已存在");
    }

    /**
     * 修改标签
     *
     * @return
     */
    @Operation(description = "修改标签", summary = "修改标签")
    @PostMapping("/updateTag")
    public Result updateTag(@RequestBody Tag tag) {
        boolean f = classifyTagManageService.updateTag(tag);
        return Result.success();
    }
}
