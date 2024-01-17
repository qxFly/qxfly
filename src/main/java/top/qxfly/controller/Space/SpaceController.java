package top.qxfly.controller.Space;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;

@Slf4j
@RestController
@RequestMapping("/space")
@Tag(name = "用户空间",description = "暂未实现")
public class SpaceController {
    @Operation(description = "暂未实现", summary = "暂未实现")
    @GetMapping("/getSpaceInfo")
    public Result getSpaceInfo(){

        return Result.success();
    }
}
