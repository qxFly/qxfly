package top.qxfly.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.qxfly.pojo.ChunkPO;

import java.util.List;

@Mapper
public interface ChunkMapper {
    @Select("select * from chunk where c_md5 = #{md5}")
    List<ChunkPO> selectChunkListByMd5(String md5);

    @Insert("insert into chunk(c_md5,c_index)values(#{md5},#{index});")
    Integer insertChunk(ChunkPO chunkPO);

    @Delete("delete from chunk where c_md5 = #{md5}")
    void deleteChunkByMd5(String md5);
}
