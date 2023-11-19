package top.qxfly.service.User;

import top.qxfly.pojo.Token;
import top.qxfly.pojo.User;

public interface UserInfoService {
    /**
     * 根据Token获取用户
     *
     * @param token
     * @return
     */
    String getUserByToken(String token);
}
