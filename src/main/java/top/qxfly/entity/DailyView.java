package top.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "日浏览量实体类")
public class DailyView {
    @Schema(description = "文章id")
    private Integer articleId;
    @Schema(description = "浏览量")
    private Integer views;
}
