package top.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户实体类")
public class User {
    @Schema(description = "用户id")
    private Integer id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "手机")
    private String phone;
    @Schema(description = "简介")
    private String introduction;
    @Schema(description = "地址")
    private String location;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "生日")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    @Schema(description = "角色")
    private String role;
    @Schema(description = "文章数")
    private String Articles;
    @Schema(description = "标签数")
    private String Tags;
    @Schema(description = "点赞数")
    private String Likes;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
