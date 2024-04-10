package fun.qxfly.service.Saying.Impl;

import fun.qxfly.entity.Word;
import fun.qxfly.mapper.Saying.WordMapper;
import fun.qxfly.service.Saying.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WordServiceImpl implements WordService {

    private final WordMapper wordMapper;

    public WordServiceImpl(WordMapper wordMapper) {
        this.wordMapper = wordMapper;
    }

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
     * 获取一个单词
     * @return
     */
    @Override
    public Word getSingleWord() {
        return wordMapper.getSingleWord();
    }
}
