package top.qxfly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.qxfly.pojo.Result;
import top.qxfly.service.UploadService;

import java.io.IOException;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        if (uploadService.upload(file)) {
            return Result.success("上传成功");
        } else {
            return Result.error("未上传文件");
        }
    }
}
