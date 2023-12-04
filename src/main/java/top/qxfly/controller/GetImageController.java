package top.qxfly.controller;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@CrossOrigin
public class GetImageController {
    //    @GetMapping("/getimage")
//    public Result getImageController(){
//        String path = System.getProperty("user.dir");
//        List<String> nameList = new ArrayList<>();
//        String name;
//        try (BufferedReader bf = new BufferedReader(new FileReader(path+ "/imageName1.txt"))) {
//            while ((name = bf.readLine()) != null) {
//                nameList.add(name);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Random random = new Random();
//        String getName = nameList.get(random.nextInt(nameList.size()));
//        String url = "https://gcore.jsdelivr.net/gh/qxFly/qxfly-image/api/" + getName;
//        return Result.success(url);
//    }
    @GetMapping("/getimage")
    public Result getImageController() {
        HttpURLConnection con;
        try {
            URL reqUrl = new URL("https://api.github.com/repos/qxFly/qxfly-image/git/trees/main?recursive=1");
            con = (HttpURLConnection) reqUrl.openConnection();
            con.setRequestProperty("Authorization","token " + "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");
            con.connect();
            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                return Result.error("接口请求失败，" + responseCode + "错误！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("接口请求超时！");
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            if ((line = bufferedReader.readLine()) != null) {
                Map map = (Map) JSON.parse(line);
                List tree = (List) map.get("tree");
                List<String> names = new ArrayList<>();
                for (int i = 1; i < tree.size(); i++) {
                    Map map1 = (Map) tree.get(i);
                    String a = (String) map1.get("path");
                    names.add(a.split("/")[1]);
                }
                Random random = new Random();
                String resUrl = "https://gcore.jsdelivr.net/gh/qxFly/qxfly-image/api/" + names.get(random.nextInt(names.size()));
                return Result.success(resUrl);
            } else {
                return Result.error("名称列表为空！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("名称列表读取错误！");
        }

    }
}
