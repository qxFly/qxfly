package fun.qxfly.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fun.qxfly.entity.Image;
import fun.qxfly.mapper.ImageMapper;
import fun.qxfly.service.ImageService;

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
