package fun.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "单词实体类")
public class Word {
    @Schema(description = "id")
    private Integer id;
    @Schema(description = "单词")
    private String word;
    @Schema(description = "释义")
    private String definition;
    @Schema(description = "例子")
    private String example;
}
