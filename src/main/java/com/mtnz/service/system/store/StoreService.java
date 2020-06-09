package com.mtnz.service.system.store;


import com.mtnz.controller.app.store.model.StoreLose;
import com.mtnz.controller.app.store.model.StoreUser;
import com.mtnz.dao.DaoSupport;
import com.mtnz.entity.Page;
import com.mtnz.sql.system.store.StoreLoseMapper;
import com.mtnz.sql.system.store.StoreMapper;
import com.mtnz.sql.system.store.StoreUserMapper;
import com.mtnz.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/*
    Created by xxj on 2018\3\21 0021.
 */

@Service("storeService")
public class StoreService {
    @Resource(name="daoSupport")
    private DaoSupport daoSupport;

    public void save(PageData pd) throws Exception {
        daoSupport.save("StoreMapper.save",pd);
    }

    public PageData findById(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("StoreMapper.findById",pd);
    }

    public List<PageData> datalistPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("StoreMapper.datalistPage",page);
    }

    public void updateName(PageData pd) throws Exception {
        daoSupport.update("StoreMapper.updateName",pd);
    }

    public void updateNumber(PageData pd) throws Exception {
        daoSupport.update("StoreMapper.updateNumber",pd);
    }

    public void updateJiaNumber(PageData pd) throws Exception {
        daoSupport.update("StoreMapper.updateJiaNumber",pd);
    }

    public void editQrCode(PageData pd) throws Exception {
        daoSupport.update("StoreMapper.editQrCode",pd);
    }

    public void editBusiness(PageData pd) throws Exception {
        daoSupport.update("StoreMapper.editBusiness",pd);
    }

    public List<PageData> findQuanBu(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("StoreMapper.findQuanBu",pd);
    }

    public void editPhoneTow(PageData pd) throws Exception {
        daoSupport.update("StoreMapper.editPhoneTow",pd);
    }


    public List<PageData> findstorListById(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("StoreMapper.findstorListById",pd);
    }

    public void saveStoreUser(PageData pd) throws Exception {
        daoSupport.save("StoreMapper.saveStoreUser",pd);
    }

    public void editCheckStore(PageData pd) throws Exception {
        daoSupport.update("StoreMapper.editCheckStore",pd);
    }

    public void editCheckStoreById(PageData pd) throws Exception {
        daoSupport.update("StoreMapper.editCheckStoreById",pd);
    }

    public void editStoreDetail(PageData pd) throws Exception {
        daoSupport.update("StoreMapper.editStoreDetail",pd);
    }

    public void editStatus(PageData pd) throws Exception {

        daoSupport.update("StoreMapper.editStatus",pd);
    }

    public PageData findStoreOneByUId(PageData pd) throws Exception {
       return (PageData) daoSupport.findForObject("StoreMapper.findStoreOneByUId",pd);
    }

    @Resource
    StoreUserMapper storeUserMapper;

    @Resource
    StoreMapper storeMapper;

    @Resource
    StoreLoseMapper storeLoseMapper;

    public List<StoreUser> select(StoreUser storeUser) {

        return  storeUserMapper.select(storeUser);
    }

    public Integer selectSumNumber(List<Long> idlist) {

        return storeMapper.selectSumNumber(idlist);
    }

    public void insert(StoreLose storeLose) {
        StoreLose bean = new StoreLose();
        bean.setStoreId(storeLose.getStoreId());
        if(storeLoseMapper.selectCount(bean)>0){
            StoreLose beans= storeLoseMapper.selectOne(bean);
            StoreLose storeLose1 = new StoreLose();
            storeLose1.setId(beans.getId());
            storeLose1.setStatus(beans.getStatus());
            storeLose1.setAutomatic(storeLose.getAutomatic());
            storeLoseMapper.updateByPrimaryKeySelective(storeLose1);
        }else {
            storeLoseMapper.insertSelective(storeLose);
        }
    }

    public StoreLose selectLose(StoreLose storeLose) {
        StoreLose bean = storeLoseMapper.selectOne(storeLose);
        if(bean==null){
            bean = new StoreLose();
            bean.setStatus(1);
            bean.setAutomatic(1);
            bean.setStoreId(storeLose.getStoreId());
        }
        return bean;
    }
}
