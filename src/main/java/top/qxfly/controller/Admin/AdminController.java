package top.qxfly.controller.Admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;
import top.qxfly.pojo.Token;
import top.qxfly.service.Admin.AdminService;

@Slf4j
@RestController
@RequestMapping("/manage")
public class AdminController {
    @Autowired
    AdminService adminService;
    /**
     * 检查是否为管理员
     * @param token
     * @return
     */
    @PostMapping("/check")
    public Result check(@RequestBody Token token) {
        String username = adminService.getUserNameByToken(token);
        if (adminService.check(username)) {
            return Result.success();
        } else {
            return Result.error("您不是管理员！");
        }
    }
}
