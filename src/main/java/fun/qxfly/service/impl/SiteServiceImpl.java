package fun.qxfly.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fun.qxfly.entity.Site;
import fun.qxfly.mapper.SiteMapper;
import fun.qxfly.service.SiteService;

import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {
    @Autowired
    private SiteMapper siteMapper;


    /**
     * 列出网站
     *
     * @return
     */
    @Override
    public List<Site> listSites() {
        return siteMapper.listSites();
    }

    /**
     * 添加站点
     *
     * @param site
     * @return
     */
    @Override
    public boolean addSite(Site site) {
        return siteMapper.addSite(site);
    }

    /**
     * 根据id删除站点
     *
     * @param site
     * @return
     */
    @Override
    public boolean deleteSite(Site site) {

        return siteMapper.deleteSiteById(site);
    }
}
