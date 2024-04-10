package fun.qxfly.service.Admin;

import fun.qxfly.entity.Site;

public interface SiteManegeService {
    /**
     * 删除站点
     * @param site
     * @return
     */
    boolean deleteSite(Site site);

    /**
     * 添加站点
     * @param site
     * @return
     */
    boolean addSite(Site site);

    /**
     * 修改站点
     * @param site
     * @return
     */
    boolean updateSite(Site site);
}
