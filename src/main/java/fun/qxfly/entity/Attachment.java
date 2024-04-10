package fun.qxfly.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章附件实体类")
public class Attachment {
    Integer aid;
    Integer uid;
    String fileName;
    String FileOriginName;
    String downloadUrl;
}
