package top.qxfly.controller.Admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Image;
import top.qxfly.pojo.PageBean;
import top.qxfly.pojo.Result;
import top.qxfly.pojo.User;
import top.qxfly.service.Admin.ImagePageService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manage")
public class ImagePageController {
    @Autowired
    private ImagePageService imagePageService;

    /**
     * 分页查询图库
     *
     * @param currPage
     * @param pageSize
     * @return
     */
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
