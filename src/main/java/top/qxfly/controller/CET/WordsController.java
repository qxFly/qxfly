package top.qxfly.controller.CET;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.CET.Word;
import top.qxfly.pojo.Result;
import top.qxfly.service.CET.WordService;

import java.util.List;


@Slf4j
@RestController
public class WordsController {
    @Autowired
    private WordService wordService;

    /**
     * 添加单词
     *
     * @param word
     * @return
     */
    @PostMapping("/addWord")
    public Result addWord(@RequestBody Word word) {
        log.info("word:{};type1:{};ZhCn1:{}", word.getWord(), word.getType1(), word.getZhcn1());

        if (word.getWord().equals("") || word.getType1().equals("") || word.getZhcn1().equals("")) {
            return Result.error("请输入必填项！！");
        } else {
            Word w = wordService.findWord(word);
            if (w == null) {
                wordService.addWord(word);
                return Result.success();
            } else {
                return Result.error("单词已经添加！！");
            }
        }
    }

    /**
     * 删除单词
     *
     * @param word
     * @return
     */
    @PostMapping("/deleteWord")
    public Result deleteWord(@RequestBody Word word) {
        log.info("word:{}", word.getWord());

        if (word.getWord().equals("")) {
            return Result.error("请输入要删除的单词！！");
        } else {
            Word w = wordService.findWord(word);
            if (w == null) {
                return Result.error("没有找到该单词！！");
            } else {
                wordService.deleteWord(word);
                return Result.error("单词删除成功！！");
            }
        }
    }

    /**
     * 列出所有单词
     *
     * @param
     * @return
     */
    @PostMapping("/listWord")
    public Result listWord() {
        List<Word> wordList = wordService.listWords();
            System.out.println(wordList);
        return Result.success(wordList);
    }

}
