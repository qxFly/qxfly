package top.qxfly.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.qxfly.service.UploadService;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class UploadServiceImpl implements UploadService {
    @Value("${file.path}")
    private String filePath;

    /**
     * 文件上传
     */
    @Override
    public Boolean upload(MultipartFile file) throws IOException {
        File path = new File(filePath);
//        File path = new File("G:\\qxfly");
        String realpath = path.getAbsolutePath() + "/";
        if (file != null) {
            String[] filename = path.list();
            if (filename != null) {
                for (String s : filename) {
                    log.info(s);
                }
            }
            /* 把文件从临时文件转存到指定目录 */
            file.transferTo(new File(realpath + file.getOriginalFilename()));
            return true;
        } else {
            return false;
        }
    }
}
