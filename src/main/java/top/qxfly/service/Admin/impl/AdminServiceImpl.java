package top.qxfly.service.Admin.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.Admin.AdminMapper;
import top.qxfly.pojo.Token;
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
    public boolean check(String username) {
        if ("ADMIN".equals(adminMapper.check(username))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据 token 查找 用户
     *
     * @param token
     * @return
     */
    @Override
    public String getUserNameByToken(Token token) {
        return adminMapper.getUserNameByToken(token);
    }
}
