package com.mtnz.service.system.product;

import com.mtnz.dao.DaoSupport;
import com.mtnz.entity.Page;
import com.mtnz.util.DateUtil;
import com.mtnz.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Created by xxj on 2018\5\29 0029.  
 */
@Service
@Resource(name = "kunCunService")
public class KunCunService {


    @Resource(name = "daoSupport")
    private DaoSupport daoSupport;

    public void save(PageData pd) throws Exception {
        daoSupport.save("KunCunMapper.save",pd);
    }

    public void batchSave(List<PageData> list,String store_id,String date,String status,String customer_id,String order_info_id,String jia) throws Exception {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("list",list);
        map.put("store_id",store_id);
        map.put("date",date);
        map.put("status",status);
        map.put("customer_id",customer_id);
        map.put("order_info_id",order_info_id);
        map.put("jia","0");
        daoSupport.save("KunCunMapper.batchSave",map);
    }

    public void batchSavess(List<PageData> list,String store_id,String date,String status,String customer_id,String order_info_id,String jia,String id) throws Exception {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("list",list);
        map.put("store_id",store_id);
        map.put("date",date);
        map.put("status",status);
        map.put("customer_id",customer_id);
        map.put("order_info_id",order_info_id);
        map.put("jia",jia);
        map.put("id",id);

        daoSupport.save("KunCunMapper.batchSavess",map);
    }

    public void batchSaves(List<PageData> list,String store_id,String date,String status,String supplier_id,String supplier_order_info_id,String id) throws Exception {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("list",list);
        map.put("store_id",store_id);
        map.put("date",date);
        map.put("status",status);
        map.put("supplier_id",supplier_id);
        map.put("supplier_order_info_id",supplier_order_info_id);
        map.put("id",id);
        daoSupport.save("KunCunMapper.batchSaves",map);
    }

    public List<PageData> datalistPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("KunCunMapper.datalistPage",page);
    }

    public PageData fundSumMoeny(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("KunCunMapper.fundSumMoeny",pd);
    }

    public List<PageData> findList(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("KunCunMapper.findList",pd);
    }

    public void editKuncun(PageData pd) throws Exception {
       daoSupport.findForList("KunCunMapper.editKuncun",pd);
    }

    public PageData findByproduct_id(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("KunCunMapper.findByproduct_id",pd);
    }

    public void editNum(PageData pd) throws Exception {
        daoSupport.update("KunCunMapper.editNum",pd);
    }

    public PageData findSum(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("KunCunMapper.findSum",pd);
    }

    public void editJiaNums(PageData pd) throws Exception {
        daoSupport.update("KunCunMapper.editJiaNums",pd);
    }

    public void editJianNumss(PageData pd) throws Exception {
        daoSupport.update("KunCunMapper.editJianNumss",pd);
    }

    public List<PageData> findSupplierList(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("KunCunMapper.findSupplierList",pd);
    }

    public PageData findSumNum(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("KunCunMapper.findSumNum",pd);
    }

    public void delete(PageData pd) throws Exception {
        daoSupport.delete("KunCunMapper.delete",pd);
    }

    public void editRevokes(PageData pd) throws Exception {
        daoSupport.update("KunCunMapper.editRevokes",pd);
    }

    public List<PageData> findReturnSupplierList(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("KunCunMapper.findReturnSupplierList",pd);
    }


    public List<PageData> findOrderInfoList(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("KunCunMapper.findOrderInfoList",pd);
    }

    public PageData findReturnSupplierProduct(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("KunCunMapper.findReturnSupplierProduct",pd);
    }

    public List<PageData> findCheXiao(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("KunCunMapper.findCheXiao",pd);
    }

    /**
     * 查询进货单
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> findListjin(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("KunCunMapper.findListjin",pd);
    }

    public void editJiahuiqu(PageData pd) throws Exception {

        daoSupport.update("KunCunMapper.editJiahuiqu",pd);
    }

    public void editNumli(PageData pd) throws Exception {
        daoSupport.update("KunCunMapper.editNumli",pd);
    }

    public void batchSavessli(List<PageData> list, String store_id, String date, String status, String customer_id, String order_info_id, String jia, String id) throws Exception {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("list",list);
        map.put("store_id",store_id);
        map.put("date",date);
        map.put("status",status);
        map.put("customer_id",customer_id);
        map.put("order_info_id",order_info_id);
        map.put("jia",jia);
        map.put("id",id);
        map.put("num","0");
        map.put("nums","0");
        daoSupport.save("KunCunMapper.batchSavessli",map);
    }

    public void batchSavesli(List<PageData> list,String store_id,String date,String status,String supplier_id,String supplier_order_info_id,String id) throws Exception {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("list",list);
        map.put("store_id",store_id);
        map.put("date",date);
        map.put("status",status);
        map.put("supplier_id",supplier_id);
        map.put("supplier_order_info_id",supplier_order_info_id);
        map.put("id",id);
        daoSupport.save("KunCunMapper.batchSavesli",map);
    }

    public void editJianNumssli(PageData pd) throws Exception {

        daoSupport.update("KunCunMapper.editJianNumssli",pd);
    }

    public void editNumlikucun(PageData pd) throws Exception {
        daoSupport.update("KunCunMapper.editNumlikucun",pd);
    }

    public void setNumsClear(PageData pd) throws Exception {
        daoSupport.update("KunCunMapper.setNumsClear",pd);
    }
}
