package com.mtnz.service.system.order_info;

import com.mtnz.controller.app.order.model.OrderInfo;
import com.mtnz.dao.DaoSupport;
import com.mtnz.entity.Page;
import com.mtnz.sql.system.order_info.OrderInfoMapper;
import com.mtnz.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/*
    Created by xxj on 2018\3\21 0021.
 */

@Service("orderInfoService")
public class OrderInfoService {

    @Resource(name = "daoSupport")
    private DaoSupport daoSupport;
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    public void updateOrderInfo(Long order_info_id){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderInfoId(order_info_id);
        OrderInfo info = orderInfoMapper.selectByPrimaryKey(orderInfo);
        if(info!=null){
            orderInfo.setOutboundStatus(1);
            orderInfoMapper.updateByPrimaryKey(orderInfo);
        }

    }

    public void save(PageData pd) throws Exception {
        daoSupport.save("OrderInfoMapper.save",pd);
    }

    public PageData findById(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("OrderInfoMapper.findById",pd);
    }

    public List<PageData> datalistPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.datalistPage",page);
    }

    public List<PageData> customerlistPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.customerlistPage",page);
    }

    public void updateOrderInfo(PageData pd) throws Exception {
        daoSupport.update("OrderInfoMapper.updateOrderInfo",pd);
    }

    public PageData findSumMoney(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("OrderInfoMapper.findSumMoney",pd);
    }

    public PageData findSumdiscountMoney(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("OrderInfoMapper.findSumdiscountMoney",pd);
    }

    public List<PageData> owelistPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.owelistPage",page);
    }

    public List<PageData> findLikeOrderInfo(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findLikeOrderInfo",pd);
    }

    public PageData findSumOweMoney(PageData pd) throws Exception {
       return (PageData) daoSupport.findForObject("OrderInfoMapper.findSumOweMoney",pd);
    }

    public List<PageData> RankinglistPage(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findkinglist",pd);
    }

    public PageData findkingcount(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("OrderInfoMapper.findkingcount",pd);
    }

    public List<PageData> AnalysislistPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.AnalysislistPage",page);
    }

    public PageData findSumAnalysisMoney(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("OrderInfoMapper.findSumAnalysisMoney",pd);
    }

    public List<PageData> findGroupCustomer(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findGroupCustomer",pd);
    }

    public PageData findStoreidSumMoney(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("OrderInfoMapper.findStoreidSumMoney",pd);
    }

    public void edutReturnGoods(PageData pd) throws Exception {
        daoSupport.update("OrderInfoMapper.edutReturnGoods",pd);
    }

    public PageData findOrderUserId(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("OrderInfoMapper.findOrderUserId",pd);
    }

    public List<PageData> findReportAnalysis(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findReportAnalysis",pd);
    }

    public List<PageData> findReportAnalysisYue(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findReportAnalysisYue",pd);
    }

    public List<PageData> findReportAnalysisXiaoShi(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findReportAnalysisXiaoShi",pd);
    }

    public void editrevokes(PageData pd) throws Exception {
        daoSupport.update("OrderInfoMapper.editrevokes",pd);
    }

    public PageData findSumTotalMoney(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("OrderInfoMapper.findSumTotalMoney",pd);
    }





    public List<PageData> findReportAnalysiss(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findReportAnalysiss",pd);
    }

    public List<PageData> findReportAnalysisXiaoShis(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findReportAnalysisXiaoShis",pd);
    }

    public List<PageData> findReportAnalysisYues(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findReportAnalysisYues",pd);
    }


    public List<PageData> findReportAnalysisWeb(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findReportAnalysisWeb",pd);
    }

    public List<PageData> findReportAnalysisYueWeb(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findReportAnalysisYueWeb",pd);
    }

    public List<PageData> AdminlistPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.AdminlistPage",page);
    }

    public List<PageData> findOwnOrder(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findOwnOrder",pd);
    }

    public List<PageData> findorderByOpenBill(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findorderByOpenBill",pd);
    }

    public List<PageData> findorderByOpenBillOnlyInfo(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderInfoMapper.findorderByOpenBillOnlyInfo",pd);
    }
}
