package top.qxfly.service.Admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.Admin.AdminPageMapper;
import top.qxfly.pojo.Token;
import top.qxfly.pojo.User;
import top.qxfly.service.Admin.AdminPageService;

import java.util.List;

@Service
public class AdminPageServiceImpl implements AdminPageService {
    @Autowired
    private AdminPageMapper adminPageMapper;

    /**
     * 获取用户信息
     *
     * @return
     */
    @Override
    public List<User> getAllUserInfo(int nextPage) {
        return adminPageMapper.getAllUserInfo(nextPage);
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteUserById(Integer id) {
        return adminPageMapper.deleteUserById(id);
    }

    /**
     * 更改用户信息
     *
     * @param userName
     * @param role
     * @param email
     * @param phone
     * @return
     */
    @Override
    public boolean changeUserInfo(Integer id, String userName, String role, String email, String phone) {
        return adminPageMapper.changeUserInfo(id, userName, role, email, phone);
    }

    /**
     * 根据 token 查找 用户
     *
     * @param token
     * @return
     */
    @Override
    public String getUserNameByToken(Token token) {
        return adminPageMapper.getUserNameByToken(token);
    }

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    @Override
    public boolean check(String username) {
        if ("ADMIN".equals(adminPageMapper.check(username))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得总用户数
     *
     * @return
     */
    @Override
    public int getUserCount() {
        return adminPageMapper.getUserCount();
    }

    /**
     * 根据Id查找用户
     *
     * @param id
     * @return
     */
    @Override
    public List<User> searchUserById(int id) {
        return adminPageMapper.searchUserById(id);
    }

    /**
     * 根据用户名查找用户（可模糊）
     *
     * @param username
     * @return
     */
    @Override
    public List<User> searchUserByUsername(String username) {
        return adminPageMapper.searchUserByUsername(username);
    }
}
