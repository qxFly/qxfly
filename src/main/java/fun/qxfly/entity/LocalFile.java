package fun.qxfly.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalFile {
    private String fileName;
    private String fileMd5Name;
    private String filePath;


}
