package top.qxfly.mapper.Admin;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.qxfly.pojo.Image;
import top.qxfly.pojo.PageBean;
import top.qxfly.pojo.User;

import java.util.List;

@Mapper
public interface ImagePageMapper {
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
     * 根据名字删除图片
     *
     * @param id
     * @return
     */
    @Delete("delete from image where id = #{id}")
    boolean deleteImageByName(Integer id);

    /**
     * 根据名字查找图片
     *
     * @param name
     * @return
     */
    @Select("select * from image where name like concat('%',#{name},'%') limit #{startPage},#{pageSize}")
    List<Image> searchImagesByName(int startPage, int pageSize,String name);

    /**
     * 根据名字查找图片数
     * @return
     */
    @Select("select count(*) from image where name like concat('%',#{name},'%') ")
    int getImagesCountByName(String name);

    /**
     * 获取所有图片
     * @return
     */
    @Select("select * from image")
    List<Image> getAllImages();
}
