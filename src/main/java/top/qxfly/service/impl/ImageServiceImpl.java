package top.qxfly.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.entity.Image;
import top.qxfly.mapper.ImageMapper;
import top.qxfly.service.ImageService;

import java.util.List;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    ImageMapper imageMapper;

    /**
     * 获取所有图片
     *
     * @return
     */
    @Override
    public List<Image> getAllImage() {
        return imageMapper.getAllImage();
    }

}
