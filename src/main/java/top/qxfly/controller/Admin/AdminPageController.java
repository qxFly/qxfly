package top.qxfly.controller.Admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.qxfly.pojo.Result;
import top.qxfly.pojo.Token;
import top.qxfly.pojo.User;
import top.qxfly.service.Admin.AdminPageService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manage")
public class AdminPageController {
    @Autowired
    private AdminPageService adminPageService;

    @PostMapping("/check")
    public Result check(@RequestBody Token token) {
        log.info("/check-token:{}", token);
        String username = adminPageService.getUserNameByToken(token);
        if (adminPageService.check(username)) {
            return Result.success();
        } else {
            return Result.error("您不是管理员！");
        }
    }

    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam int currentPage) {
        int userCount = adminPageService.getUserCount();
        int pages = userCount / 10 + 1;
        if (currentPage > pages) {
            List<User> userInfo = adminPageService.getAllUserInfo((pages - 1) * 10);
            return Result.success(userInfo);
        }
        int nextPage = (currentPage - 1) * 10;
        List<User> userInfo = adminPageService.getAllUserInfo(nextPage);
        return Result.success(userInfo);
    }

    @GetMapping("/deleteUser")
    public Result deleteUser(@RequestParam Integer id) {
        log.info("/deleteUser-id:{}", id);
        boolean flag = adminPageService.deleteUserById(id);
        if (flag) {
            return Result.success();
        } else {
            return Result.error("删除失败！");
        }

    }

    @GetMapping("/changeUserInfo")
    public Result changeUserInfo(@RequestParam Integer id,
                                 @RequestParam String userName,
                                 @RequestParam String role,
                                 @RequestParam String email,
                                 @RequestParam String phone) {
        log.info("/changeUserInfo-id:{}\nuserName:{}\nrole:{}\nemail:{}\nphone:{}", id, userName, role, email, phone);
        boolean flag = adminPageService.changeUserInfo(id, userName, role, email, phone);
        if (flag) {
            return Result.success("修改成功", null);
        } else {
            return Result.error("修改失败");
        }
    }

    @GetMapping("/getPage")
    public Result getPage(@RequestParam int currentPage) {
        int userCount = adminPageService.getUserCount();
        int pages = userCount / 10 + 1;
        return Result.success(pages);
    }

    @GetMapping("/searchUser")
    public Result searchUser(@RequestParam Integer id, @RequestParam String username) {
        if (id != 0){
            List<User> user = adminPageService.searchUserById(id);
            return Result.success(user);
        }else{
            List<User> users = adminPageService.searchUserByUsername(username);
            return Result.success(users);
        }


    }
}
