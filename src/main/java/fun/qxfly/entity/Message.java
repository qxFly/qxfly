package fun.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "消息")
public class Message {
    @Schema(description = "id")
    private Integer id;

    @Schema(description = "发送方用户id")
    private Integer uid1;

    @Schema(description = "接收方用户id")
    private Integer uid2;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "是否已读")
    private Integer status;

    @Schema(description = "发送时间")
    private Date sendTime;

    @Schema(description = "对话唯一标识id")
    private String msgId;
}
