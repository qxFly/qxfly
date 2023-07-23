package top.qxfly.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.qxfly.mapper.ChunkMapper;
import top.qxfly.pojo.ChunkPO;
import top.qxfly.service.ChunkService;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkServiceImpl implements ChunkService {
    @Autowired
    private ChunkMapper chunkMapper;

    @Override
    public Integer saveChunk(MultipartFile chunk,String md5,Integer index,Long chunkSize,String resultFileName) {


        try (RandomAccessFile randomAccessFile = new RandomAccessFile(resultFileName, "rw")) {

            // 偏移量
            long offset = chunkSize * (index - 1);
            // 定位到该分片的偏移量
            randomAccessFile.seek(offset);
            // 写入
            randomAccessFile.write(chunk.getBytes());

            ChunkPO chunkPO = new ChunkPO(md5,index);
            return chunkMapper.insertChunk(chunkPO);
        } catch (IOException e) {
           e.printStackTrace();
           return 0;
        }



    }

    @Override
    public List<Integer> selectChunkListByMd5(String md5) {
        List<ChunkPO> chunkPOList = chunkMapper.selectChunkListByMd5(md5);
        List<Integer> indexList = new ArrayList<>();
        for (ChunkPO chunkPO : chunkPOList) {
            indexList.add(chunkPO.getIndex());
        }
        return indexList;
    }

    @Override
    public void deleteChunkByMd5(String md5) {
        chunkMapper.deleteChunkByMd5(md5);
    }

    @Override
    public byte[] getChunk(Integer index, Integer chunkSize, String resultFileName,long offset) {
        File resultFile = new File(resultFileName);
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(resultFileName, "r")) {
            // 定位到该分片的偏移量
            randomAccessFile.seek(offset);
            //读取
            byte[] buffer = new byte[chunkSize];
            randomAccessFile.read(buffer);
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }
}
