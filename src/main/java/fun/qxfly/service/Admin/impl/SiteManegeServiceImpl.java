package fun.qxfly.service.Admin.impl;

import fun.qxfly.entity.Site;
import fun.qxfly.mapper.Admin.SiteManegeMapper;
import fun.qxfly.service.Admin.SiteManegeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SiteManegeServiceImpl implements SiteManegeService {

    private final SiteManegeMapper siteManegeMapper;

    public SiteManegeServiceImpl(SiteManegeMapper siteManegeMapper) {
        this.siteManegeMapper = siteManegeMapper;
    }

    /**
     * 删除站点
     * @param site
     * @return
     */
    @Override
    public boolean deleteSite(Site site) {
        return siteManegeMapper.deleteSite(site);
    }

    /**
     * 添加站点
     * @param site
     * @return
     */
    @Override
    public boolean addSite(Site site) {
        return siteManegeMapper.addSite(site);
    }

    /**
     * 更新站点
     * @param site
     * @return
     */
    @Override
    public boolean updateSite(Site site) {
        return siteManegeMapper.updateSite(site);
    }
}
