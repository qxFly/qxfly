package top.qxfly.service.User;

import org.springframework.web.multipart.MultipartFile;
import top.qxfly.entity.Token;
import top.qxfly.entity.User;
import top.qxfly.pojo.Result;

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
    boolean refreshUserInfo(Token token);
}
