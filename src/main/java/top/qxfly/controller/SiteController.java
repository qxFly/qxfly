package top.qxfly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.qxfly.pojo.Result;
import top.qxfly.pojo.Site;
import top.qxfly.service.SiteService;

import java.util.List;

@Slf4j
@RestController
public class SiteController {
    @Autowired
    private SiteService siteService;

    @PostMapping("/listsite")
    public Result listSite() {
        List<Site> siteList = siteService.listSites();
        log.info("列出网站列表...");
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
}
