package com.mtnz.controller.app.product;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtnz.controller.base.BaseController;
import com.mtnz.entity.Page;
import com.mtnz.service.system.order_info.OrderInfoService;
import com.mtnz.service.system.order_pro.OrderProService;
import com.mtnz.service.system.product.KunCunService;
import com.mtnz.service.system.product.ProductImgService;
import com.mtnz.service.system.product.ProductService;
import com.mtnz.service.system.product.WeProductService;
import com.mtnz.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.xml.rpc.ServiceException;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/*
    Created by xxj on 2018\3\22 0022.  
 */
@Controller
@RequestMapping(value = "app/product",produces = "text/html;charset=UTF-8")
public class AppProductController extends BaseController{

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


    @RequestMapping(value = "findProductFeiXis",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findProductFeiXis(String store_id,String status,String product_id,
                                    String startTime,String endTime,String product_name){
        logBefore(logger,"查询详情");
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
            List<PageData> list=productService.findProductFeiXis(pd);
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("data",list);
            pd.put("pageTotal","1");
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
    public String findlikeProduct(String store_id,String product_name){
        logBefore(logger,"模糊查询商品");
        PageData pd=this.getPageData();
        if(store_id==null||store_id.length()==0||product_name==null||product_name.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                String message="正确返回数据!";
                List<PageData> list=productService.findlikeList(pd);
                for (int i=0,len=list.size();i<len;i++){
                    List<PageData> list_img=productImgService.findList(list.get(i));
                    list.get(i).put("img",list_img);
                }
                Map<String, Object> map = new HashedMap();
                map.put("data",list);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message",message);
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
    public String findProductType(String type,String store_id){
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
    public String findProduct(String store_id,String pageNum){
        logBefore(logger,"查询商品");
        PageData pd=this.getPageData();
        Page page=new Page();
        if(store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                String message="正确返回数据!";
                if (pageNum == null || pageNum.length() == 0) {
                    pageNum = "1";
                }
                Integer SHU1 = Integer.valueOf(pageNum) * 10;
                pd.put("SHU1", SHU1 - 10);
                //查询店铺库存的所有商品
                List<PageData> list=productService.findProduct(pd);
                //查询店铺一共有多少个商品
                PageData pd_c=productService.findProductCount(pd);
                for (int i=0,len=list.size();i<len;i++){
                    //查询店铺的图片
                    List<PageData> list_img=productImgService.findList(list.get(i));
                    list.get(i).put("img",list_img);
                }
                Integer pageTotal;
                if (Integer.valueOf(pd_c.get("count").toString()) % 10 == 0){
                    pageTotal =Integer.valueOf(pd_c.get("count").toString()) / 10;
                }else {
                    pageTotal = Integer.valueOf(pd_c.get("count").toString()) / 10 + 1;
                }
                Map<String, Object> map = new HashedMap();
                map.put("data",list);
                String moneg="0.0";
                //下面计算价格的地方错误，需要修改
                /*PageData pd_s=productService.findKuCun(pd);
                if(pd_s!=null){
                    moneg=pd_s.get("money").toString();
                }*/
                //开始计算总价
                BigDecimal priceaa = new BigDecimal(0);
                for (int i = 0; i <list.size() ; i++) {
                    if(Integer.valueOf(String.valueOf(list.get(i).get("kucun")))>0){
                        PageData kucun= new PageData();
                        kucun.put("product_id",list.get(i).get("product_id"));
                        kucun.put("store_id",list.get(i).get("store_id"));
                        List<PageData> pricelist = productService.findProductPrice(kucun);
                        System.out.println(">>>>>>>"+JSONObject.toJSON(pricelist));
                        for (int j = 0; j <pricelist.size() ; j++) {
                            priceaa = priceaa.add(new BigDecimal((Long) pricelist.get(j).get("nums")).multiply(new BigDecimal((String) pricelist.get(j).get("purchase_price"))));
                        }
                    }
                }
                priceaa.setScale(2,BigDecimal.ROUND_HALF_UP);
                moneg = JSONObject.toJSONString(priceaa);
                System.out.println("计算出来的总价格>>>>"+moneg);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message",message);
                pd.put("pageTotal",String.valueOf(pageTotal));
                pd.put("money",moneg);
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
     * 添加商品
     * @param store_id  店铺ID
     * @param product_name 商品名称
     * @param product_price 商品价格
     * @param norms1 商品规格1
     * @param norms2 商品规格2
     * @param norms3 商品规格3
     * @param purchase_price 进货价格
     * @param production_enterprise 生产企业
     * @param product_img 商品图片
     * @param status 状态
     * @param kucun 库存
     * @param type 类型(农药肥料种子其它)
     * @param type2 联动
     * @param bar_code_number 条形码数字
     * @param number 登记证号
     * @param url 网址
     * @return
     */
    @RequestMapping(value = "saveProduct",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveProduct(String store_id,String product_name,String product_price,String norms1,String norms2,String norms3,String purchase_price,
                              String production_enterprise,String product_img,String status,String kucun,String type,String bar_code_number,
                              String supplier_id,String number,String url,String number_tow,String type2){
        logBefore(logger,"添加商品");
        PageData pd=this.getPageData();
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        if(store_id==null||store_id.length()==0||product_name==null||product_name.length()==0||product_price==null||product_price.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!!");
        }else{
            try{
                /*PageData pd_pp=null;
                if(number!=null&&number.length()!=0){
                    pd.put("statuss","1");
                    pd_pp =productService.findByNumber(pd);
                }
                if(pd_pp!=null){
                    pd.clear();
                    pd.put("code","2");
                    pd.put("message","商品已存在,不可以重复添加");
                }else{*/
                    if(status.equals("0")){

                    }else{
                        if(product_img!=null&&product_img.length()!=0){
                            BaiduPushUtil.ffile();
                            String ffile = this.get32UUID() + ".jpg";
                            String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG +DateUtil.getDays()+"/"+ ffile; // 文件上传路径
                            boolean flag = ImageAnd64Binary.generateImage(product_img, filePath);
                            product_img = Const.SERVERPATH + Const.FILEPATHIMG +DateUtil.getDays()+"/"+ ffile;
                        }
                    }
                    if(kucun==null||kucun.length()==0){
                        pd.put("kuncun","0");
                    }
                    pd.put("product_img",product_img);
                    pd.put("date",DateUtil.getTime());
                    if(purchase_price==null||purchase_price.length()==0){
                        pd.put("purchase_price","0");
                    }
                    if(kucun==null||kucun.length()==0){
                        pd.put("kucun","0");
                    }
                    if(type==null||type.length()==0){
                        pd.put("type","其他");
                    }
                    if(type2==null||type2.length()==0){
                        pd.put("type2","");
                    }
                    if(production_enterprise==null||production_enterprise.length()==0){
                        pd.put("production_enterprise","");
                    }
                    if(number==null||number.length()==0){
                        pd.put("number","");
                    }
                    if(url==null||url.length()==0){
                        pd.put("url","");
                    }
                    if(bar_code_number==null||bar_code_number.length()==0){
                        pd.put("bar_code_number",BaiduPushUtil.random(store_id));
                        PageData pd_p=productService.findBarCodeProduct(pd);
                        if(pd_p!=null){
                            pd.put("bar_code_number",BaiduPushUtil.random(store_id));
                        }
                        bar_code_number=pd.getString("bar_code_number");
                    }else{

                    }
                    String bar_code= BaiduPushUtil.generateBarCode(bar_code_number,"商品:"+product_name,"规格:"+norms1+norms2+norms3);
                    if(bar_code.equals("false")){
                        pd.clear();
                        pd.put("code","2");
                        pd.put("message","生成条形码失败,请重试!");
                        return mapper.writeValueAsString(pd);
                    }
                    pd.put("bar_code",bar_code);
                    if(supplier_id==null||supplier_id.length()==0){
                        pd.put("supplier_id","0");
                    }
                    if(number_tow==null||number_tow.length()==0){
                        pd.put("number_tow","");
                    }
                    productService.save(pd);
                    if(product_img!=null&&product_img.length()!=0){
                        PageData pd_img=new PageData();
                        pd_img.put("product_id",pd.get("product_id"));
                        pd_img.put("img",product_img);
                        pd_img.put("orde_by","1");
                        productImgService.save(pd_img);
                    }
                    if(purchase_price!=null&&purchase_price.length()!=0&&kucun!=null&&kucun.length()!=0){
                        PageData pd_k=new PageData();
                        pd_k.put("status","2");
                        pd_k.put("supplier_id","0");
                        pd_k.put("store_id",store_id);
                        pd_k.put("product_id",pd.get("product_id").toString());
                        pd_k.put("customer_id","0");
                        pd_k.put("num",kucun);
                        pd_k.put("money","0");
                        pd_k.put("total","0");
                        pd_k.put("jia","0");
                        pd_k.put("date",DateUtil.getTime());
                        pd_k.put("order_info_id","0");
                        pd_k.put("purchase_price",purchase_price);
                        pd_k.put("product_price",product_price);
                        kunCunService.save(pd_k);
                    }
                    pd.clear();
                    pd.put("code","1");
                    pd.put("message","正确返回数据!");
                /*}*/
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!!");
                e.printStackTrace();
            }
        }
        try {
            str= mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * @param product_id 商品ID
     * @param store_id  店铺ID
     * @param product_name  商品名称
     * @param product_price  商品价格
     * @param norms1 商品规格1
     * @param norms2 商品规格2
     * @param norms3 商品规格3
     * @param purchase_price 进货价格
     * @param production_enterprise 生产企业
     * @param product_img   商品图片
     * @param  IStatus 判断是否修改了图片 0没有(没修改就把图片路径传过来) 1修改了 2只删除了图片
     * @param kucun 库存
     * @param type 类型
     * @param  bar_code_number 条形码随机数
     * @return
     */
    @RequestMapping(value = "updateProduct",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateProduct(String product_id,String store_id,String product_name,String product_price,String norms1
            ,String norms2,String norms3,String purchase_price,String production_enterprise,String product_img,String IStatus,
                                String kucun,String type,String bar_code_number){
        logBefore(logger,"修改商品");
        PageData pd=this.getPageData();
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        if(product_id==null||product_id.length()==0||store_id==null||store_id.length()==0||product_name==null||product_name.length()==0||
            product_price==null||product_price.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                PageData pd_p=productService.findById(pd);
                if(IStatus.equals("1")){
                    if(product_img!=null||product_img.length()!=0){

                        String ffile1 = this.get32UUID() + ".jpg";
                        String filePath2 = PathUtil.getClasspath() + Const.FILEPATHIMG  + DateUtil.getDays(); // 文件上传路径
                        File file = new File(filePath2, ffile1);
                        if (!file.exists()) {
                            if (!file.getParentFile().exists()) {
                                file.getParentFile().mkdirs();
                            }
                        }
                        String ffile = this.get32UUID() + ".jpg";
                        String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG +DateUtil.getDays()+"/"+ ffile; // 文件上传路径
                        boolean flag = ImageAnd64Binary.generateImage(product_img, filePath);
                        product_img = Const.SERVERPATH + Const.FILEPATHIMG +DateUtil.getDays()+"/"+ ffile;
                    }
                }else if(IStatus.equals("2")){
                    product_img = "";
                }
                if(production_enterprise==null){
                    pd.put("production_enterprise","");
                }
                if(kucun==null||kucun.length()==0){
                    pd.put("kucun","0");
                }
                if(type==null||type.length()==0){
                    pd.put("type","其他");
                }
                pd.put("product_img",product_img);
                if(bar_code_number!=null&&bar_code_number.length()!=0){
                    if(bar_code_number.equals(pd_p.getString("bar_code_number"))){
                        pd.put("bar_code",pd_p.getString("bar_code"));
                        pd.put("bar_code_number",pd_p.getString("bar_code_number"));
                    }else{
                        String bar_code= BaiduPushUtil.generateBarCode(bar_code_number,product_name,norms1+norms2+norms3);
                        if(bar_code.equals("false")){
                            pd.clear();
                            pd.put("code","2");
                            pd.put("message","生成条形码失败,请重试!");
                            return mapper.writeValueAsString(pd);
                        }else{
                            BaiduPushUtil.deleteFile(pd_p.getString("bar_code"));
                        }
                        pd.put("bar_code",bar_code);
                        pd.put("bar_code_number",bar_code_number);
                    }

                }else{
                    pd.put("bar_code",pd_p.getString("bar_code"));
                    pd.put("bar_code_number",pd_p.getString("bar_code_number"));
                }
                productService.updateProduct(pd);
                if(IStatus.equals("1")){
                    productImgService.delete(pd);
                    PageData pd_img=new PageData();
                    pd_img.put("product_id",pd.get("product_id"));
                    pd_img.put("img",product_img);
                    pd_img.put("orde_by","1");
                    productImgService.save(pd_img);
                }else if(IStatus.equals("2")){
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


    @RequestMapping(value = "editKunCun",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editKunCun(String product_id,String kucun){
        logBefore(logger,"修改库存");
        PageData pd=this.getPageData();
        System.out.println(">>>>>>"+pd.get("kucun").toString());
        try{
            // 查询商品的信息
            PageData pd_p=productService.findById(pd);
            if(Integer.valueOf(pd_p.get("kucun").toString())<0||Integer.valueOf(pd.get("kucun").toString())>Integer.valueOf(pd_p.get("kucun").toString())){
                throw new ServiceException("参数错误");
            }
            //修改商品表库存
            productService.editNums(pd);
            pd_p.put("status","3");
            pd_p.put("supplier_id","0");
            pd_p.put("customer_id","0");
            Integer cc = 0;//这是实际操作的差值
            if(Integer.valueOf(pd_p.get("kucun").toString())>Integer.valueOf(kucun)){
                pd_p.put("num",Integer.valueOf(pd_p.get("kucun").toString())-Integer.valueOf(kucun));
                pd_p.put("jia","1");
                pd_p.put("total","0");
                pd_p.put("money","0");
                cc = Integer.valueOf(pd_p.get("kucun").toString())-Integer.valueOf(kucun);
            }else{
                pd_p.put("num",Integer.valueOf(kucun)-Integer.valueOf(pd_p.get("kucun").toString()));
                pd_p.put("money",Double.valueOf(pd_p.getString("purchase_price"))*Integer.valueOf(pd_p.get("num").toString()));
                pd_p.put("total",Double.valueOf(pd_p.getString("purchase_price"))*Integer.valueOf(pd_p.get("num").toString()));
                pd_p.put("jia","0");
                cc = Integer.valueOf(kucun)-Integer.valueOf(pd_p.get("kucun").toString());
            }
            pd_p.put("date",DateUtil.getTime());
            pd_p.put("order_info_id","0");
            if(pd_p.getString("purchase_price")!=null&&!pd_p.getString("purchase_price").equals("0")){
                pd_p.put("purchase_price",pd_p.getString("purchase_price"));
            }else{
                pd_p.put("purchase_price",pd_p.getString("product_price"));
            }
            pd_p.put("product_price",pd_p.getString("product_price"));
            kunCunService.save(pd_p);
            pd.put("store_id",pd_p.get("store_id").toString());

            //查询进货单
            List<PageData> list=kunCunService.findListjin(pd);
            System.out.println(">>>>>"+JSONObject.toJSONString(list));
            if(Integer.valueOf(pd_p.get("kucun").toString())>Integer.valueOf(kucun)){//这是减库存了
                int in=cc;//实际操作的差值
                System.out.println("实际操作的差值>>>>>>>"+JSONObject.toJSONString(in));
                for(int i=0;i<list.size();i++){
                    if(in>Integer.valueOf(list.get(i).get("nums").toString())){
                        Integer kuncuns=Integer.valueOf(in)-Integer.valueOf(list.get(i).get("nums").toString());
                        list.get(i).put("nums","0");
                        kunCunService.editNum(list.get(i));
                        in=Integer.valueOf(kuncuns.toString());
                        System.out.println("在减得操作里实际操作的差值>>>>>"+JSONObject.toJSONString(in));
                    }else {
                        System.out.println("在加得操作里实际操作的差值>>>>>"+JSONObject.toJSONString(in));
                        list.get(i).put("nums",Integer.valueOf(list.get(i).get("nums").toString())-Integer.valueOf(in));
                        kunCunService.editNum(list.get(i));
                    }
                }
            }else {//这个地方加库存
                if(list.size()>0){
                    PageData pageData = list.get(list.size()-1);
                    pageData.put("nums",Integer.valueOf(pageData.get("nums").toString()) + Integer.valueOf(cc));
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
