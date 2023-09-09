package top.qxfly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.LocalFile;
import top.qxfly.pojo.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class ListfileController {
    @Value("${file.path}")
    private String filePath;

    @Value("${download.path}")
    private String downloadPath;

    @PostMapping("/listfile")
    public Result listfile() {
        File path = new File(filePath);
        List<LocalFile> list = new ArrayList<>();
        String[] filename = path.list();
        if (filename != null) {
            for (String s : filename) {
                String filepath = downloadPath + s;
                LocalFile localFile = new LocalFile(s, filepath);
                log.info(String.valueOf(localFile));
                list.add(localFile);
            }
        }
        log.info("列出文件列表...");
        return Result.success(list);
    }
}
