package top.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章实体类")
public class Article {
    @Schema(description = "文章id")
    private Integer id;
    @Schema(description = "文章标题")
    private String title;
    @Schema(description = "文章内容")
    private String content;
    @Schema(description = "预览内容")
    private String preview;
    @Schema(description = "创建时间")
    private Date createTime;
    @Schema(description = "更新时间")
    private Date updateTime;
    @Schema(description = "标签")
    private String tag;
    @Schema(description = "赞")
    private Integer likes = 0;
    @Schema(description = "浏览量")
    private Integer views = 0;
    @Schema(description = "作者id")
    private Integer authorId;
    @Schema(description = "作者名称")
    private String author;
    @Schema(description = "封面")
    private String cover;
}
