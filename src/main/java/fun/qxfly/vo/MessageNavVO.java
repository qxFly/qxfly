package fun.qxfly.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageNavVO {
    @Schema(description = "用户id")
    private Integer id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "对话id")
    private String msgId;
    @Schema(description = "未读消息数")
    private Integer noReadCount;

}
