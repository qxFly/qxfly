package top.qxfly.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
@Data
@Component
public class UserConfig {
    /*jwt令牌密匙*/
    private String JwtSignKey = "";
    /*jwt令牌失效时长*/
    private Long JwtTimeout = 2592000000L;
    /*GithubAPI*/
    private String GithubAPI = "";
    /*邀请码*/
    private String Invite = "";

    public void readUserConfig() {
        String userConfigPath = System.getProperty("user.dir") + "/qxfly-conf";
        File userConfigFile = new File(userConfigPath + "/config.json");
        boolean a;
        boolean b;
        if (!userConfigFile.exists()) {
            try {
                a = new File(userConfigPath).mkdirs();
                b = userConfigFile.createNewFile();
            } catch (Exception e) {
                a = false;
                b = false;
                e.printStackTrace();
            }
            try (OutputStream outputStream = new FileOutputStream(userConfigFile)) {
                if (b && a) {
                    UserConfig userConfig = new UserConfig();
                    byte[] bytes = JSONObject.toJSONBytes(userConfig, SerializerFeature.PrettyFormat);
                    outputStream.write(bytes);
                }
            } catch (Exception e) {
                a = false;
                b = false;
                e.printStackTrace();
            }
        } else {
            a = true;
            b = true;
        }
        if (b && a)
            try (InputStream inputStream = new FileInputStream(userConfigFile)) {
                /*读取配置文件*/
                byte[] bytes = inputStream.readAllBytes();
                String config = new String(bytes);
                /*把 json 格式转化为 UserConfig 对象*/
                JSONObject jsonObject = JSONObject.parseObject(config);
                UserConfig userConfig = jsonObject.toJavaObject(UserConfig.class);
                /*通过反射获取 UserConfig 对象的成员变量*/
                Class<? extends UserConfig> userConfigClass = userConfig.getClass();
                Field[] fields = userConfigClass.getDeclaredFields();
                for (Field field : fields) {
                    /*排除log对象*/
                    if (field.getName().equals("log")) continue;
                    /*通过反射获取 UserConfig 对象的成员变量的 get 方法*/
                    Method method = userConfigClass.getMethod("get" + field.getName());
                    /*通过反射获取成员变量的值*/
                    Object invoke = method.invoke(userConfig);
                    /*设置系统属性，以便其他类读取*/
                    System.setProperty(field.getName(), invoke.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


    }
}
