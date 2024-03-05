package fun.qxfly.service;


import fun.qxfly.entity.FilePO;

import java.util.List;

public interface FileService {
    Integer addFile(FilePO filePO);

    Boolean selectFileByMd5(String md5);

    List<FilePO> selectFileList();

    void deleteFile(String md5);

    /**
     * 根据md5获取文件真实名字
     *
     * @param s
     * @return
     */
    String getFileName(String s);
}
