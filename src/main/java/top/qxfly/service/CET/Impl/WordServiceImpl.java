package top.qxfly.service.CET.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.qxfly.mapper.CET.WordMapper;
import top.qxfly.pojo.CET.Word;
import top.qxfly.service.CET.WordService;

import java.util.List;

@Slf4j
@Service
public class WordServiceImpl implements WordService {
    @Autowired
    private WordMapper wordMapper;

    /**
     * 添加单词
     *
     * @param word
     */
    @Override
    public void addWord(Word word) {
        wordMapper.addWord(word);
    }

    /**
     * 精确查找单词
     *
     * @param word
     * @return
     */
    @Override
    public Word findWord(Word word) {

        return wordMapper.findWord(word);
    }

    /**
     * 模糊查找单词
     *
     * @param word
     * @return
     */
    @Override
    public List<Word> findLikeWord(Word word) {
        return wordMapper.findLikeWord(word);
    }

    /**
     * 删除单词
     *
     * @param word
     */
    @Override
    public void deleteWord(Word word) {
        wordMapper.deleteWord(word);
    }

    /**
     * 列出所有单词
     *
     * @return
     */
    @Override
    public List<Word> listWords() {
        return wordMapper.listWords();
    }
}
