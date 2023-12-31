package top.qxfly.service;


import top.qxfly.pojo.FilePO;

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
