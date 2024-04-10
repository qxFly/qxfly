package fun.qxfly.controller.Saying;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
@CrossOrigin
@Tag(name = "一言")
@RequestMapping("/saying")
public class SayingController {

//    /**
//     * 一言
//     * @return
//     */
//    @GetMapping("/getWord")
//    @Operation(summary = "一言",description = "一言")
//    public String getWord() {
//        String[] words = {};
//        Random random = new Random();
//        int i = random.nextInt(0, words.length);
//        return words[i];
//    }
}
