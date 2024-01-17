package top.qxfly.service;

import top.qxfly.entity.Image;
import top.qxfly.pojo.Result;

import java.util.List;

public interface ImageService {
    /**
     * 获取所有图片
     *
     * @return
     */
    List<Image> getAllImage();


}
