package fun.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类实体类")
public class Classify {
    @Schema(description = "分类id")
    private Integer id;
    @Schema(description = "分类名称")
    private String name;
}
