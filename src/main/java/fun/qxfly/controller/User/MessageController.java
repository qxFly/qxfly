package fun.qxfly.controller.User;

import fun.qxfly.entity.Message;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.User.MessageService;
import fun.qxfly.utils.JwtUtils;
import fun.qxfly.vo.MessageNavVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 根据对话id获取对话
     *
     * @param msgId
     * @param size
     * @return
     */
    @Operation(description = "根据对话id获取对话", summary = "根据对话id获取对话")
    @GetMapping("/listMessage")
    public Result listMessage(@RequestParam String msgId, @RequestParam Integer size) {
        List<Message> listMessage = messageService.listMessage(msgId, size);
        return Result.success(listMessage);
    }

    /**
     * 初始化消息对话
     *
     * @param uid
     * @return
     */
    @Operation(description = "初始化消息对话", summary = "初始化消息对话")
    @GetMapping("/initUserMessage")
    public Result initUserMessage(@RequestParam Integer uid) {
        MessageNavVO message = messageService.initUserMessage(uid);
        return Result.success(message);
    }

    /**
     * 获取给当前用户发送的消息的用户
     *
     * @param uid
     * @param size
     * @return
     */
    @Operation(description = "获取给当前用户发送的消息的用户", summary = "获取给当前用户发送的消息的用户")
    @GetMapping("/listMessageUserNav")
    public Result listMessageUserNav(@RequestParam Integer uid, @RequestParam Integer size) {
        List<MessageNavVO> userList = messageService.listMessageUserNav(uid, size);
        return Result.success(userList);
    }

    /**
     * 发送消息
     *
     * @param
     * @return
     */
    @Operation(description = "发送消息", summary = "发送消息")
    @PostMapping("/sendMessage")
    public Result sendMessage(@RequestBody Message message) {
        String b = messageService.sendMessage(message);
        log.info("发送消息结果：{}", b);
        if (b != null && b.equals("error")) {
            return Result.error("发送失败");
        } else {
            if (b == null) return Result.success();
            else return Result.success(b);
        }
    }

    /**
     * 用户读未读消息
     *
     * @param
     * @return
     */
    @Operation(description = "用户读未读消息", summary = "用户读未读消息")
    @PostMapping("/readMessage")
    public Result readMessage(@RequestBody Message message) {
        boolean b = messageService.readMessage(message);
        if (b) {
            return Result.success();
        }
        return Result.error("未知错误");
    }

    /**
     * 根据用户id查看是否有未读消息
     *
     * @param request
     * @return
     */
    @Operation(description = "根据用户id查看是否有未读消息", summary = "根据用户id查看是否有未读消息")
    @GetMapping("/getNoReadMessageCount")
    public Result getNoReadMessageCount(HttpServletRequest request) {
        String token = request.getHeader("token");
        Integer userId;
        try {
            userId = (Integer) JwtUtils.parseJWT(token).get("userId");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取失败");
        }
        Integer f = messageService.getNoReadMessageCount(userId);
        return Result.success(f);
    }
}
