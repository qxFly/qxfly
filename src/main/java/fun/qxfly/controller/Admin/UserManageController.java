package fun.qxfly.controller.Admin;

import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import fun.qxfly.entity.User;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Admin.UserManageService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "管理员")
public class UserManageController {
    @Autowired
    private UserManageService userManageService;

    /**
     * 列出用户
     *
     * @param map
     * @return
     */
    @Operation(description = "列出用户", summary = "列出用户")
    @PostMapping("/listUser")
    public Result listUser(@RequestBody Map<String, Object> map) {
        User user = new User();
        user.setId((Integer) map.get("id"));
        user.setUsername((String) map.get("username"));
        user.setIntroduction((String) map.get("introduction"));
        user.setLocation((String) map.get("location"));
        user.setRole((Integer) map.get("role"));
        user.setEmail((String) map.get("email"));
        user.setPhone((String) map.get("phone"));
        PageInfo<User> pageBean = userManageService.listUser((Integer) map.get("currPage"), (Integer) map.get("pageSize"), user);
        return Result.success(pageBean);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Operation(description = "删除用户", summary = "删除用户")
    @GetMapping("/deleteUser")
    public Result deleteUser(@RequestParam Integer id) {
        boolean flag = userManageService.deleteUserById(id);
        if (flag) {
            return Result.success();
        } else {
            return Result.error("删除失败！");
        }

    }

    /**
     * 编辑用户信息
     *
     * @param user
     * @return
     */
    @Operation(description = "编辑用户信息", summary = "编辑用户信息")
    @PostMapping("/editUserInfo")
    public Result editUserInfo(@RequestBody User user) {
        boolean flag = userManageService.editUserInfo(user);
        if (flag) {
            return Result.success("修改成功", null);
        } else {
            return Result.error("修改失败");
        }
    }

    /**
     * 删除用户头像
     *
     * @param user
     * @return
     */
    @Operation(description = "删除用户头像", summary = "删除用户头像")
    @PostMapping("/deleteUserAvatar")
    public Result deleteUserAvatar(@RequestBody User user) {
        boolean flag = userManageService.deleteUserAvatar(user);
        if (flag) {
            return Result.success("修改成功", null);
        } else {
            return Result.error("修改失败");
        }
    }
}
