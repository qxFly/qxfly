package top.qxfly.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.qxfly.pojo.Site;

import java.util.List;

@Mapper
public interface SiteMapper {
    @Select("select name, address from sites")
    List<Site> listSites();

    @Insert("insert into sites(name,address)values (#{name},#{address})")
    boolean addSite(Site site);
}
