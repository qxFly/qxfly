package top.qxfly.service.User.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.qxfly.entity.Token;
import top.qxfly.entity.User;
import top.qxfly.mapper.User.UserInfoMapper;
import top.qxfly.pojo.Result;
import top.qxfly.service.User.UserInfoService;
import top.qxfly.utils.AliyunDysmsapi;
import top.qxfly.vo.UserVO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    AliyunDysmsapi aliyunDysmsapi;

    /**
     * 根据Token获取用户
     *
     * @param token
     * @return
     */
    @Override
    public User getUserInfoByToken(Token token) {
        return userInfoMapper.getUserInfoByToken(token);
    }

    /**
     * 更改用户信息
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateUserInfo(User user) {
        return userInfoMapper.updateUserInfo(user);
    }

    /**
     * 检查用户名是否可行
     *
     * @param user
     * @return
     */
    @Override
    public User checkUsernameAndCode(User user) {
        return userInfoMapper.checkUsername(user);
    }

    /**
     * 头像上传
     *
     * @param file
     * @return
     */
    @Value("${file.userImg.download.path}")
    private String downloadPath;

    @Override
    public Result updateAvatar(MultipartFile file, Token token) {
        String path = System.getProperty("user.dir") + "/data/qxfly-userAvatar";
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        User user = userInfoMapper.getUserInfoByToken(token);
        /* 先删除原来的头像 */
        if (user.getAvatar() != null) {
            String[] split = user.getAvatar().split("/");
            String avatarName = split[split.length - 1];
            File avatarDelete = new File(path + "/" + avatarName);
            boolean delete = avatarDelete.delete();
        }
        String uuid = UUID.randomUUID().toString();
        String[] split1 = file.getOriginalFilename().split("\\.");
        String fileName = uuid + "." + "webp";
        try (OutputStream outputStream = new FileOutputStream(path + "/" + fileName)) {
            outputStream.write(file.getBytes());
            userInfoMapper.updateImg(downloadPath + fileName, user.getId());
            return Result.success(downloadPath + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }

    /**
     * 刷新用户信息
     *
     * @param token
     * @return
     */
    @Override
    public boolean refreshUserInfoTask(Token token) {
        Integer articleCount = userInfoMapper.getArticleCount(token);
        Integer LikeCount = userInfoMapper.getLikeCount(token);
        Integer Collection = userInfoMapper.getCollectionCount(token);
        Integer Views = userInfoMapper.getViewsCount(token);
        if (articleCount == null) {
            articleCount = 0;
        }
        if (LikeCount == null) {
            LikeCount = 0;
        }
        if (Collection == null) {
            Collection = 0;
        }
        if (Views == null) {
            Views = 0;
        }
        return userInfoMapper.refreshUserInfo(articleCount, LikeCount, Collection, Views, 0, token.getId());
    }

    /**
     * 定时刷新用户信息
     *
     * @return
     */
    @Override
    public Integer[] refreshUserInfoTask() {
        List<User> userList = userInfoMapper.listAllUser();
        Integer[] i = {userList.size(), 0};
        for (User user : userList) {
            if (refreshUserInfoTask(new Token(user.getId()))) {
                i[1]++;
            }
        }
        return i;
    }

    /**
     * 获取推荐作者
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<UserVO> getSuggestAuthorByPage(Integer currPage, Integer pageSize) {
        PageHelper.startPage(currPage, pageSize);
        List<UserVO> userList = userInfoMapper.getSuggestAuthor();
        return new PageInfo<>(userList);
    }

    /**
     * 发送验证码
     *
     * @param user
     * @return
     */
    @Override
    public int sendCode(User user) {
        int code = aliyunDysmsapi.sendCode(user.getPhone());
        return code == -1 ? -1 : userInfoMapper.captcha(user.getPhone(), code, new Date().getTime());
    }

    /**
     * 定时清理超时验证码
     *
     * @return
     */
    @Override
    public Integer[] clearCaptchaTask() {
        Integer i = userInfoMapper.clearCaptchaTask(new Date().getTime());
        return new Integer[0];
    }

    /**
     * 检测验证码
     * @param user
     * @return
     */
    @Override
    public int testCode(User user) {
        return aliyunDysmsapi.testCode(user.getPhone(), user.getRole());
    }

    /**
     * 找回密码
     *
     * @param phone
     * @param password
     * @param code
     * @return
     */
    @Override
    public Integer resetPassword(String phone, String password, Integer code) {
        int c = aliyunDysmsapi.testCode(phone, code);
        if (c == 0) {
            return 0;
        } else if (c == -1) {
            return -1;
        } else {
            return userInfoMapper.resetPassword(phone, password);
        }
    }


}
