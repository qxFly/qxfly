package top.qxfly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.qxfly.pojo.FilePO;
import top.qxfly.pojo.LocalFile;
import top.qxfly.pojo.Result;
import top.qxfly.service.ChunkService;
import top.qxfly.service.FileService;

import java.io.File;
import java.util.*;

@RestController
@CrossOrigin
@Slf4j
public class FileController {

    @Value("${file.path}")
    private String filePath;

    @Value("${download.path}")
    private String downloadPath;
    @Autowired
    private FileService fileService;
    @Autowired
    private ChunkService chunkService;


    @GetMapping("/check")
    public Result checkFile(@RequestParam("md5") String md5) {
        log.info("检查MD5:" + md5);
        //首先检查是否有完整的文件
        Boolean isUploaded = fileService.selectFileByMd5(md5);
        Map<String, Object> data = new HashMap<>();
        data.put("isUploaded", isUploaded);
        //如果有，就返回秒传
        if (isUploaded) {
            return new Result(201, "文件已经秒传", data);
        }

        //如果没有，就查找分片信息，并返回给前端
        List<Integer> chunkList = chunkService.selectChunkListByMd5(md5);
        data.put("chunkList", chunkList);

        return new Result(201, "", data);
    }

    @PostMapping("/upload/chunk")
    public Result uploadChunk(@RequestParam("chunk") MultipartFile chunk,
                              @RequestParam("md5") String md5,
                              @RequestParam("index") Integer index,
                              @RequestParam("chunkTotal") Integer chunkTotal,
                              @RequestParam("fileSize") Long fileSize,
                              @RequestParam("fileName") String fileName,
                              @RequestParam("chunkSize") Long chunkSize
    ) {


        String[] splits = fileName.split("\\.");
        String type = splits[splits.length - 1];
        String resultFileName = filePath + md5 + "." + type;

        chunkService.saveChunk(chunk, md5, index, chunkSize, resultFileName);
        log.info("上传分片：" + index + " ," + chunkTotal + "," + fileName + "," + resultFileName);
        if (Objects.equals(index, chunkTotal)) {
            FilePO filePO = new FilePO(fileName, md5, fileSize);
            fileService.addFile(filePO);
            chunkService.deleteChunkByMd5(md5);
            return new Result(200, "文件上传成功", index);
        } else {
            return new Result(201, "分片上传成功", index);
        }

    }

    //暂无用
    @GetMapping("/fileList")
    public Result getFileList() {
        log.info("查询文件列表");
        List<FilePO> fileList = fileService.selectFileList();
        log.info("fileList:{}", fileList.toString());
        return new Result(201, "文件列表查询成功", fileList);
    }

    @PostMapping("/listfile")
    public Result listfile() {
        File path = new File(filePath);
        List<LocalFile> list = new ArrayList<>();
        String[] filename = path.list();
        if (filename != null) {
            for (String s : filename) {
                String[] md5s = s.split("\\.");
                String realName = fileService.getFileName(md5s[0]);
                log.info("realName:{}",realName);
                String filepath = downloadPath + s;
                LocalFile localFile = new LocalFile(realName,s, filepath);
                log.info(String.valueOf(localFile));
                list.add(localFile);
            }
        }
        log.info("列出文件列表...");
        return Result.success(list);
    }

    /**
     * 删除文件
     *
     * @param filePO
     * @return
     */
    @PostMapping("/deleteFile")
    public Result deleteFile(@RequestBody FilePO filePO){
        log.info("filePO:{}",filePO);
        String[] suffixes = filePO.getName().split("\\.");
        String fileName = filePO.getMd5() + "." + suffixes[suffixes.length -1];
        log.info("fileName:{}",fileName);

        File file = new File(filePath + fileName);
        boolean fileDelete = file.delete();
        log.info(String.valueOf(file.delete()));
        if(fileDelete){
            fileService.deleteFile(filePO.getMd5());
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}
