package fun.qxfly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Image;
import fun.qxfly.mapper.ImageMapper;
import fun.qxfly.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageMapper imageMapper;

    public ImageServiceImpl(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    @Value("${file.articleImage.download.path}")
    private String articleImageDownloadPath;

    /**
     * 获取所有图片
     *
     * @return
     */
    @Override
    public List<Image> getAllImage() {
        return imageMapper.getAllImage();
    }

    /**
     * 分页获取图片
     *
     * @param currPage
     * @param pageSize
     * @param sort
     * @return
     */
    @Override
    public PageInfo<Image> getImageByPage(int currPage, int pageSize, String sort) {
        PageHelper.startPage(currPage, pageSize);
        List<Image> imageList = imageMapper.getImageByPage(sort);
        for (Image image : imageList) {
            image.setUrl(articleImageDownloadPath + image.getName());
        }
        PageInfo<Image> pageInfo = new PageInfo<>(imageList);
        return pageInfo;
    }

}
