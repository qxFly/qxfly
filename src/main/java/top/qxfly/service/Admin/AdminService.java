package top.qxfly.service.Admin;

import top.qxfly.pojo.Token;

public interface AdminService {

    /**
     * 判断用户是否为管理员
     *
     * @param username
     * @return
     */
    boolean check(String username);


    /**
     * 根据 token 查找 用户
     *
     * @param token
     * @return
     */
    String getUserNameByToken(Token token);
}
