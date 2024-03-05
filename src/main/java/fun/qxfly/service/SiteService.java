package fun.qxfly.service;

import fun.qxfly.entity.Site;

import java.util.List;

public interface SiteService {
    /**
     * 列出网站
     *
     * @return
     */
    List<Site> listSites();

    /**
     * 添加站点
     *
     * @param site
     * @return
     */
    boolean addSite(Site site);

    /**
     * 根据id删除站点
     *
     * @param site
     * @return
     */
    boolean deleteSite(Site site);
}
