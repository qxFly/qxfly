package fun.qxfly.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    /**
     * 文件上传
     */
    Boolean upload(MultipartFile file) throws IOException;
}
