package fun.qxfly.mapper.Saying;

import fun.qxfly.entity.Word;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WordMapper {

    /**
     * 添加一个单词
     * @param word
     */
    @Insert("insert into word(word,definition,example) values(#{word},#{definition},#{example})")
    void addWord(Word w);

    /**
     * 获取一个单词
     * @return
     */
    @Select("select * from word where id >= (select floor(max(id) * rand()) from word) order by id limit 1")
    Word getSingleWord();
}
