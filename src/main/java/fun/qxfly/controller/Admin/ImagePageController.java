package fun.qxfly.controller.Admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fun.qxfly.entity.Image;
import fun.qxfly.pojo.PageBean;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Admin.ImagePageService;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "图库")
@Tag(name = "管理员")
public class ImagePageController {
    @Autowired
    private ImagePageService imagePageService;

    /**
     * 更新图库
     *
     * @return
     */
    @Operation(description = "更新图库",summary = "更新图库")
    @GetMapping("/updateImage")
    public Result updateImage() {
        return imagePageService.updateImage();
    }

    /**
     * 分页查询图库
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @Operation(description = "分页查询图库",summary = "分页查询图库")
    @GetMapping("/getImagesByPage")
    public Result getImagesByPage(@RequestParam int currPage, @RequestParam int pageSize) {
        PageBean<Image> pageBean = imagePageService.getImagesByPage(currPage, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 根据名字查找图片
     *
     * @param name
     * @return
     */
    @Operation(description = "根据名字查找图片",summary = "根据名字查找图片")
    @GetMapping("/searchImage")
    public Result searchImage(@RequestParam int currPage, @RequestParam int pageSize, @RequestParam String name) {
        PageBean<Image> pageBean = imagePageService.searchImagesByNameByPage(currPage, pageSize, name);
        return Result.success(pageBean);
    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    @Operation(description = "删除图片",summary = "删除图片")
    @GetMapping("/deleteImage")
    public Result deleteImage(@RequestParam Integer id) {
        boolean flag = imagePageService.deleteImageByName(id);
        if (flag) {
            return Result.success();
        } else {
            return Result.error("删除失败！");
        }

    }
}
