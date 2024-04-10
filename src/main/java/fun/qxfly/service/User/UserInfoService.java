package fun.qxfly.service.User;

import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;
import fun.qxfly.entity.Token;
import fun.qxfly.entity.User;
import fun.qxfly.pojo.Result;
import fun.qxfly.vo.UserVO;

public interface UserInfoService {
    /**
     * 根据Token获取用户
     *
     * @param token
     * @return
     */
    User getUserInfoByToken(Token token);

    /**
     * 更改用户信息
     *
     * @param user
     * @return
     */
    boolean updateUserInfo(User user);

    /**
     * 检查用户名是否可行
     *
     * @param user
     * @return
     */
    User checkUsername(User user);

    /**
     * 头像上传
     *
     * @param file
     * @return
     */
    Result updateAvatar(MultipartFile file, Token token);

    /**
     * 刷新用户信息
     *
     * @param token
     * @return
     */
    boolean refreshUserInfoTask(Token token);

    /**
     * 刷新用户信息
     *
     * @return
     */
    Integer[] refreshUserInfoTask();

    /**
     * 获取推荐作者
     * @param currPage
     * @param pageSize
     * @return
     */
    PageInfo<UserVO> getSuggestAuthorByPage(Integer currPage, Integer pageSize);

    /**
     * 找回密码
     * @param phone
     * @param password
     * @param code
     * @return
     */
    Integer resetPassword(String phone, String password, Integer code);

    /**
     * 发送验证码
     * @param user
     * @return
     */
    int sendCode(User user);

    /**
     * 定时清理超时验证码
     * @return
     */
    Integer[] clearCaptchaTask();

    /**
     *  检测验证码
     * @param user
     * @return
     */
    int testCode(User user);
}
