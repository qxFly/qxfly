package top.qxfly.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.qxfly.pojo.ChunkPO;

import java.util.List;


@Mapper
public interface ChunkMapper {
    List<ChunkPO> selectChunkListByMd5(String md5);

    Integer insertChunk(ChunkPO chunkPO);

    void deleteChunkByMd5(String md5);
}
