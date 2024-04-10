package fun.qxfly.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import fun.qxfly.entity.Image;

import java.util.List;

@Mapper
public interface ImageMapper {
    /**
     * 获取所有图片
     *
     * @return
     */
    @Select("select i.* from image i, article a where i.aid = a.id and a.verify = 3")
    List<Image> getAllImage();

    /**
     * 分页获取图片
     * @param sort
     * @return
     */
    List<Image> getImageByPage(String sort);
}
