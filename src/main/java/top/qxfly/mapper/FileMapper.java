package top.qxfly.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.qxfly.pojo.FilePO;

import java.util.List;


@Mapper
public interface FileMapper {
    public Integer insertFile(FilePO filePO) ;

    FilePO selectFileByMd5(String md5);

    List<FilePO> selectFileList();
}
