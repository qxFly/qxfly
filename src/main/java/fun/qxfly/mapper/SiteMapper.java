package fun.qxfly.mapper;

import fun.qxfly.entity.Site;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SiteMapper {
    /**
     * 分页查询站点
     * @param name
     * @return
     */
    @Select("select * from sites where name like concat('%',#{name},'%')")
    List<Site> listSites(String name);
}
