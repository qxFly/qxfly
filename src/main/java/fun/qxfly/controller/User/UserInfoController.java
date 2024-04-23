package fun.qxfly.controller.User;

import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Token;
import fun.qxfly.entity.User;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.User.LoginService;
import fun.qxfly.service.User.UserInfoService;
import fun.qxfly.utils.JwtUtils;
import fun.qxfly.utils.RSAEncrypt;
import fun.qxfly.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户")
public class UserInfoController {
    private final UserInfoService userInfoService;
    private final LoginService loginService;

    public UserInfoController(UserInfoService userInfoService, LoginService loginService) {
        this.userInfoService = userInfoService;
        this.loginService = loginService;
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
                    token.setId((Integer) JwtUtils.parseJWT(token.getToken()).get("userId"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.error("验证失败");
                }
            }

            boolean refresh = userInfoService.refreshUserInfoTask(token);
            User user = userInfoService.getUserInfoByToken(token);
            UserVO userVO = new UserVO();
            if (user != null) {
                BeanUtils.copyProperties(user, userVO);
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
    public Result updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader("token");
        String username;
        Integer uid;
        try {
            username = (String) JwtUtils.parseJWT(token).get("username");
            uid = (Integer) JwtUtils.parseJWT(token).get("userId");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("登录状态异常");
        }
        User u = userInfoService.checkUsername(user);
        String op = userInfoService.getOriginPhone(uid);
        if ((user.getPassword() != null && !user.getPassword().equals("")) || (!Objects.equals(user.getPhone(), op))) {
            if (user.getRole() != null) {
                User user1;
                user1 = user;
                user1.setPhone(op);
                int f = userInfoService.testCode(user1);
                if (f == 0) return Result.error("验证码错误");
                else if (f == -1) return Result.error("请获取验证码");
            } else {
                return Result.error("请输入验证码");
            }

        }

        /*用户名可行*/
        if (u == null || u.getId().equals(user.getId())) {
            user.setAvatar(null);
            boolean flag = userInfoService.updateUserInfo(user);
            if (flag) {
                if (!username.equals(user.getUsername())) {
                    String newToken = JwtUtils.createToken(user.getId(), user.getUsername(), new Date(), null);
                    loginService.updateToken(user.getUsername(), newToken, new Date().getTime());
                }
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

    /**
     * 获取推荐作者
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @Operation(description = "获取推荐作者", summary = "获取推荐作者")
    @GetMapping("/getSuggestAuthor")
    public Result getSuggestAuthor(@RequestParam(defaultValue = "1") int currPage, @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<UserVO> pageInfo = userInfoService.getSuggestAuthorByPage(currPage, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 发送验证码
     *
     * @param user
     * @return
     */
    @Operation(description = "发送验证码", summary = "发送验证码")
    @PostMapping("/sendCode")
    public Result sendCode(@RequestBody User user) {
//        return Result.success("发送成功");
        int i = userInfoService.sendCode(user);
        return i == -1 ? Result.error("发送失败，服务端错误") : Result.success();
    }

    /**
     * 找回密码
     *
     * @param map
     * @return
     */
    @Operation(description = "找回密码", summary = "找回密码")
    @PostMapping("/findPassword")
    public Result findPassword(@RequestBody HashMap<String, String> map) {
        String encodePassword = (String) map.get("password");
        String publicKey = (String) map.get("salt");
        String decodePassword;
        String privateKey = loginService.getPrivateKey(publicKey);
        try {
            decodePassword = RSAEncrypt.decrypt(encodePassword, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
        Integer i = userInfoService.resetPassword(map.get("phone"), decodePassword, Integer.valueOf(map.get("code")));
        if (i == -1) {
            return Result.error("请获取验证码");
        } else if (i == 0) {
            return Result.error("验证码错误");
        } else {
            return Result.success("修改成功，请登录");
        }
    }
}
