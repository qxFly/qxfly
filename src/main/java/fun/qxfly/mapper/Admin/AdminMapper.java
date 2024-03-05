package fun.qxfly.mapper.Admin;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import fun.qxfly.entity.Classify;

@Mapper
public interface AdminMapper {

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    @Select("select role from user where username = #{username}")
    Integer check(String username);


}
