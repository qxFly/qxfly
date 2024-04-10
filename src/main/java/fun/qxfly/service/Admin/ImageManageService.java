package fun.qxfly.service.Admin;

import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Image;
import fun.qxfly.pojo.Result;

public interface ImageManageService {
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
    PageInfo<Image> getImagesByPage(int currPage, int pageSize, Integer aid, String originName, String createTimeStart, String createTimeEnd, Integer verify);
}
