package top.qxfly.service.CET;

import top.qxfly.pojo.CET.Word;

import java.util.List;

public interface WordService {
    /**
     * 添加单词
     *
     * @param word
     */
    void addWord(Word word);

    /**
     * 查找单词
     *
     * @param word
     * @return
     */
    Word findWord(Word word);

    /**
     * 删除单词
     *
     * @param word
     */
    void deleteWord(Word word);

    /**
     * 列出所有单词
     *
     * @return
     */
    List<Word> listWords();
}
