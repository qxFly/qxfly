package fun.qxfly.controller.Admin;

import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Image;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Admin.ImageManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "图库")
@Tag(name = "管理员")
public class ImageManageController {
    @Autowired
    private ImageManageService imageManageService;

    /**
     * 更新图库
     *
     * @return
     */
    @Operation(description = "更新图库", summary = "更新图库")
    @GetMapping("/updateImage")
    public Result updateImage() {
        return imageManageService.updateImage();
    }

    /**
     * 分页查询图库
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @Operation(description = "分页查询图库", summary = "分页查询图库")
    @PostMapping("/listImages")
    public Result getImagesByPage(@RequestBody HashMap<String, Object> map) {
        Integer currPage = (Integer) map.get("currPage");
        Integer pageSize = (Integer) map.get("pageSize");
        String createTimeStart = (String) map.get("createTimeStart");
        String createTimeEnd = (String) map.get("createTimeEnd");
        String originName = (String) map.get("originName");
        Integer verify = (Integer) map.get("verify");
        Integer aid = (Integer) map.get("aid");
        PageInfo<Image> pageBean = imageManageService.getImagesByPage(currPage, pageSize, aid,originName, createTimeStart, createTimeEnd, verify);
        return Result.success(pageBean);
    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    @Operation(description = "删除图片", summary = "删除图片")
    @GetMapping("/deleteImage")
    public Result deleteImage(@RequestParam Integer id) {
        return Result.success();
    }
}
