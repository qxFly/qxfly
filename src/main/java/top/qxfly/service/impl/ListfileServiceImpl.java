package top.qxfly.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.ListfileMapper;
import top.qxfly.service.ListfileService;

@Service
public class ListfileServiceImpl implements ListfileService {
    @Autowired
    private ListfileMapper listfileMapper;
    /**
     * 根据md5获取文件真实名字
     *
     * @param s
     * @return
     */
    @Override
    public String getRealName(String s) {
        return listfileMapper.getRealNameByMd5(s);
    }
}
