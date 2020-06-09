package com.mtnz.service.system.banner;

import com.mtnz.dao.DaoSupport;
import com.mtnz.entity.Page;
import com.mtnz.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/*
    Created by xxj on 2018\3\21 0021.
 */

@Service("bannerService")
public class BannerService {
    @Resource(name = "daoSupport")
    private DaoSupport daoSupport;

    public void save(PageData pd) throws Exception {
        daoSupport.save("BannerMapper.save",pd);
    }
    public void delete(Long id) throws Exception {
        daoSupport.save("BannerMapper.delete",id);
    }

    public List<PageData> findList(String s, PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("BannerMapper.findList",pd);
    }

    public PageData findById(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("BannerMapper.findById",pd);
    }

    public void updateBanner(PageData pd) throws Exception {
        daoSupport.update("BannerMapper.updateBanner",pd);
    }

    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("BannerMapper.datalistPage",page);
    }



}
