package top.qxfly.controller.CET;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.CET.Word;
import top.qxfly.pojo.Result;
import top.qxfly.service.CET.WordService;

import java.util.ArrayList;
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
                return Result.error("没有找到单词！！");
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

    /**
     * 精确搜索单词
     *
     * @param word
     * @return
     */
    @PostMapping("/searchWord")
    public Result searchWord(@RequestBody Word word) {
        log.info("word:{}", word.getWord());

        /* 判断是否为空 */
        if (word.getWord().equals("")) {
            return Result.error("请输入要搜索的单词！！");
        } else {
            String[] wordList = word.getWord().split(";");
            List<Word> w = new ArrayList<>();
            for (String s : wordList) {
                Word word1 = new Word(s.replace(" ", "")); // 去除多余空格
                if (wordService.findWord(word1) != null)
                    if (!w.contains(wordService.findWord(word1))) {
                        w.add(wordService.findWord(word1));
                    }
            }
            if (w.isEmpty()) {
                return Result.error("没有查找到单词！！");
            } else {
                System.out.println(w);
                return Result.success(w);
            }
        }

    }

    /**
     * 模糊搜索单词
     *
     * @param word
     * @return
     */
    @PostMapping("/likeSearchWord")
    public Result likeSearchWord(@RequestBody Word word) {
        log.info("word:{}", word.getWord());

        /* 判断是否为空 */
        if (word.getWord().equals("")) {
            return Result.error("请输入要搜索的单词！！");
        } else {
            Word word1 = new Word(word.getWord().replace(" ", "")); // 去除多余空格
            if (!wordService.findLikeWord(word1).isEmpty()) {
                return Result.success(wordService.findLikeWord(word1));
            }else {
                return Result.error("没有匹配到单词！！");
            }

        }

    }
}
