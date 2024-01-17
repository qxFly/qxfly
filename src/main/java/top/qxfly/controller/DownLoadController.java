package top.qxfly.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.qxfly.service.ChunkService;

import java.io.*;
import java.util.Objects;

@Controller
@CrossOrigin
@Slf4j
@Tag(name = "文件")
public class DownLoadController {
    @Value("${file.path}")
    private String filePath;

    private final ChunkService chunkService;

    public DownLoadController(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    /**
     * 下载页下载
     * @param md5
     * @param fileName
     * @param chunkSize
     * @param chunkTotal
     * @param index
     * @param response
     */
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
        log.info("resultFileName:{}",resultFileName);
        File resultFile = new File(resultFileName);

        long offset = (long) chunkSize * (index - 1);
        if (Objects.equals(index, chunkTotal)) {
            offset = resultFile.length() - chunkSize;
        }
        byte[] chunk = chunkService.getChunk(index, chunkSize, resultFileName, offset);


        log.info("下载文件分片" + resultFileName + "," + index + "," + chunkSize + "," + chunk.length + "," + offset);
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

    @PostMapping("/downloadtest")
    public void download(String fileName, HttpServletResponse response) {
        log.info("fileName；{}",fileName);
        try {
            // path是指欲下载的文件的路径。
            File file = new File(filePath + fileName);
            // 取得文件名。
            String filename = file.getName();
            log.info("filename；{}",filename);
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            log.info("filename；{}",filename);
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(filePath+ fileName));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            ServletOutputStream out = null;
            out = response.getOutputStream();
            response.setContentType("application/octet-stream");
            out.write(buffer);
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
