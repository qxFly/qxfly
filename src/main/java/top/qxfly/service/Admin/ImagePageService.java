package top.qxfly.service.Admin;

import top.qxfly.pojo.Image;
import top.qxfly.pojo.PageBean;

public interface ImagePageService {
    /**
     * 分页查询图库
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    PageBean<Image> getImagesByPage(int currPage, int pageSize);

    /**
     * 根据名字查找图片
     *
     * @param currPage
     * @param pageSize
     * @param name
     * @return
     */
    PageBean<Image> searchImagesByNameByPage(int currPage, int pageSize, String name);

    /**
     * 根据名字删除图片
     *
     * @param id
     * @return
     */
    boolean deleteImageByName(Integer id);
}
