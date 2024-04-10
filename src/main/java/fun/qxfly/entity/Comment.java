package fun.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评论实体类")
public class Comment {
    @Schema(description = "评论id")
    private Integer id;
    @Schema(description = "文章id")
    private Integer articleId;
    @Schema(description = "评论")
    private String content;
    @Schema(description = "父评论id")
    private Integer parentCommentId;
    @Schema(description = "点赞")
    private Integer likeCount;
    @Schema(description = "评论时间")
    private Date createTime;
    @Schema(description = "评论筛选结束时间")
    private Date createTimeEnd;
    @Schema(description = "子评论")
    private List<Comment> childComment;
    @Schema(description = "被评论的用户id")
    private Integer toUserId;
    @Schema(description = "被评论的用户名")
    private String toUsername;
    @Schema(description = "审核")
    private Integer verify;
    @Schema(description = "评论的用户")
    private User user;
}
