package fun.qxfly.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordDefinition {
    @Schema(description = "名词")
    String n;
    @Schema(description = "动词")
    String v;
    @Schema(description = "及物动词")
    String vt;
    @Schema(description = "不及物动词")
    String vi;
    @Schema(description = "形容词")
    String adj;
    @Schema(description = "副词")
    String adv;
    @Schema(description = "介系词")
    String prep;
    @Schema(description = "连词")
    String conj;
    @Schema(description = "短语")
    String phr;
    @Schema(description = "动词短语")
    String phrv;
}
