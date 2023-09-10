package top.qxfly.mapper.CET;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.qxfly.pojo.CET.Word;

import java.util.List;

@Mapper
public interface WordMapper {
    /**
     * 添加单词
     *
     * @param word
     */
    @Insert("insert into words(word,type1,zhcn1,type2,zhcn2,type3,zhcn3,type4,zhcn4)value(#{word},#{type1},#{zhcn1},#{type2},#{zhcn2},#{type3},#{zhcn3},#{type4},#{zhcn4})")
    void addWord(Word word);

    /**
     * 删除单词
     *
     * @param word
     */
    @Delete("delete from words where word = #{word}")
    void deleteWord(Word word);

    /**
     * 列出所有单词
     *
     * @return
     */
    @Select("select * from words order by word ASC")
    List<Word> listWords();

    /**
     * 精确查找单词
     *
     * @param word
     * @return
     */
    @Select("select * from words where word = #{word}")
    Word findWord(Word word);

    /**
     * 模糊查找单词
     *
     * @param word
     * @return
     */
    @Select("select * from words where word like concat('%',#{word},'%')")
    List<Word> findLikeWord(Word word);
}
