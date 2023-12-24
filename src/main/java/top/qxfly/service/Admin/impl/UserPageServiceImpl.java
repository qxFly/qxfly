package top.qxfly.service.Admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.Admin.UserPageMapper;
import top.qxfly.pojo.PageBean;
import top.qxfly.pojo.User;
import top.qxfly.service.Admin.UserPageService;

import java.util.List;

@Service
public class UserPageServiceImpl implements UserPageService {
    /**
     * 分页查询用户数据
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<User> getUsersByPage(int currPage, int pageSize) {
        /*获取所有记录数*/
        int count = userPageMapper.getAllUserCount();
        /*创建分页信息*/
        PageBean<User> userPageBean = new PageBean<>(currPage, pageSize, count);
        /*获取分页记录数*/
        List<User> pageUserInfo = userPageMapper.getAllUserInfo(userPageBean.getStart(), pageSize);
        /*保存分页记数据*/
        userPageBean.setData(pageUserInfo);

        return userPageBean;
    }

    /**
     * 根据Id查找用户
     *
     * @param id
     * @return
     */
    @Override
    public User searchUserById(int id) {
        /*获取分页记录数*/
        User user = userPageMapper.searchUserById(id);
        return user;
    }

    /**
     * 根据用户名查找用户（可模糊）
     *
     * @param currPage
     * @param pageSize
     * @param username
     * @return
     */
    @Override
    public PageBean<User> searchUserByUsernameByPage(int currPage, int pageSize, String username) {
        /*获取所有记录数*/
        int count = userPageMapper.getUserCountByName(username);
        /*创建分页信息*/
        PageBean<User> userPageBean = new PageBean<>(currPage, pageSize, count);
        /*获取分页记录数*/
        List<User> pageUserInfo = userPageMapper.searchUserByUsername(userPageBean.getStart(), pageSize, username);
        /*保存分页记数据*/
        userPageBean.setData(pageUserInfo);
        return userPageBean;
    }

    @Autowired
    private UserPageMapper userPageMapper;

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteUserById(Integer id) {
        return userPageMapper.deleteUserById(id);
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
        return userPageMapper.changeUserInfo(id, userName, role, email, phone);
    }
}
