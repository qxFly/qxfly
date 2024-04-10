package fun.qxfly.service;

import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Site;

public interface SiteService {
    /**
     * 列出网站
     *
     * @return
     */
    PageInfo<Site> listSites(Integer currPage, Integer pageSize, String name);
}
