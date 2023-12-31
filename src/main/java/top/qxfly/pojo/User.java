package top.qxfly.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String role;
    private String username;
    private String password;
    private String email;
    private String phone;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
