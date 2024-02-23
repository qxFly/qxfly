package top.qxfly.mapper.Admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
