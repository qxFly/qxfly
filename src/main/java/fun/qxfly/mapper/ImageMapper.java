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
    @Select("select * from image")
    List<Image> getAllImage();
}
