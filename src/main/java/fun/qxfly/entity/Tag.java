package fun.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签实体类")
public class Tag {
    @Schema(description = "标签id")
    private Integer id;
    @Schema(description = "标签名称")
    private String name;
    @Schema(description = "用户id")
    private String uid;
    @Schema(description = "创建时间")
    private String createTime;

}
