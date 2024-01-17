package top.qxfly.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.qxfly.entity.FilePO;

import java.util.List;


@Mapper
public interface FileMapper {
    public Integer insertFile(FilePO filePO) ;

    FilePO selectFileByMd5(String md5);

    List<FilePO> selectFileList();

    void deleteFileByMd5(String md5);

    @Select("select f_name from file where f_md5 = #{s}")
    String getFileNameByMd5(String s);
}
