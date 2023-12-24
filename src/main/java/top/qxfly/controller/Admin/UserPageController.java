package top.qxfly.controller.Admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.PageBean;
import top.qxfly.pojo.Result;
import top.qxfly.pojo.User;
import top.qxfly.service.Admin.UserPageService;

@Slf4j
@RestController
@RequestMapping("/manage")
public class UserPageController {
    @Autowired
    private UserPageService userPageService;

    /**
     * 分页查询用户数据
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @GetMapping("/getUsersByPage")
    public Result getUsersByPage(@RequestParam int currPage, @RequestParam int pageSize) {
        PageBean<User> pageBean = userPageService.getUsersByPage(currPage, pageSize);
        return Result.success(pageBean);
    }

    /**
     * 搜索用户
     *
     * @param id
     * @param username
     * @return
     */
    @GetMapping("/searchUser")
    public Result searchUser(@RequestParam int currPage, @RequestParam int pageSize, @RequestParam Integer id, @RequestParam String username) {
        if (id != 0) {
            User user = userPageService.searchUserById(id);
            return Result.success(user);
        } else {
            PageBean<User> pageBean = userPageService.searchUserByUsernameByPage(currPage, pageSize, username);
            return Result.success(pageBean);
        }
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteUser")
    public Result deleteUser(@RequestParam Integer id) {
        boolean flag = userPageService.deleteUserById(id);
        if (flag) {
            return Result.success();
        } else {
            return Result.error("删除失败！");
        }

    }

    /**
     * 更改用户信息
     *
     * @param id
     * @param userName
     * @param role
     * @param email
     * @param phone
     * @return
     */
    @GetMapping("/changeUserInfo")
    public Result changeUserInfo(@RequestParam Integer id,
                                 @RequestParam String userName,
                                 @RequestParam String role,
                                 @RequestParam String email,
                                 @RequestParam String phone) {
        boolean flag = userPageService.changeUserInfo(id, userName, role, email, phone);
        if (flag) {
            return Result.success("修改成功", null);
        } else {
            return Result.error("修改失败");
        }
    }
}
