package top.qxfly.service;

import top.qxfly.pojo.Jwt;

public interface LogoutService {
    /**
     * 退出登入
     *
     * @param token
     */
    void logout(Jwt token);

    /**
     * 删除token
     *
     * @param jwt
     */
    void deleteJwt(Jwt jwt);

    /**
     * 更新退出黑名单
     *
     * @param jwt
     */
    void updateJwtblack(Jwt jwt);
}
