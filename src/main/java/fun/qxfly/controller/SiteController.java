package fun.qxfly.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import fun.qxfly.entity.Site;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.SiteService;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@Tag(name = "右侧栏站点列表")
public class SiteController {
    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    @Operation(description = "列出站点", summary = "列出站点")
    @PostMapping("/listSite")
    public Result listSite() {
        List<Site> siteList = siteService.listSites();
        return Result.success(siteList);
    }

    @Operation(description = "添加站点", summary = "添加站点")
    @PostMapping("/addSite")
    public Result addSite(@RequestBody Site site) {
        String address = site.getAddress().toLowerCase();
        if (address.contains("https://") || address.contains("http://")) {
            return siteService.addSite(site) ? Result.success("添加成功") : Result.error("添加失败");
        } else {
            return Result.error("添加失败，请加上 https:// 或 http://");
        }
    }

    @Operation(description = "删除站点", summary = "删除站点")
    @PostMapping("/deleteSite")
    public Result deleteSite(@RequestBody Site site) {
        return siteService.deleteSite(site) ? Result.success("删除成功！") : Result.error("删除失败");
    }
}
