package fun.qxfly.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.UploadService;

import java.io.IOException;

@Slf4j
@RestController
@Tag(name = "文件")
@Tag(name = "暂时废弃")
@Deprecated
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Operation(description = "暂无用处",summary = "上传文件")
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        if (uploadService.upload(file)) {
            return Result.success("上传成功");
        } else {
            return Result.error("未上传文件");
        }
    }
}
