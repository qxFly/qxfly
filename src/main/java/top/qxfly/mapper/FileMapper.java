package top.qxfly.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.qxfly.pojo.FilePO;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("insert into file(f_name,f_md5,f_size)values(#{name},#{md5},#{size});")
    public Integer insertFile(FilePO filePO);

    @Select("select * from file where f_md5 = #{md5}")
    FilePO selectFileByMd5(String md5);

    @Select("select * from file order by f_id desc")
    List<FilePO> selectFileList();
}
