package fun.qxfly.controller.Admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fun.qxfly.entity.Classify;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Admin.AdminService;
import fun.qxfly.utils.JwtUtils;

@Slf4j
@RestController
@RequestMapping("/manage")
@Tag(name = "管理员")
public class AdminController {
    @Autowired
    AdminService adminService;

    /**
     * 检查是否为管理员
     *
     * @return
     */
    @Operation(description = "检查是否为管理员、审核员", summary = "检查是否为管理员、审核员")
    @PostMapping("/check")
    public Result check(HttpServletRequest request) {
        String username;
        String token = request.getHeader("token");
        try {
            username = (String) JwtUtils.parseJWT(token).get("username");
            Integer f;
            if ((f = adminService.check(username)) != 0) {
                return Result.success(f);
            } else {
                return Result.error("您没有权限访问！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("您没有权限访问！");
        }

    }
}
