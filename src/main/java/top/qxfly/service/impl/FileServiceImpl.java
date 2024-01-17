package top.qxfly.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.FileMapper;
import top.qxfly.entity.FilePO;
import top.qxfly.service.FileService;

import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;

    @Override
    public Integer addFile(FilePO filePO) {
        return fileMapper.insertFile(filePO);
    }

    @Override
    public Boolean selectFileByMd5(String md5) {
        FilePO filePO = fileMapper.selectFileByMd5(md5);
        return filePO != null;
    }

    @Override
    public List<FilePO> selectFileList() {
        List<FilePO> list = fileMapper.selectFileList();
        log.info("FileServiceImpl.list:{}", list);
        return list;
    }

    @Override
    public void deleteFile(String md5) {
        fileMapper.deleteFileByMd5(md5);
    }

    /**
     * 根据md5获取文件真实名字
     *
     * @param s
     * @return
     */
    @Override
    public String getFileName(String s) {
        return fileMapper.getFileNameByMd5(s);
    }
}
