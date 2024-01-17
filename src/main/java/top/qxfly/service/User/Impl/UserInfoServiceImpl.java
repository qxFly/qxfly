package top.qxfly.service.User.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.qxfly.entity.Token;
import top.qxfly.entity.User;
import top.qxfly.mapper.User.UserInfoMapper;
import top.qxfly.pojo.Result;
import top.qxfly.service.User.UserInfoService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

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
    public User checkUsername(User user) {
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
        if(user.getAvatar() != null){
            String[] split = user.getAvatar().split("/");
            String avatarName = split[split.length - 1];
            File avatarDelete = new File(path + "/" + avatarName);
            boolean delete = avatarDelete.delete();
        }
        String uuid = UUID.randomUUID().toString();
        String[] split1 = file.getOriginalFilename().split("\\.");
        String suffix = split1[split1.length - 1];
        String fileName = uuid + "." + suffix;
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
    public boolean refreshUserInfo(Token token) {
        Integer articleCount = userInfoMapper.getArticleCount(token);
        Integer LikeCount = userInfoMapper.getLike(token);
        if (articleCount == null){
            articleCount = 0;
        }
        if (LikeCount == null) {
            LikeCount = 0;
        }
        userInfoMapper.refreshUserInfo(articleCount,LikeCount,0,token.getToken());
        return false;
    }
}
