package fun.qxfly.service.Admin;

import fun.qxfly.entity.Image;
import fun.qxfly.pojo.PageBean;
import fun.qxfly.pojo.Result;

public interface ImagePageService {
    /**
     * 更新图库
     *
     * @return
     */
    Result updateImage();

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
