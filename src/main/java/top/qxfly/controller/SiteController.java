package top.qxfly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;
import top.qxfly.pojo.Site;
import top.qxfly.service.SiteService;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
public class SiteController {
    @Autowired
    private SiteService siteService;

    @PostMapping("/listsite")
    public Result listSite() {
        List<Site> siteList = siteService.listSites();
        return Result.success(siteList);
    }

    @PostMapping("/addsite")
    public Result addSite(@RequestBody Site site){
        String address = site.getAddress().toLowerCase();
        if (address.contains("https://") || address.contains("http://")){
            boolean flag = siteService.addSite(site);
            if (flag){
                return Result.success("添加成功");
            }else{
                return Result.error("添加失败");
            }
        }else{
            return Result.error("添加失败，请加上 https:// 或 http://");
        }
    }

    @PostMapping("/deletesite")
    public Result deleteSite(@RequestBody Site site){
        boolean flag =  siteService.deleteSite(site);
        if (flag){
            return Result.success("删除成功！");
        }else {
            return Result.error("删除失败");
        }

    }
}
