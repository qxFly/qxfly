package fun.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户token")
public class Token {
    @Schema(description = "用户ID")
    private Integer id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "token")
    private String token;
    @Schema(description = "创建时间")
    private Integer time;

    public Token(String token) {
        this.token = token;
    }

    public Token(Integer id) {
        this.id = id;
    }
}
