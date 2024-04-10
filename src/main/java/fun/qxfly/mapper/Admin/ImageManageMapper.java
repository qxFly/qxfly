package fun.qxfly.mapper.Admin;

import fun.qxfly.entity.Image;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ImageManageMapper {
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
     * 分页查询图库
     *
     * @return
     */
    @Select("select id, name, url from image limit #{startPage}, #{pageSize}")
    List<Image> getAllImageInfo(int startPage, int pageSize);

    /**
     * 获取所有图片数
     *
     * @return
     */
    @Select("select count(id) from image")
    int getAllImagesCount();

    /**
     * 获取所有图片
     *
     * @return
     */
    @Select("select * from image")
    List<Image> getAllImages();

    /**
     * 分页查询图库
     *
     * @param createTimeStart
     * @param createTimeEnd
     * @return
     */
    List<Image> getImagesByPage(Integer aid, String originName, String createTimeStart, String createTimeEnd, Integer verify);
}
