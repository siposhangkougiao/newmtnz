package com.mtnz.controller.app.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.mtnz.controller.app.product.model.Product;
import com.mtnz.controller.app.product.model.ProductVo;
import com.mtnz.controller.base.BaseController;
import com.mtnz.controller.base.Result;
import com.mtnz.controller.base.ServiceException;
import com.mtnz.entity.Page;
import com.mtnz.service.system.order_info.OrderInfoService;
import com.mtnz.service.system.order_pro.OrderProService;
import com.mtnz.service.system.product.KunCunService;
import com.mtnz.service.system.product.ProductImgService;
import com.mtnz.service.system.product.ProductService;
import com.mtnz.service.system.product.WeProductService;
import com.mtnz.sql.system.product.ProductNewMapper;
import com.mtnz.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/*
    Created by xxj on 2018\3\22 0022.  
 */
@Controller
@RequestMapping(value = "app/productNew",produces = "text/html;charset=UTF-8")
public class AppProductNewController extends BaseController{

    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "productImgService")
    private ProductImgService productImgService;
    @Resource(name = "weProductService")
    private WeProductService weProductService;
    @Resource(name = "orderProService")
    private OrderProService orderProService;
    @Resource(name = "orderInfoService")
    private OrderInfoService orderInfoService;
    @Resource(name = "kunCunService")
    private KunCunService kunCunService;
    @Autowired
    private ProductNewMapper productNewMapper;


    @RequestMapping(value = "findProductFeiXis",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findProductFeiXis(String store_id,String status,String product_id,
                                    String startTime,String endTime,String product_name,
                                    Integer pageNumber,Integer pageSize,Integer type,String typeName){
        logBefore(logger,"查询详情");
        PageData pd=this.getPageData();
        pageSize = 5;
        if(pageNumber!=null&&pageSize!=null){
            pd.put("pageNumber",(pageNumber-1)*pageSize);
            pd.put("pageSize",pageSize);
        }else {
            pageNumber = getPageNumber();
            pageSize = getPageSize();
            pd.put("pageNumber",0);
            pd.put("pageSize",pageSize);
        }
        try{
            if("0".equals(status)){
                pd.put("startTime","");
                pd.put("endTime",DateUtil.getDay());
            }else if("1".equals(status)){
                pd.put("startTime",DateUtil.getFirstDayOfMonth(Integer.valueOf(DateUtil.getYear()),Integer.valueOf(DateUtil.getDay().split("-")[1])));
                pd.put("endTime",DateUtil.getDay());
            }else if("2".equals(status)){
                pd.put("startTime",DateUtil.getFirstDayOfMonth(Integer.valueOf(DateUtil.getYear()),1));
                pd.put("endTime",DateUtil.getDay());
            }else if("3".equals(status)){
                pd.put("startTime",startTime);
                pd.put("endTime",endTime);
            }else if("4".equals(status)){
                pd.put("startTime",DateUtil.getDay());
                pd.put("endTime",DateUtil.getDay());
            }
            List<PageData> list=productService.findProductFeiXis(pd);
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("data",list);
            pd.put("pageTotal",list.size());
            pd.put("pageNumber",pageNumber);
            pd.put("pageSize",pageSize);
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * @param store_id 店ID
     * @param status 0 全部 1本月 2本年 3其他 4本日
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping(value = "findWholeProductProfitss",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findWholeProductProfitss(String store_id,String status,String product_id,String startTime,String endTime){
        logBefore(logger,"查询商品利润111");
        PageData pd=this.getPageData();
        try{
            if("0".equals(status)){
                pd.put("status","");
                pd.put("startTime","");
                pd.put("endTime",DateUtil.getDay());
            }else if("1".equals(status)){
                pd.put("status","3");
                pd.put("startTime",DateUtil.getFirstDayOfMonth(Integer.valueOf(DateUtil.getYear()),Integer.valueOf(DateUtil.getDay().split("-")[1])));
                pd.put("endTime",DateUtil.getDay());
            }else if("2".equals(status)){
                pd.put("status","3");
                pd.put("startTime",DateUtil.getFirstDayOfMonth(Integer.valueOf(DateUtil.getYear()),1));
                pd.put("endTime",DateUtil.getDay());
            }else if("3".equals(status)){
                pd.put("status","3");
                pd.put("startTime",startTime);
                pd.put("endTime",endTime);
            }else if("4".equals(status)){
                pd.put("status","3");
                pd.put("startTime",DateUtil.getDay());
                pd.put("endTime",DateUtil.getDay());
            }
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.0");
            List<PageData> list=new ArrayList<>();
            if("1".equals(status)||"3".equals(status)){
                list=orderInfoService.findReportAnalysiss(pd);
            }else if("4".equals(status)){
                list=orderInfoService.findReportAnalysisXiaoShis(pd);
            }else{
                list=orderInfoService.findReportAnalysisYues(pd);
            }
            String profit="0.0";
            String receivable="0.0";
            PageData pd_t=productService.findSumProductMoney(pd);
            if(pd_t!=null){
                if(pd_t.get("receivable")!=null&&!pd_t.get("receivable").toString().equals("0.0")){
                    receivable=df.format(pd_t.get("receivable"));
                }
                if(pd_t.get("profit")!=null&&!pd_t.get("profit").toString().equals("0.0")){
                    profit=df.format(pd_t.get("profit"));
                }
            }
            String money="0.0";
            pd.put("return_goods","0");
            PageData pd_money=orderInfoService.findStoreidSumMoney(pd);
            if(pd_money!=null){
                money=pd_money.get("money").toString();
            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("data",list);
            pd.put("total_profit",profit);
            pd.put("total_receivable",receivable);
            pd.put("status",status);
            pd.put("pageTotal","1");
            pd.put("money",money);
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     *
     * @param store_id 店ID
     * @param status 0 全部 1本月 2本年 3其他 4本日
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping(value = "findWholeProductProfits",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findWholeProductProfits(String store_id,String status,String product_id,String startTime,String endTime){
        logBefore(logger,"查询商品利润");
        PageData pd=this.getPageData();
        try{
            if("0".equals(status)){
                pd.put("startTime","");
                pd.put("endTime",DateUtil.getDay());
            }else if("1".equals(status)){
                pd.put("startTime",DateUtil.getFirstDayOfMonth(Integer.valueOf(DateUtil.getYear()),Integer.valueOf(DateUtil.getDay().split("-")[1])));
                pd.put("endTime",DateUtil.getDay());
            }else if("2".equals(status)){
                pd.put("startTime",DateUtil.getFirstDayOfMonth(Integer.valueOf(DateUtil.getYear()),1));
                pd.put("endTime",DateUtil.getDay());
            }else if("3".equals(status)){
                pd.put("startTime",startTime);
                pd.put("endTime",endTime);
            }else if("4".equals(status)){
                pd.put("startTime",DateUtil.getDay());
                pd.put("endTime",DateUtil.getDay());
            }
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.0");
            List<PageData> list=new ArrayList<>();
            if("1".equals(status)){
                list=productService.findReportAnalysis(pd);
            }else if("4".equals(startTime)){
                list=productService.findReportAnalysisXiaoShi(pd);
            }else{
                list=productService.findReportAnalysisYue(pd);
            }
            String profit="0.0";
            String receivable="0.0";
            PageData pd_t=productService.findSumProductMoney(pd);
            if(pd_t!=null){
                if(pd_t.get("receivable")!=null&&!pd_t.get("receivable").toString().equals("0.0")){
                    receivable=df.format(pd_t.get("receivable"));
                }
                if(pd_t.get("profit")!=null&&!pd_t.get("profit").toString().equals("0.0")){
                    profit=df.format(pd_t.get("profit"));
                }
            }
            String money="0.0";
            pd.put("return_goods","0");
            pd.put("status","3");
            PageData pd_money=orderInfoService.findStoreidSumMoney(pd);
            if(pd_money!=null){
                money=pd_money.get("money").toString();
            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("data",list);
            pd.put("total_profit",profit);
            pd.put("total_receivable",receivable);
            pd.put("status",status);
            pd.put("pageTotal","1");
            pd.put("money",money);
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * @param store_id 店ID
     * @param product_name 模糊查询商品名
     * @param startTime 开始时间 (开始为空)
     * @param endTime 结束时间(开始为空)
     * @return
     */
    @RequestMapping(value = "findWholeProductProfit",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findWholeProductProfit(String store_id,String product_name,String startTime,String endTime,String type){
        logBefore(logger,"查询商品利润1");
        PageData pd=this.getPageData();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String status="0";
        if(store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","1");
            pd.put("message","缺少参数!");
        }else{
            try{
                java.text.DecimalFormat df = new java.text.DecimalFormat("########.0");
                if(startTime==null||startTime.length()==0||endTime==null||endTime.length()==0){
                    Calendar cal_1=Calendar.getInstance();//获取当前日期
                    cal_1.add(Calendar.MONTH, -1);
                    String firstDay = format.format(cal_1.getTime());
                    pd.put("startTime",firstDay);
                    pd.put("endTime",DateUtil.getDay());
                    pd.put("status","0");
                }
                List<PageData> list=productService.findProductFenXi(pd);
                double profit=0.0;
                double receivable=0.0;
                if(list!=null&&list.size()!=0){
                    profit=list.stream().mapToDouble(PageData::getProfit).sum();
                    receivable=list.stream().mapToDouble(PageData::getReceivable).sum();
                }
                String money="0.0";
                pd.put("return_goods","0");
                pd.put("status","3");
                PageData pd_money=orderInfoService.findStoreidSumMoney(pd);
                if(pd_money!=null){
                    money=pd_money.get("money").toString();
                }
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("data",list);
                if(profit!=0.0){
                    pd.put("total_profit",df.format(profit));
                }else{
                    pd.put("total_profit",profit);
                }
                if(receivable!=0.0){
                    pd.put("total_receivable",df.format(receivable));
                }else{
                    pd.put("total_receivable",receivable);
                }
                pd.put("status",status);
                pd.put("pageTotal","1");
                pd.put("money",money);
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }




    /**
     *
     * @param store_id 店ID
     * @param product_name 模糊查询商品名
     * @param startTime 开始时间 (开始为空)
     * @param endTime 结束时间(开始为空)
     * @return
     */
    @RequestMapping(value = "findProductProfitList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findProductProfitList(String store_id,String product_name,String startTime,String endTime,String type){
        logBefore(logger,"查询商品利润");
        PageData pd=this.getPageData();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String status="0";
        if(store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","1");
            pd.put("message","缺少参数!");
        }else{
            try{
                /*List<PageData> lists=productService.findProductProfitList(pd);
                if(lists!=null&& lists.size()!=0){
                    status="1";
                }*/
                pd.put("status","3");
                java.text.DecimalFormat df = new java.text.DecimalFormat("########.0");
                if(startTime==null||startTime.length()==0||endTime==null||endTime.length()==0){
                    Calendar cal_1=Calendar.getInstance();//获取当前日期
                    cal_1.add(Calendar.MONTH, -1);
                    String firstDay = format.format(cal_1.getTime());
                    pd.put("startTime",firstDay);
                    pd.put("endTime",DateUtil.getDay());
                    pd.put("status","0");
                }
                List<PageData> list=productService.findProductProfit(pd);
                double profit=0.0;
                double receivable=0.0;
                if(list!=null&&list.size()!=0){
                    profit=list.stream().mapToDouble(PageData::getProfit).sum();
                    receivable=list.stream().mapToDouble(PageData::getReceivable).sum();
                }
                String money="0.0";
                pd.put("return_goods","0");
                PageData pd_money=orderInfoService.findStoreidSumMoney(pd);
                if(pd_money!=null){
                    money=pd_money.get("money").toString();
                }
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("data",list);
                if(profit!=0.0){
                    pd.put("total_profit",df.format(profit));
                }else{
                    pd.put("total_profit",profit);
                }
                if(receivable!=0.0){
                    pd.put("total_receivable",df.format(receivable));
                }else{
                    pd.put("total_receivable",receivable);
                }
                pd.put("status",status);
                pd.put("pageTotal","1");
                pd.put("money",money);
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     *
     * @param product_id 商品ID
     * @param pageNum 页码
     * @param name 名字
     * @return
     */
    @RequestMapping(value = "ProductManagements",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ProductManagements(String product_id,String pageNum,String name){
        logBefore(logger,"商品分析");
        PageData pd=this.getPageData();
        if(product_id==null||product_id.length()==0){
            pd.clear();
            pd.put("code","1");
            pd.put("message","缺少参数!");
        }else{
            try{
                if(pageNum==null||pageNum.length()==0){
                    pageNum="1";
                }
                Integer SHU1 = Integer.valueOf(pageNum) * 10;
                pd.put("SHU1", SHU1 - 10);
                List<PageData> list=orderProService.findProductPro(pd);
                PageData pd_c=orderProService.findProductProCount(pd);
                Integer pageTotal;
                if (Integer.valueOf(pd_c.get("count").toString()) % 10 == 0){
                    pageTotal =Integer.valueOf(pd_c.get("count").toString()) / 10;
                }else {
                    pageTotal = Integer.valueOf(pd_c.get("count").toString()) / 10 + 1;
                }
                Map<String, Object> map = new HashedMap();
                map.put("data",list);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message","正确返回数据");
                pd.put("pageTotal",pageTotal.toString());
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * @param product_id 商品ID
     * @param pageNum 页码
     * @param name 名字
     * @return
     */
    @RequestMapping(value = "ProductManagement",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ProductManagement(String product_id,String pageNum,String name){
        logBefore(logger,"商品分析");
        PageData pd=this.getPageData();
        if(product_id==null||product_id.length()==0){
            pd.clear();
            pd.put("code","1");
            pd.put("message","缺少参数!");
        }else{
            try{
                if(pageNum==null||pageNum.length()==0){
                    pageNum="1";
                }
                Integer SHU1 = Integer.valueOf(pageNum) * 10;
                pd.put("SHU1", SHU1 - 10);
                List<PageData> list=orderProService.findProductPro(pd);
                PageData pd_c=orderProService.findProductProCount(pd);
                Integer pageTotal;
                if (Integer.valueOf(pd_c.get("count").toString()) % 10 == 0){
                    pageTotal =Integer.valueOf(pd_c.get("count").toString()) / 10;
                }else{
                    pageTotal = Integer.valueOf(pd_c.get("count").toString()) / 10 + 1;
                }
                Map<String, Object> map = new HashedMap();
                map.put("object",list);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message","正确返回数据");
                pd.put("pageTotal",pageTotal.toString());
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     *
     * @param product_id 商品ID
     * @return
     */
    @RequestMapping(value = "findProductProfit",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findProductProfit(String product_id){
        logBefore(logger,"查询商品利润");
        PageData pd=this.getPageData();
        String num="0";
        if(product_id==null||product_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try{
                PageData pd_p=productService.findById(pd);
                PageData pd_c=orderProService.findProNum(pd);
                if(pd_c!=null){
                    num=pd_c.get("num").toString();
                }
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("product_price",pd_p.getString("product_price"));
                pd.put("purchase_price",pd_p.getString("purchase_price"));
                pd.put("num",num);
                pd.put("name",pd_p.getString("product_name"));
                pd.put("kucun",pd_p.get("kucun").toString());
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * @param name 模糊查询
     * @return
     */
    @RequestMapping(value = "findWProductList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findWProductList(String name){
        logBefore(logger,"查询我们自己的商品");
        PageData pd=new PageData();
        try{
            List<PageData> list=weProductService.findList(pd);
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("data",list);
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!!");
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 查询商品
     * @param product_id 商品ID
     * @return
     */
    @RequestMapping(value = "findProductId",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findProductId(String product_id){
        logBefore(logger,"查询商品详情");
        PageData pd=this.getPageData();
        Page page=new Page();
        if(product_id==null||product_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                PageData pd_p=productService.findById(pd);
                List<PageData> list_img=productImgService.findList(pd_p);
                pd_p.put("img",list_img);
                Map<String, Object> map = new HashedMap();
                map.put("data",pd_p);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 查询商品
     * @param store_id 店铺ID
     * @param product_name 商品名称
     * @return
     */
    @RequestMapping(value = "findlikeProduct",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findlikeProduct(String store_id,String product_name,String type,Integer pageNumber,Integer pageSize){
        logBefore(logger,"模糊查询商品");
        PageData pd=this.getPageData();
        if(store_id==null||store_id.length()==0||product_name==null||product_name.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                String message="正确返回数据!";
                if(pageNumber==null||pageSize==null){
                    pageNumber =1;
                    pageSize =15;
                }
                PageHelper.startPage(pageNumber,pageSize);
                List<PageData> list=productService.findlikeList(pd);
                for (int i=0,len=list.size();i<len;i++){
                    List<PageData> list_img=productImgService.findList(list.get(i));
                    list.get(i).put("img",list_img);
                }
                //开始计算总进货价
                BigDecimal priceaa = new BigDecimal(0);
                for (int i = 0; i <list.size() ; i++) {
                    //if(Integer.valueOf(String.valueOf(list.get(i).get("kucun")))>0){
                    if(new BigDecimal(String.valueOf(list.get(i).get("kucun"))).compareTo(new BigDecimal(0))>0){
                        PageData kucun= new PageData();
                        kucun.put("product_id",list.get(i).get("product_id"));
                        kucun.put("store_id",list.get(i).get("store_id"));
                        List<PageData> pricelist = productService.findProductPrice(kucun);
                        for (int j = 0; j <pricelist.size() ; j++) {
                            //System.out.println("數量："+pricelist.get(j).get("nums")+"单价是："+pricelist.get(j).get("purchase_price"));
                            priceaa = priceaa.add(new BigDecimal(pricelist.get(j).get("nums").toString()).multiply(new BigDecimal(pricelist.get(j).get("purchase_price").toString())));
                            if(pricelist.get(j).get("likucun")!=null){
                                BigDecimal a = new BigDecimal(pricelist.get(j).get("likucun").toString());
                                BigDecimal b = new BigDecimal(list.get(i).get("norms1").toString());
                                BigDecimal c = new BigDecimal(pricelist.get(j).get("purchase_price").toString());
                                priceaa = priceaa.add(a.divide(b,4).multiply(c));
                            }
                        }
                    }
                }
                priceaa.setScale(2,BigDecimal.ROUND_HALF_UP);
                Map<String, Object> map = new HashedMap();
                //map.put("data",list);
                PageInfo pageInfo = new PageInfo(list);
                map.put("data",pageInfo.getList());
                pd.clear();
                pd.put("object",map);
                pd.put("pageInfo",pageInfo);
                pd.put("code","1");
                pd.put("message",message);
                pd.put("money",priceaa);
                pd.put("pageTotal",pageInfo.getPages());
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @RequestMapping(value = "findProductType",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findProductType(String type,String store_id,String typeName){
        logBefore(logger,"根据商品类型查询商品");
        PageData pd=this.getPageData();
        try{
            List<PageData> list=productService.findListType(pd);
            pd.clear();
            pd.put("object",list);
            pd.put("code","1");
            pd.put("message","正确返回数据!");
        }catch (Exception e){
            logBefore(logger,"-------------根据商品类型查询商品--------"+e);
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }





    /**
     * 查询商品
     * @param store_id 店铺ID
     * @param pageNum  页码
     * @return
     */
    @RequestMapping(value = "findProduct",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findProduct(String store_id,String pageNum,String type){
        logBefore(logger,"查询商品");
        PageData pd=this.getPageData();//获取请求参数
        //Page page=new Page();
        if(store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                if(pd.containsKey("pageSize")){
                    pd.remove("pageSize");
                }
                String message="正确返回数据!";
                if (pageNum == null || pageNum.length() == 0) {
                    pageNum = "1";
                }
                /*Integer SHU1 = Integer.valueOf(pageNum) * 10;
                pd.put("SHU1", SHU1 - 10);*/
                //查询店铺库存的所有商品
                Integer pageNumber = Integer.valueOf(pageNum);
                Integer pageSize = 10;
                //PageHelper.startPage(pageNumber,pageSize);
                List<PageData> lista=productService.selectproductList(pd);//查询商品列表
                int startIndex = (pageNumber - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize,lista.size());
                List<PageData> list = lista.subList(startIndex,endIndex);//显示商品列表

                com.github.pagehelper.Page page = new com.github.pagehelper.Page(pageNumber, pageSize);
                page.setTotal(lista.size());
                page.addAll(list);
                PageInfo pageInfo = new PageInfo<>(page);
                //查询店铺一共有多少个商品
                //PageData pd_c=productService.findProductCount(pd);
                for (int i=0,len=list.size();i<len;i++){
                    //查询店铺的图片norms1
                    List<PageData> list_img=productImgService.findList(list.get(i));
                    list.get(i).put("img",list_img);
                    BigDecimal a = new BigDecimal(list.get(i).get("likucun").toString());
                    BigDecimal b = new BigDecimal(1);
                    try {
                        b = new BigDecimal(list.get(i).get("norms1").toString());
                    }catch (Exception e){

                    }

                    BigDecimal c = null;
                    try {
                        c = a.divide(b,4);//likucun/规格
                    } catch (Exception e) {
                        c= new BigDecimal(0);
                    }
                    BigDecimal kus = new BigDecimal(list.get(i).get("kucun").toString()).add(c);
                    list.get(i).put("kucun",kus);
                }
                /*Integer pageTotal;
                if (Integer.valueOf(pd_c.get("count").toString()) % 10 == 0){
                    pageTotal =Integer.valueOf(pd_c.get("count").toString()) / 10;
                }else {
                    pageTotal = Integer.valueOf(pd_c.get("count").toString()) / 10 + 1;
                }*/
                Map<String, Object> map = new HashedMap();
                map.put("data",pageInfo.getList());
                //下面计算价格的地方错误，需要修改
                /*PageData pd_s=productService.findKuCun(pd);
                if(pd_s!=null){
                    moneg=pd_s.get("money").toString();
                }*/
                //开始计算总进货价
                BigDecimal priceaa = new BigDecimal(0);
                for (int i = 0; i <lista.size() ; i++) {
                    //if(Integer.valueOf(String.valueOf(list.get(i).get("kucun")))>0){
                    if(new BigDecimal(String.valueOf(lista.get(i).get("kucun"))).compareTo(new BigDecimal(0))>0){
                        PageData kucun= new PageData();
                        kucun.put("product_id",lista.get(i).get("product_id"));
                        kucun.put("store_id",lista.get(i).get("store_id"));
                        List<PageData> pricelist = productService.findProductPrice(kucun);
                        for (int j = 0; j <pricelist.size() ; j++) {
                            //System.out.println("數量："+pricelist.get(j).get("nums")+"单价是："+pricelist.get(j).get("purchase_price"));
                            priceaa = priceaa.add(new BigDecimal(pricelist.get(j).get("nums").toString()).multiply(new BigDecimal(pricelist.get(j).get("purchase_price").toString())));
                            if(pricelist.get(j).get("likucun")!=null){
                                BigDecimal a = new BigDecimal(pricelist.get(j).get("likucun").toString());
                                BigDecimal b = new BigDecimal(lista.get(i).get("norms1").toString());
                                BigDecimal c = new BigDecimal(pricelist.get(j).get("purchase_price").toString());
                                priceaa = priceaa.add(a.divide(b,4).multiply(c));// 0/100*10
                            }
                        }
                    }
                }
                priceaa.setScale(2,BigDecimal.ROUND_HALF_UP);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message",message);
                pd.put("pageTotal",String.valueOf(pageInfo.getPages()));
                pd.put("money",priceaa);
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.error("查询商品返回参数------》："+str);
        return str;
    }


    /**
     * 查询商品
     * @param store_id 店铺ID
     * @param pageNum  页码
     * @return
     */
    @RequestMapping(value = "findProductList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findProductList(String store_id,String pageNum,String type){
        logBefore(logger,"查询商品");
        PageData pd=this.getPageData();//获取请求参数
        //Page page=new Page();
        if(store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                if(pd.containsKey("pageSize")){
                    pd.remove("pageSize");
                }
                String message="正确返回数据!";
                if (pageNum == null || pageNum.length() == 0) {
                    pageNum = "1";
                }
                /*Integer SHU1 = Integer.valueOf(pageNum) * 10;
                pd.put("SHU1", SHU1 - 10);*/
                //查询店铺库存的所有商品
                Integer pageNumber = Integer.valueOf(pageNum);
                Integer pageSize = 10;
                //PageHelper.startPage(pageNumber,pageSize);
                List<PageData> lista=productService.selectproductList(pd);//查询商品列表
                int startIndex = (pageNumber - 1) * pageSize;
                int endIndex = Math.min(startIndex + pageSize,lista.size());
                List<PageData> list = lista.subList(startIndex,endIndex);//显示商品列表

                com.github.pagehelper.Page page = new com.github.pagehelper.Page(pageNumber, pageSize);
                page.setTotal(lista.size());
                page.addAll(list);
                PageInfo pageInfo = new PageInfo<>(page);
                //查询店铺一共有多少个商品
                //PageData pd_c=productService.findProductCount(pd);
                for (int i=0,len=list.size();i<len;i++){
                    //查询店铺的图片norms1
                    List<PageData> list_img=productImgService.findList(list.get(i));
                    list.get(i).put("img",list_img);
                    BigDecimal a = new BigDecimal(list.get(i).get("likucun").toString());
                    BigDecimal b = new BigDecimal(1);
                    try {
                        b = new BigDecimal(list.get(i).get("norms1").toString());
                    }catch (Exception e){

                    }

                    BigDecimal c = null;
                    try {
                        c = a.divide(b,4);//likucun/规格
                    } catch (Exception e) {
                        c= new BigDecimal(0);
                    }
                    BigDecimal kus = new BigDecimal(list.get(i).get("kucun").toString()).add(c);
                    list.get(i).put("kucun",kus);
                }
                /*Integer pageTotal;
                if (Integer.valueOf(pd_c.get("count").toString()) % 10 == 0){
                    pageTotal =Integer.valueOf(pd_c.get("count").toString()) / 10;
                }else {
                    pageTotal = Integer.valueOf(pd_c.get("count").toString()) / 10 + 1;
                }*/
                Map<String, Object> map = new HashedMap();
                map.put("data",pageInfo.getList());
                //下面计算价格的地方错误，需要修改
                /*PageData pd_s=productService.findKuCun(pd);
                if(pd_s!=null){
                    moneg=pd_s.get("money").toString();
                }*/
                //开始计算总进货价
                BigDecimal priceaa = new BigDecimal(0);
                /*for (int i = 0; i <lista.size() ; i++) {
                    //if(Integer.valueOf(String.valueOf(list.get(i).get("kucun")))>0){
                    if(new BigDecimal(String.valueOf(lista.get(i).get("kucun"))).compareTo(new BigDecimal(0))>0){
                        PageData kucun= new PageData();
                        kucun.put("product_id",lista.get(i).get("product_id"));
                        kucun.put("store_id",lista.get(i).get("store_id"));
                        List<PageData> pricelist = productService.findProductPrice(kucun);
                        for (int j = 0; j <pricelist.size() ; j++) {
                            //System.out.println("數量："+pricelist.get(j).get("nums")+"单价是："+pricelist.get(j).get("purchase_price"));
                            priceaa = priceaa.add(new BigDecimal(pricelist.get(j).get("nums").toString()).multiply(new BigDecimal(pricelist.get(j).get("purchase_price").toString())));
                            if(pricelist.get(j).get("likucun")!=null){
                                BigDecimal a = new BigDecimal(pricelist.get(j).get("likucun").toString());
                                BigDecimal b = new BigDecimal(lista.get(i).get("norms1").toString());
                                BigDecimal c = new BigDecimal(pricelist.get(j).get("purchase_price").toString());
                                priceaa = priceaa.add(a.divide(b,4).multiply(c));// 0/100*10
                            }
                        }
                    }
                }*/
                priceaa.setScale(2,BigDecimal.ROUND_HALF_UP);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message",message);
                pd.put("pageTotal",String.valueOf(pageInfo.getPages()));
                pd.put("money",priceaa);
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.error("查询商品返回参数------》："+str);
        return str;
    }



    /**
     * 查询剩余库存成本
     * @param storeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "stockTotal",method = RequestMethod.POST,produces = "application/json")
    public Result stockTotal1(Long storeId){

        Result result = new Result(0,"成功");

        try {
            Map<String,Object>totalPrice = productService.stockTotal(storeId);
            if(totalPrice==null){
                result.setData(0);
            }else {
                result.setData(totalPrice);
            }

        }catch (ServiceException e) {
            logger.error("数据操作失败",e);
            result.setCode(e.getExceptionCode());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("系统错误",e);
            result.setCode(-101);
            logger.error("系统错误",e);
        }
        return result;

    }

    /**
     * 添加商品
     * @return
     */
    @RequestMapping(value = "saveProduct",method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseBody
    public String saveProduct(@RequestBody ProductVo productVo) {
//        Gson gson=new Gson();
//        ProductVo productVo=gson.fromJson(productString,ProductVo.class);
        logger.error(productVo);
        logBefore(logger, "添加商品");
        PageData pd = this.getPageData();
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        String product_img = "";
        String bar_code_number = "";
        String purchasePrice="";
        Product product = new Product();
        try {

                if (!productVo.getProduct_img().contains("http")) {
                    if (productVo.getProduct_img() != null && productVo.getProduct_img().length() != 0) {
                        BaiduPushUtil.ffile();
                        String ffile = this.get32UUID() + ".jpg";
                        String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + DateUtil.getDays() + "/" + ffile; // 文件上传路径
                        boolean flag = ImageAnd64Binary.generateImage(productVo.getProduct_img(), filePath);
                        product_img = Const.SERVERPATH + Const.FILEPATHIMG + DateUtil.getDays() + "/" + ffile;
                        System.out.println(">>>>>>1111" + filePath);
                        System.out.println(">>>>>>2222" + Const.SERVERPATH);
                        System.out.println(">>>>>>3333" + product_img);
                    }
                }

                try {
                    BigDecimal nooo = new BigDecimal(productVo.getNorms1());
                    if (nooo.compareTo(new BigDecimal(0)) == 0 || nooo.compareTo(new BigDecimal(0)) == -1) {
                        return getMessage("102", "商品规格1格式不正确！");
                    }
                } catch (NumberFormatException e) {
                    return getMessage("102", "商品规格必须为数字！");
                }
                product.setProductImg(product_img);
                product.setDate(DateUtil.getTime());
                /*pd.put("product_img", product_img);
                pd.put("date", DateUtil.getTime());*/

                if (productVo.getBar_code_number() == null || productVo.getBar_code_number().length() == 0) {
                    pd.put("bar_code_number", BaiduPushUtil.random(productVo.getStore_id()));
                    product.setBarCodeNumber(BaiduPushUtil.random(productVo.getStore_id()));
                    PageData pd_p = productService.findBarCodeProduct(pd);
                    if (pd_p != null) {
                        pd.put("bar_code_number", BaiduPushUtil.random(productVo.getStore_id()));
                        product.setBarCodeNumber(BaiduPushUtil.random(productVo.getStore_id()));
                    }
                    bar_code_number = pd.getString("bar_code_number");
                }
                String bar_code = "";
                if (Integer.valueOf(productVo.getIsThreeSales()) == 0) {
                    bar_code = BaiduPushUtil.generateBarCode(bar_code_number, "商品:"
                            + productVo.getProduct_name(), "规格:" + productVo.getNorms1() + productVo.getNorms2() + productVo.getNorms3());
                } else {
                    bar_code = BaiduPushUtil.generateBarCode(bar_code_number, "商品:"
                            + productVo.getProduct_name(), "规格:" + productVo.getNorms3() + productVo.getNorms4() + productVo.getNorms5());
                }

                if (bar_code.equals("false")) {
                    pd.clear();
                    pd.put("code", "2");
                    pd.put("message", "生成条形码失败,请重试!");
                    return mapper.writeValueAsString(pd);
                }
                product.setBarCode(bar_code);
                product.setProductName(productVo.getProduct_name());
                product.setProductPrice(productVo.getProduct_price());
                product.setNorms1(productVo.getNorms1());
                product.setNorms2(productVo.getNorms2());
                product.setNorms3(productVo.getNorms3());
                product.setNorms4(productVo.getNorms4());
                product.setNorms5(productVo.getNorms5());
                product.setThreePurchase(productVo.getThreePurchase());

                product.setProductionEnterprise(productVo.getProduction_enterprise());

                String kucun;
                if (productVo.getKucun()==null || productVo.getKucun().length()==0){
                    kucun="0";
                }else {
                    kucun = productVo.getKucun();
                }

                if(productVo.getIsThreeSales().equals("1")){
                    product.setKucun(new BigDecimal(kucun).multiply(new BigDecimal(productVo.getNorms4())));
                    if (productVo.getPurchase_price()==null || productVo.getPurchase_price().length()==0 || productVo.getPurchase_price().equals("0")){
                        product.setPurchasePrice("0");
                    }else {
                        BigDecimal purchase = new BigDecimal(productVo.getPurchase_price()).divide(new BigDecimal(productVo.getNorms4()));
                        product.setPurchasePrice(purchase.toString());
                    }

                }else {

                    if (productVo.getPurchase_price()==null || productVo.getPurchase_price().length()==0 || productVo.getPurchase_price().equals("0")){
                        product.setPurchasePrice("0");
                        product.setKucun(new BigDecimal("0"));
                    }else {
                        product.setKucun(new BigDecimal(kucun));
                        product.setPurchasePrice(purchasePrice);
                    }
                }
                product.setType(productVo.getType());
                product.setSupplierId(Long.valueOf(productVo.getSupplier_id()==null ? "0" : productVo.getSupplier_id()));
                product.setLikucun(new BigDecimal(0));
                product.setNumber(productVo.getNumber());
                product.setUrl(productVo.getUrl());
                product.setNumberTow(productVo.getNumber_tow()==null?"":productVo.getNumber_tow());
                product.setType2(productVo.getType2());
                product.setIsThreeSales(Integer.valueOf(productVo.getIsThreeSales()));
                product.setStoreId(Long.valueOf(productVo.getStore_id()));
                product.setStatus(0);

                productNewMapper.insert(product);
                if (product_img != null && product_img.length() != 0) {
                    PageData pd_img = new PageData();
                    pd_img.put("product_id", pd.get("product_id"));
                    pd_img.put("img", product_img);
                    pd_img.put("orde_by", "1");
                    productImgService.save(pd_img);
                }
                if (productVo.getPurchase_price() != null && productVo.getPurchase_price().length() != 0 &&
                        productVo.getKucun() != null && productVo.getKucun().length() != 0) {
                    PageData pd_k = new PageData();
                    pd_k.put("status", "2");
                    pd_k.put("supplier_id", "0");
                    pd_k.put("store_id", productVo.getStore_id());
                    pd_k.put("product_id", product.getProductId().toString());
                    pd_k.put("customer_id", "0");
                    if(productVo.getIsThreeSales().equals("1")){
                        pd_k.put("num", new BigDecimal(productVo.getKucun()).multiply(new BigDecimal(productVo.getNorms4())));
                        pd_k.put("nums", new BigDecimal(productVo.getKucun()).multiply(new BigDecimal(productVo.getNorms4())));
                        pd_k.put("purchase_price", new BigDecimal(productVo.getPurchase_price()).divide(new BigDecimal(productVo.getNorms4())).toString());
                    }else {
                        pd_k.put("num", new BigDecimal(productVo.getKucun()));
                        pd_k.put("nums", new BigDecimal(productVo.getKucun()));
                        pd_k.put("purchase_price", productVo.getPurchase_price());
                    }
                    pd_k.put("money", "0");
                    pd_k.put("total", "0");
                    pd_k.put("jia", "0");
                    pd_k.put("date", DateUtil.getTime());
                    pd_k.put("order_info_id", "0");
                    pd_k.put("product_price", productVo.getProduct_price());
                    kunCunService.save(pd_k);
                }
                pd.clear();
                pd.put("code", "1");
                pd.put("message", "正确返回数据!");

            } catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
            }
            try {
                str = mapper.writeValueAsString(pd);

            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.error(str);
            return str;

    }
        /**
     * 删除商品
     * @param product_id 商品ID
     * @return
     */
    @RequestMapping(value = "deleteProduct",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String deleteProduct(String product_id){
        logBefore(logger,"删除商品");
        PageData pd=this.getPageData();
        if(product_id==null||product_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        } else {
            try{
                productService.editStatus(pd);
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 修改商品
     * @return
     */
    @RequestMapping(value = "updateProduct",method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseBody
    public String updateProduct(@RequestBody ProductVo product){
        logBefore(logger,"修改商品");
        PageData pd=this.getPageData();
        ObjectMapper mapper=new ObjectMapper();
        String str="";

        String product_img=product.getProduct_img();
            try{
                pd.put("product_id",product.getProduct_id());
                PageData pd_p=productService.findById(pd);
                if(product.getProduct_img()!=null && product.getProduct_img().length()!=0){
                    if (!product.getProduct_img().contains("http")) {
                        String ffile1 = this.get32UUID() + ".jpg";
                        String filePath2 = PathUtil.getClasspath() + Const.FILEPATHIMG + DateUtil.getDays(); // 文件上传路径
                        File file = new File(filePath2, ffile1);
                        if (!file.exists()) {
                            if (!file.getParentFile().exists()) {
                                file.getParentFile().mkdirs();
                            }
                        }
                        String ffile = this.get32UUID() + ".jpg";
                        String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + DateUtil.getDays() + "/" + ffile; // 文件上传路径
                        boolean flag = ImageAnd64Binary.generateImage(product_img, filePath);
                        product_img = Const.SERVERPATH + Const.FILEPATHIMG + DateUtil.getDays() + "/" + ffile;
                    }
                }
                pd.put("production_enterprise",product.getProduction_enterprise());
                if(product.getProduction_enterprise()==null){
                    pd.put("production_enterprise","");
                }
                String purchase_price="";

                if (product.getPurchase_price()==null || product.getPurchase_price().length()==0){
                    purchase_price="0";
                }else {
                    purchase_price = product.getPurchase_price();
                }
                String kucn="";
                if (product.getKucun()==null || product.getKucun().length()==0 || product.getKucun().equals("0")){
                    kucn="0";
                }else {
                    kucn = product.getKucun();
                }

                if(product.getIsThreeSales().equals("1")){
                    pd.put("kucun",new BigDecimal(kucn).multiply(new BigDecimal(product.getNorms4())));
                    pd.put("purchase_price", new BigDecimal(purchase_price).divide(new BigDecimal(product.getNorms4())).toString());

                }else {
                    pd.put("kucun",new BigDecimal(kucn));
                    pd.put("purchase_price", purchase_price);
                }
                if(product.getKucun()==null||product.getKucun().length()==0){
                    pd.put("kucun","0");
                }
                pd.put("type",product.getType());
                if(product.getType()==null||product.getType().length()==0){
                    pd.put("type","其他");
                }

                pd.put("product_img",product_img);
                if(product.getBar_code_number()!=null&&product.getBar_code_number().length()!=0){
                    if(product.getBar_code_number().equals(pd_p.getString("bar_code_number"))){
                        pd.put("bar_code",pd_p.getString("bar_code"));
                        pd.put("bar_code_number",pd_p.getString("bar_code_number"));
                    }else{
                       String bar_code = "";
                        if (Integer.valueOf(product.getIsThreeSales()) == 0) {
                            bar_code = BaiduPushUtil.generateBarCode(product.getBar_code_number(), "商品:"
                                    + product.getProduct_name(), "规格:" + product.getNorms1() + product.getNorms2() + product.getNorms3());
                        } else {
                            bar_code = BaiduPushUtil.generateBarCode(product.getBar_code_number(), "商品:"
                                    + product.getProduct_name(), "规格:" + product.getNorms4() + product.getNorms3() + product.getNorms5());
                        }
                        if(bar_code.equals("false")){
                            pd.clear();
                            pd.put("code","2");
                            pd.put("message","生成条形码失败,请重试!");
                            return mapper.writeValueAsString(pd);
                        }else{
                            BaiduPushUtil.deleteFile(pd_p.getString("bar_code"));
                        }
                        pd.put("bar_code",bar_code);
                        pd.put("bar_code_number",product.getBar_code_number());
                    }

                }else{
                    pd.put("bar_code",pd_p.getString("bar_code"));
                    pd.put("bar_code_number",pd_p.getString("bar_code_number"));
                }
                pd.put("product_name",product.getProduct_name());
                pd.put("store_id", product.getStore_id());
                pd.put("product_price", product.getProduct_price());
                pd.put("norms1", product.getNorms1());
                pd.put("norms2", product.getNorms2());
                pd.put("norms3", product.getNorms3());
                pd.put("norms4", product.getNorms4());
                pd.put("norms5", product.getNorms5());
                pd.put("threePurchase", product.getThreePurchase());

                pd.put("supplier_id", product.getSupplier_id());
                pd.put("number", product.getNumber());
                pd.put("url", product.getUrl());
                pd.put("number_tow", product.getNumber_tow());
                pd.put("type2", product.getType2());
                pd.put("level1_price", product.getLevel1_price());
                pd.put("level2_price", product.getLevel2_price());
                pd.put("level3_price", product.getLevel3_price());
                pd.put("isThreeSales", product.getIsThreeSales());

                productService.updateProduct(pd);

                if(product_img != null && product_img.length()!=0){
                    productImgService.delete(pd);
                    PageData pd_img=new PageData();
                    pd_img.put("product_id",pd.get("product_id"));
                    pd_img.put("img",product_img);
                    pd_img.put("orde_by","1");
                    productImgService.save(pd_img);
                }else {
                    productImgService.delete(pd);
                }
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }

        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @RequestMapping(value = "findBarCodeProduct",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findBarCodeProduct(String store_id,String bar_code_number){
        logBefore(logger,"安卓根据条形码查询商品详情");
        PageData pd=this.getPageData();
        if(bar_code_number==null||bar_code_number.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                PageData pd_p=productService.findBarCodeProduct(pd);
                List<PageData> list_p=new ArrayList<>();
                if(pd_p!=null){
                    List<PageData> list_img=productImgService.findList(pd_p);
                    pd_p.put("img",list_img);
                }else{
                    pd_p=new PageData();
                }
                list_p.add(pd_p);
                Map<String, Object> map = new HashedMap();
                map.put("data",list_p);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    @RequestMapping(value = "findBarCodeProducts",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findBarCodeProducts(String store_id,String bar_code_number){
        logBefore(logger,"IOS根据条形码查询商品详情");
        PageData pd=this.getPageData();
        ObjectMapper mapper=new ObjectMapper();
        if(bar_code_number==null||bar_code_number.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                PageData pd_p=productService.findBarCodeProduct(pd);
                if(pd_p!=null){
                    List<PageData> list_img=productImgService.findList(pd_p);
                    pd_p.put("img",list_img);
                }else{
                    pd.clear();
                    pd.put("code","3");
                    pd.put("message","没有数据!");
                    return mapper.writeValueAsString(pd);
                }
                Map<String, Object> map = new HashedMap();
                map.put("data",pd_p);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    @RequestMapping(value = "findBarCodeProductss",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findBarCodeProductss(String store_id,String bar_code_number){
        logBefore(logger,"安卓根据条形码查询商品详情");
        PageData pd=this.getPageData();
        if(bar_code_number==null||bar_code_number.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                List<PageData> list=productService.findBarCodeProducts(pd);
                for(int i=0;i<list.size();i++){
                    List<PageData> list_img=productImgService.findList(list.get(i));
                    list.get(i).put("img",list_img);
                }
                Map<String, Object> map = new HashedMap();
                map.put("data",list);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 调整库存
     * @param product_id
     * @param kucun
     * @return
     */
    @RequestMapping(value = "editKunCun",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editKunCun(String product_id,String kucun,Integer isThreeSales){
        logBefore(logger,"修改库存");
        PageData pd=this.getPageData();
        System.out.println(">>>>>>"+pd.get("kucun").toString());
        try{

            // 查询商品的信息
            PageData pd_p=productService.findById(pd);
            //修改商品表库存
            productService.editNums(pd);
            if(new BigDecimal(kucun).compareTo(new BigDecimal(0))==0){//如果清除库存
                productService.editNumslikucun(pd);
                kunCunService.editNumlikucun(pd);
                kunCunService.setNumsClear(pd);
            }
            pd_p.put("status","3");
            pd_p.put("supplier_id","0");
            pd_p.put("customer_id","0");
            BigDecimal cc = new BigDecimal(0);//这是实际操作的差值
            BigDecimal whole = new BigDecimal(0);//这是实际操作的差值

            /*if(Integer.valueOf(pd_p.get("kucun").toString())>Integer.valueOf(kucun)){*/
            if(new BigDecimal(pd_p.get("kucun").toString()).compareTo(new BigDecimal(kucun))==1){
                //pd_p.put("num",Integer.valueOf(pd_p.get("kucun").toString())-Integer.valueOf(kucun));
                pd_p.put("num",new BigDecimal(pd_p.get("kucun").toString()).subtract(new BigDecimal(kucun)));
                pd_p.put("jia","1");
                pd_p.put("total","0");
                pd_p.put("money","0");
                //cc = Integer.valueOf(pd_p.get("kucun").toString())-Integer.valueOf(kucun);
                cc = new BigDecimal(pd_p.get("kucun").toString()).subtract(new BigDecimal(kucun));
            }else{
                //pd_p.put("num",Integer.valueOf(kucun)-Integer.valueOf(pd_p.get("kucun").toString()));
                pd_p.put("num",new BigDecimal(kucun).subtract(new BigDecimal(pd_p.get("kucun").toString())));
                //pd_p.put("money",Double.valueOf(pd_p.getString("purchase_price"))*Integer.valueOf(pd_p.get("num").toString()));
                pd_p.put("money",new BigDecimal(pd_p.get("purchase_price").toString()).multiply(new BigDecimal(pd_p.get("num").toString())).setScale(2,BigDecimal.ROUND_HALF_UP));
                //pd_p.put("total",Double.valueOf(pd_p.getString("purchase_price"))*Integer.valueOf(pd_p.get("num").toString()));
                pd_p.put("total",new BigDecimal(pd_p.get("purchase_price").toString()).multiply(new BigDecimal(pd_p.get("num").toString())).setScale(2,BigDecimal.ROUND_HALF_UP));
                pd_p.put("jia","0");
                //cc = Integer.valueOf(kucun)-Integer.valueOf(pd_p.get("kucun").toString());
                cc = new BigDecimal(kucun).subtract(new BigDecimal(pd_p.get("kucun").toString()));
            }
            pd_p.put("date",DateUtil.getTime());
            pd_p.put("order_info_id","0");
            if(pd_p.getString("purchase_price")!=null&&!pd_p.getString("purchase_price").equals("0")){
                pd_p.put("purchase_price",pd_p.getString("purchase_price"));
            }else{
                pd_p.put("purchase_price","0");
            }
            pd_p.put("product_price",pd_p.getString("product_price"));
            pd_p.put("nums",0);
            kunCunService.save(pd_p);
            pd.put("store_id",pd_p.get("store_id").toString());

            //查询进货单
            List<PageData> list=kunCunService.findListjin(pd);
            //if(Integer.valueOf(pd_p.get("kucun").toString())>Integer.valueOf(kucun)){//这是减库存了
            if(new BigDecimal(pd_p.get("kucun").toString()).compareTo(new BigDecimal(kucun))==1){//这是减库存了
                /*int in=cc;//实际操作的差值*/
                //int in = Integer.valueOf(pd_p.get("kucun").toString()) - Integer.valueOf(kucun);
                BigDecimal in = new BigDecimal(pd_p.get("kucun").toString()).subtract(new BigDecimal(kucun));
                BigDecimal left = in;
                for(int i=0;i<list.size();i++){
                    if (left.compareTo(new BigDecimal("0"))==0){
                        break;
                    }
                    //if(in>Integer.valueOf(list.get(i).get("nums").toString())){
                    if(in.compareTo(new BigDecimal(list.get(i).get("nums").toString()))==1){
                        //Integer kuncuns=Integer.valueOf(in)-Integer.valueOf(list.get(i).get("nums").toString());
                        BigDecimal kuncuns=in.subtract(new BigDecimal(list.get(i).get("nums").toString()));
                        list.get(i).put("nums","0");
                        kunCunService.editNum(list.get(i));
                        in=kuncuns;
                    }else {
                        //list.get(i).put("nums",Integer.valueOf(list.get(i).get("nums").toString())-Integer.valueOf(in));
                        BigDecimal tmp = new BigDecimal(list.get(i).get("nums").toString()).subtract(in);
                        list.get(i).put("nums",new BigDecimal(list.get(i).get("nums").toString()).subtract(in));
                        kunCunService.editNum(list.get(i));
                        left = left.subtract(in);

                    }
                }
            }else {//这个地方加库存
                if(list.size()>0){
                    PageData pageData = list.get(list.size()-1);
                    //pageData.put("nums",Integer.valueOf(pageData.get("nums").toString()) + Integer.valueOf(cc));
                    pageData.put("nums",new BigDecimal(pageData.get("nums").toString()).add(cc));
                    kunCunService.editNum(pageData);
                }
            }

            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @RequestMapping(value = "findKunCun",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findKunCun(String product_id,String pageNum){
        logBefore(logger,"查询库存明细");
        PageData pd=this.getPageData();
        Page page=new Page();
        try {
            String message="正确返回数据!";
            if (pageNum == null || pageNum.length() == 0) {
                pageNum = "1";
            }
            page.setPd(pd);
            page.setShowCount(10);
            page.setCurrentPage(Integer.parseInt(pageNum));
            List<PageData> list=kunCunService.datalistPage(page);
            Map<String, Object> map = new HashedMap();
            if (page.getCurrentPage() == Integer.parseInt(pageNum)) {
                map.put("data",list);
            }else{
                map.put("message",message);
                map.put("data",new ArrayList());
            }
            pd.clear();
            pd.put("object",map);
            pd.put("code","1");
            pd.put("message",message);
            pd.put("pageTotal",String.valueOf(page.getTotalPage()));
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @RequestMapping(value = "findNotRelationProduct",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findNotRelationProduct(String store_id,String pageNum){
        logBefore(logger,"查询店铺未关联的商品");
        PageData pd=this.getPageData();
        Page page=new Page();
        try{
            String message="正确返回数据!";
            if (pageNum == null || pageNum.length() == 0) {
                pageNum = "1";
            }
            pd.put("return_goods","0");
            page.setPd(pd);
            page.setShowCount(10);
            page.setCurrentPage(Integer.parseInt(pageNum));
            List<PageData> list=productService.findNotSupplierList(page);
            Map<String, Object> map = new HashedMap();
            if (page.getCurrentPage() == Integer.parseInt(pageNum)) {
                map.put("data", list);
            }else{
                map.put("message",message);
                map.put("data",new ArrayList());
            }
            pd.clear();
            pd.put("object", map);
            pd.put("message", message);
            pd.put("code", "1");
            pd.put("pageTotal",String.valueOf(page.getTotalPage()));
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    @RequestMapping(value = "findlikeNotRelationProduct",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findlikeNotRelationProduct(String store_id,String name){
        logBefore(logger,"模糊查询店铺未关联的商品");
        PageData pd=this.getPageData();
        try{
            List<PageData> list=productService.findLikeSupplierProduct(pd);
            Map<String, Object> map = new HashedMap();
            map.put("data", list);
            pd.clear();
            pd.put("object", map);
            pd.put("message", "正确返回数据");
            pd.put("code", "1");
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * @param type (PD,WP,LS)
     * @param number 数字
     * @param store_id 店ID
     * @return
     */
    @RequestMapping(value = "findNumber",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findNumber(String type,String number,String store_id){
        logBefore(logger,"安卓扫描二维码查询商品是否存在");
        PageData pd=this.getPageData();
        try{
            pd.put("statuss","2");
            PageData pd_p=productService.findByNumber(pd);
            List<PageData> list_p=new ArrayList<>();
            if(pd_p!=null){
                List<PageData> list_img=productImgService.findList(pd_p);
                pd_p.put("img",list_img);
            }else{
                pd_p=new PageData();
            }
            list_p.add(pd_p);
            Map<String, Object> map = new HashedMap();
            map.put("data",list_p);
            pd.clear();
            pd.put("object",map);
            pd.put("code","1");
            pd.put("message","正确返回数据!");
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    @RequestMapping(value = "findNumbers",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findNumbers(String type,String number,String store_id){
        logBefore(logger,"IOS扫描二维码查询商品是否存在");
        PageData pd=this.getPageData();
        ObjectMapper mapper=new ObjectMapper();
        if(store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                pd.put("statuss","2");
                PageData pd_p=productService.findByNumber(pd);
                if(pd_p!=null){
                    List<PageData> list_img=productImgService.findList(pd_p);
                    pd_p.put("img",list_img);
                }else{
                    pd.clear();
                    pd.put("code","3");
                    pd.put("message","没有数据!");
                    return mapper.writeValueAsString(pd);
                }
                Map<String, Object> map = new HashedMap();
                map.put("data",pd_p);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * @param type (PD,WP,LS)
     * @param number 数字
     * @param store_id 店ID
     * @return
     */
    @RequestMapping(value = "findNumberss",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findNumberss(String type,String number,String store_id){
        logBefore(logger,"安卓扫描二维码查询商品是否存在");
        PageData pd=this.getPageData();
        try{
            pd.put("statuss","2");
            List<PageData> list=productService.findByNumbers(pd);
            for(int i=0;i<list.size();i++){
                List<PageData> list_img=productImgService.findList(list.get(i));
                list.get(i).put("img",list_img);
            }
            Map<String, Object> map = new HashedMap();
            map.put("data",list);
            pd.clear();
            pd.put("object",map);
            pd.put("code","1");
            pd.put("message","正确返回数据!");
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /*@RequestMapping(value = "findWholeList",produces = "text/htmk;charset=UTF-8")
    @ResponseBody
    public String findWholeList(String store_id){
        logBefore(logger,"查询");
    }*/

}
