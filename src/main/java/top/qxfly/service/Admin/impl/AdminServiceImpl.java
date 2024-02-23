package top.qxfly.service.Admin.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.Admin.AdminMapper;
import top.qxfly.service.Admin.AdminService;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminMapper adminMapper;

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    @Override
    public Integer check(String username) {
        return adminMapper.check(username);
    }

}
