package top.qxfly.entity.CET;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "单词实体类")
public class Word {
    private String word;
    private String type1;
    private String zhcn1;

    private String type2;
    private String zhcn2;

    private String type3;
    private String zhcn3;

    private String type4;
    private String zhcn4;

    public Word(String s) {
        this.word = s;
    }
}
