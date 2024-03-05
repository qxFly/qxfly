package fun.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "站点实体类")
public class Site {
    @Schema(description = "id")
    private Integer id;
    @Schema(description = "站点名字")
    private String name;
    @Schema(description = "站点地址")
    private String address;
}
