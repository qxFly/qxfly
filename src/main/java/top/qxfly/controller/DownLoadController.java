package top.qxfly.controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.qxfly.service.ChunkService;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Controller
@CrossOrigin
public class DownLoadController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${file.path}")
    private String filePath;

    @Autowired
    private ChunkService chunkService;

    @PostMapping("/download")
    public void download(@RequestParam("md5") String md5,
                         @RequestParam("fileName") String fileName,
                         @RequestParam("chunkSize") Integer chunkSize,
                         @RequestParam("chunkTotal") Integer chunkTotal,
                         @RequestParam("index") Integer index,
                         HttpServletResponse response) {
        String[] splits = fileName.split("\\.");
        String type = splits[splits.length - 1];
        String resultFileName = filePath + md5 + "." + type;

        File resultFile = new File(resultFileName);

        long offset = (long) chunkSize * (index - 1);
        if (Objects.equals(index, chunkTotal)) {
            offset = resultFile.length() - chunkSize;
        }
        byte[] chunk = chunkService.getChunk(index, chunkSize, resultFileName, offset);


        logger.info("下载文件分片" + resultFileName + "," + index + "," + chunkSize + "," + chunk.length + "," + offset);
//        response.addHeader("Access-Control-Allow-Origin","Content-Disposition");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Content-Length", "" + (chunk.length));
        response.setHeader("filename", fileName);


        response.setContentType("application/octet-stream");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            out.write(chunk);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
