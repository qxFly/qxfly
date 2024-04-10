package fun.qxfly.controller;

import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Site;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.SiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/listSite")
    public Result listSite(@RequestParam Integer currPage, @RequestParam Integer pageSize, @RequestParam String name) {
        PageInfo<Site> pageInfo = siteService.listSites(currPage, pageSize, name);
        return Result.success(pageInfo);
    }
}
