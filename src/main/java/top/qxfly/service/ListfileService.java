package top.qxfly.service;

public interface ListfileService {
    /**
     * 根据md5获取文件真实名字
     *
     * @param s
     * @return
     */
    String getRealName(String s);
}
