package top.qxfly.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.qxfly.pojo.Image;

import java.util.List;

@Mapper
public interface ImageMapper {
    /**
     * 查询数据库是否有相同名字的图片
     *
     * @param name
     * @return
     */
    @Select("select id from image where name = #{name}")
    Integer getIdByName(String name);

    /**
     * 添加图片
     *
     * @param name
     * @param url
     */
    @Insert("insert into image(name, url)values(#{name}, #{url}) ")
    int addImage(String name, String url);

    /**
     * 获取所有图片
     *
     * @return
     */
    @Select("select * from image")
    List<Image> getAllImage();
}
