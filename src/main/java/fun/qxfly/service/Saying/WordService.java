package fun.qxfly.service.Saying;

import fun.qxfly.entity.Word;

public interface WordService {
    /**
     * 添加单词
     * @param word
     */
    void addWord(Word word);

    /**
     * 获取单个单词
     * @return
     */
    Word getSingleWord();
}
