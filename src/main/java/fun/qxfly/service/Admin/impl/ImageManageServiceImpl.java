package fun.qxfly.service.Admin.impl;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Image;
import fun.qxfly.mapper.Admin.ImageManageMapper;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Admin.ImageManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ImageManageServiceImpl implements ImageManageService {
    @Autowired
    private ImageManageMapper imageManageMapper;
    @Value("${file.articleImage.download.path}")
    private String articleImageDownloadPath;

    /**
     * 更新图库
     *
     * @return
     */
    @Override
    public Result updateImage() {
        HttpURLConnection con;
        try {
            URL reqUrl = new URL("https://api.github.com/repos/qxFly/qxfly-image/git/trees/main?recursive=1");
            con = (HttpURLConnection) reqUrl.openConnection();
            /*获取 GitHub Api*/
            String githubAPI = System.getProperty("GithubAPI");
            if (!githubAPI.isBlank()) {
                con.setRequestProperty("Authorization", "token " + githubAPI);
            }
            con.connect();
            /*判断连接是否成功*/
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
                int iflag = 0;
                for (int i = 2; i < tree.size(); i++) {
                    Map map1 = (Map) tree.get(i);
                    String a = (String) map1.get("path");
                    String name = a.split("/")[1];
                    /* 查询数据库是否有相同名字的图片 */
                    Integer imgId = imageManageMapper.getIdByName(name);
                    /* 没有则添加新图片 */
                    if (imgId == null) {
                        String url = "https://fastly.jsdelivr.net/gh/qxFly/qxfly-image/api/" + name;
                        int flag = imageManageMapper.addImage(name, url);
                        if (flag == 0) {
                            iflag++;
                            names.add(url);
                        }
                    }
                }
                return Result.success(String.valueOf(iflag), names);
            } else {
                return Result.error("名称列表为空！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("名称列表读取错误！");
        }
    }

    /**
     * 分页查询图库
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Image> getImagesByPage(int currPage, int pageSize, Integer aid, String originName, String createTimeStart, String createTimeEnd, Integer verify) {
        PageHelper.startPage(currPage, pageSize);
        List<Image> imageList = imageManageMapper.getImagesByPage(aid, originName, createTimeStart, createTimeEnd, verify);
        for (Image image : imageList) {
            image.setUrl(articleImageDownloadPath + image.getName());
        }
        return new PageInfo<>(imageList);
    }
}
