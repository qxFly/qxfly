package top.qxfly.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.qxfly.entity.Site;

import java.util.List;

@Mapper
public interface SiteMapper {
    @Select("select id, name, address from sites")
    List<Site> listSites();

    @Insert("insert into sites(name,address)values (#{name},#{address})")
    boolean addSite(Site site);

    @Delete("delete from sites where id = #{id}")
    boolean deleteSiteById(Site site);
}
