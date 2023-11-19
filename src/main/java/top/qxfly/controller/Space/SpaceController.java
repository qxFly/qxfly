package top.qxfly.controller.Space;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;

@Slf4j
@RestController
@RequestMapping("/space")
public class SpaceController {
    @GetMapping("/getSpaceInfo")
    public Result getSpaceInfo(){

        return Result.success();
    }
}
