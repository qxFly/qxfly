package fun.qxfly.service.Admin.impl;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fun.qxfly.entity.Image;
import fun.qxfly.mapper.Admin.ImagePageMapper;
import fun.qxfly.pojo.PageBean;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Admin.ImagePageService;

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
public class ImagePageServiceImpl implements ImagePageService {
    @Autowired
    private ImagePageMapper imagePageMapper;

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
                    Integer imgId = imagePageMapper.getIdByName(name);
                    /* 没有则添加新图片 */
                    if (imgId == null) {
                        String url = "https://fastly.jsdelivr.net/gh/qxFly/qxfly-image/api/" + name;
                        int flag = imagePageMapper.addImage(name, url);
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

    /* 图库缓存，避免重复多次调用数据库 */
    List<Image> listCache = new ArrayList<>();//图库资源
    int ImagesCountCache = 0;//图库总数
    int cacheUsage = 0;//缓存使用次数

    @Override
    public PageBean<Image> getImagesByPage(int currPage, int pageSize) {
        /* 查看是否有缓存，如果缓存使用次数大于1000次更新图库 */
        if (listCache == null || listCache.isEmpty() || cacheUsage > 20) {
            /*获取所有记录数*/
            ImagesCountCache = imagePageMapper.getAllImagesCount();
            listCache = imagePageMapper.getAllImages();
            cacheUsage = 0;
        }
        /*创建分页信息*/
        PageBean<Image> imagePageBean = new PageBean<>(currPage, pageSize, ImagesCountCache);
        /*获取分页记录数*/
//        List<Image> pageImagesInfo = imagePageMapper.getAllImageInfo(imagePageBean.getStart(), pageSize);
//        imagePageBean.setData(pageImagesInfo);
        List<Image> imageList = new ArrayList<>();
        for (int i = imagePageBean.getStart(); i < imagePageBean.getEnd(); i++) {
            if (i < listCache.size()) {
                imageList.add(listCache.get(i));
            }
        }
        /*保存分页记数据*/
        imagePageBean.setData(imageList);
        cacheUsage++;
        return imagePageBean;
    }

    /**
     * 根据名字查找图片
     *
     * @param currPage
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public PageBean<Image> searchImagesByNameByPage(int currPage, int pageSize, String name) {
        /*获取所有记录数*/
        int count = imagePageMapper.getImagesCountByName(name);
        /*创建分页信息*/
        PageBean<Image> imagePageBean = new PageBean<>(currPage, pageSize, count);
        /*获取分页记录数*/
        List<Image> pageImagesInfo = imagePageMapper.searchImagesByName(imagePageBean.getStart(), pageSize, name);
        imagePageBean.setData(pageImagesInfo);
        return imagePageBean;
    }

    /**
     * 根据名字删除图片
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteImageByName(Integer id) {
        return imagePageMapper.deleteImageByName(id);
    }
}
