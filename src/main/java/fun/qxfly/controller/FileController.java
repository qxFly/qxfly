package fun.qxfly.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import fun.qxfly.entity.FilePO;
import fun.qxfly.entity.LocalFile;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.ChunkService;
import fun.qxfly.service.FileService;

import java.io.File;
import java.util.*;

@RestController
@CrossOrigin
@Slf4j
@Tag(name = "文件")
@Tag(name = "暂时废弃")
@Deprecated
public class FileController {

    @Value("${file.path}")
    private String filePath;

    @Value("${download.path}")
    private String downloadPath;
    private final FileService fileService;
    private final ChunkService chunkService;

    public FileController(FileService fileService, ChunkService chunkService) {
        this.fileService = fileService;
        this.chunkService = chunkService;
    }

    @Operation(description = "检查md5",summary = "检查md5")
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

    @Operation(description = "分片上传",summary = "分片上传")
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

    /**
     * 下载页文件列表
     * @return
     */
    @Operation(description = "下载页文件列表",summary = "下载页文件列表")
    @GetMapping("/fileList")
    public Result getFileList() {
        List<FilePO> fileList = fileService.selectFileList();
        return new Result(201, "文件列表查询成功", fileList);
    }

    /**
     * 左侧栏文件列表
     * @return
     */
    @Operation(description = "左侧栏文件列表",summary = "左侧栏文件列表")
    @PostMapping("/listFile")
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
        return Result.success(list);
    }

    /**
     * 删除文件
     *
     * @param filePO
     * @return
     */
    @Operation(description = "删除文件",summary = "删除文件")
    @PostMapping("/deleteFile")
    public Result deleteFile(@RequestBody FilePO filePO){
        String[] suffixes = filePO.getName().split("\\.");
        String fileName = filePO.getMd5() + "." + suffixes[suffixes.length -1];
        File file = new File(filePath + fileName);
        boolean fileDelete = file.delete();
        if(fileDelete){
            fileService.deleteFile(filePO.getMd5());
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}
