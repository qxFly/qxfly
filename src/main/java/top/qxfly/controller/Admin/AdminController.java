package top.qxfly.controller.Admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;
import top.qxfly.entity.Token;
import top.qxfly.service.Admin.AdminService;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "管理员")
public class AdminController {
    @Autowired
    AdminService adminService;
    /**
     * 检查是否为管理员
     * @param token
     * @return
     */
    @Operation(description = "传入token,检查是否为管理员",summary = "检查是否为管理员")
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
