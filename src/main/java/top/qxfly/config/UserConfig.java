package top.qxfly.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

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
    /*数据库驱动*/
//    private String DatabaseDriver = "com.mysql.cj.jdbc.Driver";
//    /*数据库链接*/
//    private String DatabaseUrl = "jdbc:mysql://localhost:3306/qxfly";
//    /*数据库用户名*/
//    private String DatabaseUsername = "root";
//    /*数据库密码*/
//    private String DatabasePassword = "123456";

    @Bean
    public void writeConfig() {
        // 目录路径
        File dir = new File(System.getProperty("user.dir") + "/data/qxfly-conf");
        // 配置文件路径
        File userConfigFile = new File(dir + "/config.json");
        //目录不存在
        if (!userConfigFile.exists()) {
            log.warn("配置文件不存在...");
            try {
                log.warn("正在创建配置文件...");
                dir.mkdirs();
                userConfigFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("配置文件创建失败！");
            }
            try (OutputStream outputStream = new FileOutputStream(userConfigFile)) {
                log.warn("正在写出配置文件...");
                UserConfig userConfig = new UserConfig();
                byte[] bytes = JSONObject.toJSONBytes(userConfig, SerializerFeature.PrettyFormat);
                outputStream.write(bytes);
                log.warn("配置文件写出完成，请先填写配置文件");

            } catch (Exception e) {
                e.printStackTrace();
                log.error("配置文件写出失败！");
            }
        } else {
            readUserConfig(userConfigFile);
        }
    }

    public void readUserConfig(File userConfigFile) {
        try (InputStream inputStream = new FileInputStream(userConfigFile)) {
            log.warn("检测到配置文件，读取配置文件");
            /*读取配置文件*/
            byte[] bytes = inputStream.readAllBytes();
            String config = new String(bytes);
            /*把 json 格式转化为 UserConfig 对象*/
            JSONObject jsonObject = JSONObject.parseObject(config);
            UserConfig userConfig = jsonObject.toJavaObject(UserConfig.class);
            /*通过反射获取 UserConfig 对象的成员变量*/
            Class<? extends UserConfig> userConfigClass = userConfig.getClass();
            Field[] fields = userConfigClass.getDeclaredFields();
//            HashMap<String, String> db = new HashMap<>();
            for (Field field : fields) {
                String fieldName = field.getName();
                /*排除log对象*/
                if (fieldName.equals("log")) continue;
                /*通过反射获取 UserConfig 对象的成员变量的 get 方法*/
                Method method = userConfigClass.getMethod("get" + field.getName());
                /*通过反射获取成员变量的值*/
                String invoke = method.invoke(userConfig).toString();
                /*如果是包含数据库相关的*/
//                if (fieldName.contains("Database")) {
//                    log.info("包含Database，{}", fieldName);
//                    db.put(fieldName.replace("Database", "").toLowerCase(), invoke);
//                    continue;
//                }

                /*设置系统属性，以便其他类读取*/
                System.setProperty(fieldName, invoke);
            }
//            setApplication(db);
            log.warn("读取配置文件成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("读取配置文件失败!");
            log.warn("请检查文件格式 或 删除文件后重新启动生成配置文件！");
        }
    }

    public void setApplication(HashMap<String, String> db) {
        String[] datasource = {
                "spring.datasource.driver-class-name",
                "spring.datasource.url",
                "spring.datasource.username",
                "spring.datasource.password"
        };
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(new File("src/main/resources/application.properties"), "rw")) {
            StringBuilder str = new StringBuilder();
            String line;
            while ((line = randomAccessFile.readLine()) != null) {
                boolean flag = true;
                for (String d : datasource) {
                    if (line.contains(d)) {
                        flag = false;
                        db.forEach((key, value) -> {
                            if (d.contains(key)) {
                                str.append(d).append("=").append(value).append("\n");
                            }
                        });
                    }
                }
                if (flag) {
                    str.append(line).append("\n");
                }
            }
            randomAccessFile.setLength(0);
            randomAccessFile.write(str.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        File file = new File("src/main/resources/application.properties");
//        String content = "spring.datasource.password=123456";
//        if (file.exists()) {
//            log.info("存在{}", file.getName());
//            try (OutputStream outputStream = new FileOutputStream(file,false);
//                 InputStream inputStream = new FileInputStream(file);
//                 RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")
//            ) {
//                String line;
////                for (; ; ) {
////                    if ((line = randomAccessFile.readLine()) != null) {
////                        if (line.contains("spring.datasource.password")) {
////                            log.info("存在spring.datasource.password");
////                            int length = line.length();
////                            byte[] bytes = inputStream.readAllBytes();
////                            String application = new String(bytes);
////                            int index = application.indexOf("spring.datasource.password=");
////                            log.info("位于：{}", index);
////                            randomAccessFile.seek(index);
////                            for (int j = 0; j < length; j++) {
////                                blank.append(" ");
////                            }
////                            randomAccessFile.write(blank.toString().getBytes());
////                            randomAccessFile.seek(index);
////                            randomAccessFile.write(content.getBytes());
////                        }
////                    }
////                }
//                line = randomAccessFile.readLine();
//                log.info("line:{}",line);
////                StringBuilder str = new StringBuilder();
////                for (; ; ) {
////                    if ((line = randomAccessFile.readLine()) != null) {
////                        log.info("awda{}", line);
////                        if (!line.contains("spring.datasource.password")) {
////                            str.append(line);
////                        } else {
////                            str.append(content);
////                        }
////                    } else {
////                        break;
////                    }
////                }
////                randomAccessFile.writeBytes(str.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            log.info("不存在");
//        }
    }

}
