package fun.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户点赞收藏")
public class UserLikesAndCollection {
    @Schema(description = "用户Id")
    private Integer userId;
    @Schema(description = "喜欢的文章（通过json存储）")
    private String likeArticles;
    @Schema(description = "收藏的文章（通过json存储）")
    private String collectionArticles;
}
