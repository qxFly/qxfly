package fun.qxfly.controller.Saying;

import com.alibaba.fastjson2.JSONObject;
import fun.qxfly.entity.Word;
import fun.qxfly.pojo.Result;
import fun.qxfly.pojo.WordDefinition;
import fun.qxfly.service.Saying.WordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
@RestController
@CrossOrigin
@Tag(name = "一言")
@RequestMapping("/saying")
@Deprecated
public class WordController {
    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    /**
     * 获取单词
     *
     * @return
     */
    @GetMapping("/getWord")
    @Operation(summary = "获取单词", description = "获取单词")
    public String getWord(@RequestParam(defaultValue = "true") boolean single, @RequestParam(defaultValue = "true") boolean text) {
        Word word = new Word();
        if (single) {
            word = wordService.getSingleWord();
        }
        if (text) {
            StringBuilder wordText = new StringBuilder(word.getWord() + ": ");
            Class<WordDefinition> wordDefinitionClass = WordDefinition.class;
            WordDefinition wordDefinition = JSONObject.parseObject(word.getDefinition(), wordDefinitionClass);
            Method[] methods = wordDefinitionClass.getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.contains("get")) {
                    try {
                        String invoke = (String) wordDefinitionClass.getMethod(methodName).invoke(wordDefinition);
                        String[] field = methodName.split("get", 0);
                        if (invoke != null && !invoke.equals(""))
                            wordText.append(field[1].toLowerCase()).append(". ").append(invoke).append("; ");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "error";
                    }
                }
            }
            return wordText.toString();
        } else {
            return JSONObject.toJSONString(word);
        }

    }

    /**
     * 添加单词
     *
     * @return
     */
    @PostMapping("/addWord")
    @Operation(summary = "添加单词", description = "添加单词")
    public Result addWord(@RequestBody Word word) {
        String definition = word.getDefinition();
        if (definition != null && !definition.equals("")) {
            WordDefinition wordDefinition = JSONObject.parseObject(definition, WordDefinition.class);
            word.setDefinition(JSONObject.toJSONString(wordDefinition));
        }
        wordService.addWord(word);
        return Result.success();
    }
}
