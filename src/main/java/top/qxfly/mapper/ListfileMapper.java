package top.qxfly.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ListfileMapper {
    @Select("select f_name from file where f_md5 = #{s}")
    String getRealNameByMd5(String s);
}
