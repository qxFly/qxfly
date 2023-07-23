package top.qxfly.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.FileMapper;
import top.qxfly.pojo.FilePO;
import top.qxfly.service.FileService;

import java.util.List;

@Service
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
        return  list;
    }
}
