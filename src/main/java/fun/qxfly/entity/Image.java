package fun.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "图片实体类")
public class Image {
    @Schema(description = "id")
    private Integer id;
    @Schema(description = "名字")
    private String name;
    @Schema(description = "链接")
    private String url;
}
