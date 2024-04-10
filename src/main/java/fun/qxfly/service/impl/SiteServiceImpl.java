package fun.qxfly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.Site;
import fun.qxfly.mapper.SiteMapper;
import fun.qxfly.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
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
    public PageInfo<Site> listSites(Integer currPage, Integer pageSize, String name) {
        PageHelper.startPage(currPage, pageSize);
        List<Site> sites = siteMapper.listSites(name);
        // 检查网站是否可用
        HttpURLConnection httpURLConnection;
        for (Site site : sites) {
            try {
                URL url = new URL(site.getUrl());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                site.setStatus(httpURLConnection.getResponseCode());

            } catch (Exception e) {
                e.printStackTrace();
                site.setStatus(404);
            }
        }
        return new PageInfo<>(sites);
    }
}
