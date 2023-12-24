package top.qxfly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Image;
import top.qxfly.pojo.Result;
import top.qxfly.service.ImageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@CrossOrigin
public class ImageController {

    @Autowired
    ImageService imageService;

    /**
     * 更新图库
     *
     * @return
     */
    @GetMapping("/updateImage")
    public Result updateImage() {
        return imageService.updateImage();
    }


    /**
     * 获取图片
     *
     * @param count
     * @return
     */
    /* 图库缓存，避免重复多次调用数据库 */
    List<Image> listCache = new ArrayList<>();
    int cacheUsage = 0;

    @GetMapping("/getimage")
    public Result getImageController(@RequestParam int count) {
        List<Image> imageList;
        /* 查看是否有缓存，如果缓存使用次数大于1000次更新图库 */
        if (listCache == null || listCache.isEmpty() || cacheUsage > 1000) {
            imageList = imageService.getAllImage();
            cacheUsage = 0;
            listCache = imageList;
        }
        Random random = new Random();
        List<Image> images = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            images.add(listCache.get(random.nextInt(listCache.size())));
        }
        return Result.success(images);
    }
}
