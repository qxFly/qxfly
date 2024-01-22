package top.qxfly.controller.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.qxfly.entity.Token;
import top.qxfly.entity.User;
import top.qxfly.pojo.Result;
import top.qxfly.service.User.UserInfoService;
import top.qxfly.utils.JwtUtils;
import top.qxfly.vo.UserVO;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户")
public class UserInfoController {
    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 用户信息页，信息展示
     *
     * @param token
     * @return
     */
    @Operation(description = "用户信息页，信息展示", summary = "用户信息页，信息展示")
    @PostMapping("/getUserInfo")
    public Result getUserInfo(@RequestBody Token token) {

        if (token.getToken() != null || token.getId() != null) {
            if (token.getToken() != null) {
                try {
                    JwtUtils.parseJWT(token.getToken());
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.error("验证失败");
                }
            }

            boolean refresh = userInfoService.refreshUserInfo(token);
            User user = userInfoService.getUserInfoByToken(token);
            UserVO userVO = new UserVO();

            if (user != null) {
                BeanUtils.copyProperties(user,userVO);
                return Result.success(userVO);
            } else {
                return Result.error("没有相关用户信息");
            }
        } else {
            return Result.error("参数错误");
        }

    }

    /**
     * 更改用户信息
     *
     * @param user
     * @return
     */
    @Operation(description = "更改用户信息", summary = "更改用户信息")
    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody User user) {

        User u = userInfoService.checkUsername(user);
        if (u == null || u.getId().equals(user.getId())) {
            boolean flag = userInfoService.updateUserInfo(user);
            if (flag) {
                return Result.success();
            } else {
                return Result.error("修改失败");
            }
        } else {
            return Result.error("用户名已存在");
        }
    }

    /**
     * 头像上传
     *
     * @param file
     * @return
     */
    @Operation(description = "头像上传", summary = "头像上传")
    @PostMapping("/updateAvatar")
    public Result updateImg(MultipartFile file, Token token) {
        return userInfoService.updateAvatar(file, token);
    }
}
