package top.qxfly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
public class GetImageController {
    @GetMapping("/getimage")
    public Result GetImageController(){
//        String path = System.getProperty("user.dir");
//        String[] path = paths.split(";");
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println(path);
//        File file = new File(path);
        List<String> nameList = new ArrayList<>();
        String name;
        try (BufferedReader bf = new BufferedReader(new FileReader(path + "imageName.txt"))) {
            while ((name = bf.readLine()) != null) {
                nameList.add(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Random random = new Random();
        String getName = nameList.get(random.nextInt(nameList.size()));
        String url = "https://gcore.jsdelivr.net/gh/qxFly/qxfly-image/api/" + getName;
        return Result.success(url);
    }
}
