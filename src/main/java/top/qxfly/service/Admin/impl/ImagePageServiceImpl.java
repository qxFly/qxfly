package top.qxfly.service.Admin.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.Admin.ImagePageMapper;
import top.qxfly.pojo.Image;
import top.qxfly.pojo.PageBean;
import top.qxfly.service.Admin.ImagePageService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ImagePageServiceImpl implements ImagePageService {
    @Autowired
    private ImagePageMapper imagePageMapper;

    /**
     * 分页查询图库
     *
     * @param currPage
     * @param pageSize
     * @return
     */

    /* 图库缓存，避免重复多次调用数据库 */
    List<Image> listCache = new ArrayList<>();//图库资源
    int ImagesCountCache = 0;//图库总数
    int cacheUsage = 0;//缓存使用次数

    @Override
    public PageBean<Image> getImagesByPage(int currPage, int pageSize) {
        /* 查看是否有缓存，如果缓存使用次数大于1000次更新图库 */
        if (listCache == null || listCache.isEmpty() || cacheUsage > 20) {
            /*获取所有记录数*/
            ImagesCountCache = imagePageMapper.getAllImagesCount();
            listCache = imagePageMapper.getAllImages();
            cacheUsage = 0;
        }
        /*创建分页信息*/
        PageBean<Image> imagePageBean = new PageBean<>(currPage, pageSize, ImagesCountCache);
        /*获取分页记录数*/
//        List<Image> pageImagesInfo = imagePageMapper.getAllImageInfo(imagePageBean.getStart(), pageSize);
//        imagePageBean.setData(pageImagesInfo);
        List<Image> imageList = new ArrayList<>();
        for (int i = imagePageBean.getStart(); i < imagePageBean.getEnd(); i++) {
            if (i < listCache.size()) {
                imageList.add(listCache.get(i));
            }
        }
        /*保存分页记数据*/
        imagePageBean.setData(imageList);
        cacheUsage++;
        return imagePageBean;
    }

    /**
     * 根据名字查找图片
     *
     * @param currPage
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public PageBean<Image> searchImagesByNameByPage(int currPage, int pageSize, String name) {
        /*获取所有记录数*/
        int count = imagePageMapper.getImagesCountByName(name);
        /*创建分页信息*/
        PageBean<Image> imagePageBean = new PageBean<>(currPage, pageSize, count);
        /*获取分页记录数*/
        List<Image> pageImagesInfo = imagePageMapper.searchImagesByName(imagePageBean.getStart(), pageSize, name);
        imagePageBean.setData(pageImagesInfo);
        return imagePageBean;
    }

    /**
     * 根据名字删除图片
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteImageByName(Integer id) {
        return imagePageMapper.deleteImageByName(id);
    }
}
