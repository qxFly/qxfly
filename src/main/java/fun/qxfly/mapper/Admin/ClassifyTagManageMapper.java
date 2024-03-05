package fun.qxfly.mapper.Admin;

import fun.qxfly.entity.Classify;
import fun.qxfly.entity.Tag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ClassifyTagManageMapper {
    /**
     * 删除分类
     *
     * @param classify
     * @return
     */
    @Delete("delete from classify where id = #{id}")
    boolean deleteClassify(Classify classify);

    /**
     * 添加分类
     *
     * @param classify
     * @return
     */
    @Insert("insert into classify(name) values(#{name})")
    boolean addClassify(Classify classify);

    /**
     * 删除标签
     *
     * @param tag
     * @return
     */
    @Delete("delete from tag where id = #{id}")
    boolean deleteTag(Tag tag);

    /**
     * 添加标签
     *
     * @param tag
     * @return
     */
    @Insert("insert into tag(name) values(#{name})")
    boolean addTag(Tag tag);

    /**
     * 更新分类
     *
     * @param classify
     * @return
     */
    @Update("update classify set name = #{name} where id = #{id}")
    boolean updateClassify(Classify classify);

    /**
     * 更新标签
     *
     * @param tag
     * @return
     */
    @Update("update tag set name = #{name} where id = #{id}")
    boolean updateTag(Tag tag);
}
