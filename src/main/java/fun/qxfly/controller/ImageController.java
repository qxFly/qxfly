package fun.qxfly.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fun.qxfly.entity.Image;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.ImageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
@CrossOrigin
@Tag(name = "图库")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
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

    @Operation(description = "获取图片，可传入数量",summary = "获取图片")
    @GetMapping("/getImage")
    public Result getImage(@RequestParam int count) {
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
