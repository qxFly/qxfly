package top.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCard {
    @Schema(description = "用户id")
    private Integer id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "简介")
    private String introduction;
    @Schema(description = "文章数")
    private String Articles;
    @Schema(description = "标签数")
    private String Tags;
    @Schema(description = "点赞数")
    private String Likes;

}
