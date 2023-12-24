package top.qxfly.service;

import top.qxfly.pojo.Image;
import top.qxfly.pojo.Result;

import java.util.List;

public interface ImageService {
    /**
     * 获取所有图片
     *
     * @return
     */
    List<Image> getAllImage();

    /**
     * 更新图库
     * @return
     */
    Result updateImage();
}
