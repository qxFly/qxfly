package fun.qxfly.service.Admin.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import fun.qxfly.entity.User;
import fun.qxfly.mapper.Admin.UserManageMapper;
import fun.qxfly.service.Admin.UserManageService;

import java.io.File;
import java.util.List;

@Service
public class UserManageServiceImpl implements UserManageService {
    private final UserManageMapper userManageMapper;

    public UserManageServiceImpl(UserManageMapper userManageMapper) {
        this.userManageMapper = userManageMapper;
    }

    /**
     * 根据条件列出用户
     *
     * @param user
     * @return
     */
    @Override
    public PageInfo<User> listUser(Integer currPage, Integer pageSize, User user) {
        PageHelper.startPage(currPage, pageSize);
        List<User> userList = userManageMapper.listUser(user);
        return new PageInfo<>(userList);
    }

    /**
     * 删除用户头像
     *
     * @param user
     * @return
     */
    @Override
    public boolean deleteUserAvatar(User user) {
        String[] split = user.getAvatar().split("/");
        String s = split[split.length - 1];
        File file = new File(System.getProperty("user.dir") + "/data/qxfly-userAvatar/" + s);
        boolean delete = file.delete();
        if (delete) {
            return userManageMapper.deleteUserAvatar(user);
        } else {
            return false;
        }

    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteUserById(Integer id) {
        return userManageMapper.deleteUserById(id);
    }

    /**
     * 更改用户信息
     * @param user
     * @return
     */
    @Override
    public boolean editUserInfo(User user) {
        return userManageMapper.editUserInfo(user);
    }
}
