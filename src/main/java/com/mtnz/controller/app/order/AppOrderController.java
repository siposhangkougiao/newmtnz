package com.mtnz.controller.app.order;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mtnz.controller.app.order.model.OrderGift;
import com.mtnz.controller.app.preorder.model.PreOrder;
import com.mtnz.controller.base.BaseController;
import com.mtnz.controller.base.Result;
import com.mtnz.controller.base.ServiceException;
import com.mtnz.entity.Page;
import com.mtnz.service.system.agency.AgencyService;
import com.mtnz.service.system.already.AlreadyService;
import com.mtnz.service.system.balance.BalanceService;
import com.mtnz.service.system.customer.CustomerService;
import com.mtnz.service.system.integral.IntegralService;
import com.mtnz.service.system.order_info.OrderGiftService;
import com.mtnz.service.system.order_info.OrderInfoService;
import com.mtnz.service.system.order_kuncun.OrderKuncunService;
import com.mtnz.service.system.order_pro.OrderProService;
import com.mtnz.service.system.preorder.PreOrderService;
import com.mtnz.service.system.product.KunCunService;
import com.mtnz.service.system.product.ProductService;
import com.mtnz.service.system.returns.ReturnOrderInfoService;
import com.mtnz.service.system.store.MyStoreService;
import com.mtnz.service.system.supplier.SupplierOrderProService;
import com.mtnz.service.system.sys_app_user.SysAppUserService;
import com.mtnz.util.*;
import javafx.application.Application;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
    Created by xxj on 2018\3\21 0021.
 */
@Controller
@RequestMapping(value = "app/order")
public class AppOrderController extends BaseController{

    @Resource(name = "orderInfoService")
    private OrderInfoService orderInfoService;
    @Resource(name = "orderProService")
    private OrderProService orderProService;
    @Resource(name = "customerService")
    private CustomerService customerService;
    @Resource(name="alreadyService")
    private AlreadyService alreadyService;
    @Resource(name = "sysAppUserService")
    private SysAppUserService sysAppUserService;
    @Resource(name = "agencyService")
    private AgencyService agencyService;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "kunCunService")
    private KunCunService kunCunService;
    @Resource(name = "orderKuncunService")
    private OrderKuncunService orderKuncunService;
    @Resource(name = "returnOrderInfoService")
    private ReturnOrderInfoService returnOrderInfoService;
    @Resource(name = "supplierOrderProService")
    private SupplierOrderProService supplierOrderProService;
    @Resource(name = "integralService")
    private IntegralService integralService;
    @Resource(name = "balanceService")
    private BalanceService balanceService;
    @Resource(name = "orderGiftService")
    private OrderGiftService orderGiftService;
    @Resource
    private PreOrderService preOrderService;

    @RequestMapping(value = "/updateOrderInfo",method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseBody
    public Result updateOrderInfo(Long order_info_id){

        Result result = new Result(0,"成功");
        try {
            orderInfoService.updateOrderInfo(order_info_id);
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
     * 查询销售单详情
     * @param order_info_id 订单ID
     * @return
     */
    @RequestMapping(value = "findOrderInfoById",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findOrderInfoById(String order_info_id){
        logBefore(logger,"查询订单详情");
        PageData pd=this.getPageData();
        if(order_info_id==null||order_info_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try{
                PageData pd_o=orderInfoService.findById(pd);
                System.out.println(">>>>>"+JSONObject.toJSONString(pd_o));
                //PageData pageData = integralService.findIntegralDetailByOrderId(pd);
                List<PageData> listdata =  integralService.findIntegralDetailListByOrderId(pd);
                BigDecimal aa = new BigDecimal(0);
                if(listdata!=null&&listdata.size()>0){
                    for (int i = 0; i < listdata.size(); i++) {
                        if(Integer.valueOf(listdata.get(i).get("status").toString())==1){
                            aa = aa.subtract(new BigDecimal(listdata.get(i).get("integral").toString()));
                        }else {
                            aa =aa.add(new BigDecimal(listdata.get(i).get("integral").toString()));
                        }
                    }
                }
                pd_o.put("integral",aa);
                List<PageData> gifts = orderGiftService.findGiftByOrderInfoId(pd_o);
                pd_o.put("gifts",gifts);
                List<PageData> list=orderProService.findList(pd_o);
                //查询用户预付款信息
                pd_o.put("user_id",pd_o.get("customer_id"));
                PageData user_balance = balanceService.findUserbalanceByUserId(pd_o);
                PageData order_balance = balanceService.findBalanceDetailByOrderId(pd_o);
                Integer isreturn = preOrderService.selectisreturn(pd_o);
                /*if(order_balance!=null){
                    pd_o.put("discount_money",new BigDecimal(pd_o.get("discount_money").toString())
                            .subtract(new BigDecimal(order_balance.get("balance").toString())));
                }*/
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("order",pd_o);
                pd.put("list",list);
                if(user_balance!=null){
                    pd.put("all_balance",user_balance.get("balance"));
                }else {
                    pd.put("all_balance",new BigDecimal(0));
                }
                if(order_balance!=null){
                    pd.put("order_balance",order_balance.get("balance"));
                }else {
                    pd.put("order_balance",new BigDecimal(0));
                }
                if(isreturn>0){
                    pd.put("is_return",1);
                    PreOrder preOrder = preOrderService.selectPreOrderByOrderInfo(pd_o);
                    if(preOrder!=null){
                        pd.put("pre_price",preOrder.getPrice());
                    }else {
                        pd.put("pre_price",new BigDecimal(0));
                    }

                }else {
                    pd.put("is_return",0);
                    pd.put("pre_price",new BigDecimal(0));
                }
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
                logger.error(e.getMessage());
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
     * 查询客户的订单详情
     * @param customer_id   客户ID
     * @param pageNum   页码
     * @return
     */
    @RequestMapping(value = "findCustomerOrder",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findCustomerOrder(String customer_id,String pageNum){
        logBefore(logger,"查询客户的订单");
        PageData pd=this.getPageData();
        Page page=new Page();
        java.text.DecimalFormat df = new java.text.DecimalFormat("########.0");
        if(customer_id==null||customer_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            String message="正确返回数据!";
            try{
                if (pageNum == null || pageNum.length() == 0) {
                    pageNum = "1";
                }
                pd.put("return_goods","0");
                page.setPd(pd);
                page.setShowCount(10);
                page.setCurrentPage(Integer.parseInt(pageNum));
                List<PageData> list=orderInfoService.customerlistPage(page);
                for (int i = 0, len = list.size(); i < len; i++) {
                    List<PageData> list_pro=orderProService.findList(list.get(i));
                    list.get(i).put("pro",list_pro);
                    PageData pageData = integralService.findIntegralDetailByOrderId(list.get(i));
                    if(pageData!=null){
                        list.get(i).put("integral",pageData.get("integral"));
                    }
                }
                PageData pd_c=customerService.findById(pd);
                PageData pd_o=orderInfoService.findSumMoney(pd);
                /*if(pd_o!=null&&!"0.0".equals(pd_o.get("money").toString())){
                    pd_c.put("money",df.format(pd_o.get("money")));
                }else{
                    pd_c.put("money","0.0");
                }*/
                if(pd_o!=null&&new BigDecimal(pd_o.get("money").toString()).compareTo(new BigDecimal(0))>0){
                    pd_c.put("money",new BigDecimal(pd_o.get("money").toString()).setScale(2,BigDecimal.ROUND_HALF_UP));
                }else {
                    pd_c.put("money",new BigDecimal(0));
                }
                Map<String, Object> map = new HashedMap();
                if (page.getCurrentPage() == Integer.parseInt(pageNum)) {
                    map.put("data",list);
                }else{
                    map.put("message",message);
                    map.put("data",new ArrayList());
                }
                PageData pdx = new PageData();
                pdx.put("customer_id",customer_id);
                PageData pageData = integralService.findUserIntegralByUserid(pdx);
                if(pageData!=null){
                    pd_c.put("integral",pageData.get("remain_integral"));
                }else {
                    pd_c.put("integral",new BigDecimal(0));
                }
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message",message);
                pd.put("pageTotal",String.valueOf(page.getTotalPage()));
                pd.put("data",pd_c);
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
     * 添加订单
     * @param name 客户姓名
     * @param phone 客户电话
     * @param status 状态（0实收,1欠款）
     * @param total_money  总价
     * @param money 实收
     * @param discount_money    优惠（没有优惠传0）
     * @param owe_money 余欠(不欠传0)
     * @param data  商品数据源（product_name:商品名字:product_price:商品单价:num:
     *              数量:total:商品总价:norms1:规格1:norms2:规格2:norms3:规格3,purchase_price:进货价格）
     * @param  customer_id 客户ID(没有传空字符串)
     * @param  store_id     店面ID
     * @param medication_date 下次用药时间
     * @param remarks 备注
     * @param uid 用户ID
     * @return
     */
   @RequestMapping(value = "saveOrder",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveOrder(String name,String phone,String status,
                             String total_money,String money,String discount_money,
                            String owe_money,String data,String customer_id,String store_id,
                            String medication_date,String remarks,String uid,String open_bill){
        logBefore(logger,"销售开单");
       java.text.DecimalFormat df = new java.text.DecimalFormat("########.0");
        PageData pd=this.getPageData();

            if(name==null||name.length()==0||phone==null||phone.length()==0||status==null||status.length()==0){
                pd.clear();
                pd.put("code","2");
                pd.put("message","缺少参数");
            }else{
                try{
                    PageData pd_customer=new PageData();
                    String statuss="0";
                    //判断客户存在不存在
                    if(customer_id.length()==0){
                        pd_customer=customerService.findNameCustomer(pd);
                        if(pd_customer!=null){
                            customer_id=pd_customer.get("customer_id").toString();
                        }else{
                            pd_customer=new PageData();
                            pd_customer.put("name",name);
                            pd_customer.put("phone",phone);
                            pd_customer.put("address","");
                            pd_customer.put("crop","");
                            pd_customer.put("area","");
                            pd_customer.put("input_date", DateUtil.getTime());
                            pd_customer.put("billing_date","");
                            pd_customer.put("owe","0");
                            pd_customer.put("status","0");
                            pd_customer.put("store_id",store_id);
                            pd_customer.put("uid",uid);
                            pd_customer.put("province","");
                            pd_customer.put("city","");
                            pd_customer.put("county","");
                            pd_customer.put("street","");
                            customerService.save(pd_customer);
                            customer_id=pd_customer.get("customer_id").toString();
                            statuss="1";
                        }
                    }else{
                        pd_customer=customerService.findById(pd);
                    }
                    Date now = new Date();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    String s = String.valueOf((int) ((Math.random() * 9 + 1) * 100));
                    // 订单号
                    String no = s + sdf1.format(now).substring(2);
                    PageData pd_o=new PageData();
                    pd_o.put("store_id",store_id);
                    pd_o.put("customer_id",customer_id);
                    pd_o.put("name",name);
                    pd_o.put("phone",phone);
                    pd_o.put("address","");
                    pd_o.put("money",money);
                    pd_o.put("date",DateUtil.getTime());
                    pd_o.put("status",status);
                    pd_o.put("owe_money",owe_money);
                    pd_o.put("total_money",total_money);
                    pd_o.put("discount_money",discount_money);
                    pd_o.put("medication_date",medication_date);
                    pd_o.put("remarks",remarks);
                    pd_o.put("order_number",no);
                    pd_o.put("return_date","");
                    if(open_bill!=null&&open_bill.length()!=0){
                        pd_o.put("open_bill",open_bill);
                    }else{
                        pd_o.put("open_bill","");
                    }
                    orderInfoService.save(pd_o);
                    data = DateUtil.delHTMLTag(data);
                    data = data.replace("\r", "");
                    data = data.replace("\n", "");
                    data = data.replace("\\", "");
                    Gson gson = new Gson();
                    List<PageData> list = gson.fromJson(data, new TypeToken<List<PageData>>() {
                    }.getType());   //获取订单商品列表
                    PageData pd_cc=new PageData();
                    pd_cc.put("customer_id",customer_id);
                    pd_cc.put("billing_date",pd_o.getString("date"));
                    customerService.updateBillingDate(pd_cc);
                    for(int i=0;i<list.size();i++){
                        String order_kuncun="";
                        if(list.get(i).get("product_id")!=null){
                            list.get(i).put("order_info_id",pd_o.get("order_info_id").toString());
                            list.get(i).put("orde_by","1");
                            orderProService.save(list.get(i));
                            double product_id=Double.valueOf(list.get(i).get("product_id").toString());
                            int product_ids=(int)product_id;
                            list.get(i).put("product_id",product_ids);
                            productService.editNum(list.get(i));

                            PageData pd_pp=productService.findById(list.get(i));
                            pd_pp.put("order_pro_id",list.get(i).get("order_pro_id").toString());
                            pd_pp.put("product_price",pd_pp.get("product_price").toString());
                            if(pd_pp!=null&&!"0".equals(pd_pp.get("purchase_price").toString())){
                                pd_pp.put("purchase_price",pd_pp.getString("purchase_price"));
                            }else{
                                pd_pp.put("purchase_price",list.get(i).get("product_price").toString());
                            }
                            pd_pp.put("kuncun_id","0");
                            pd_pp.put("store_id",store_id);
                            pd_pp.put("order_info_id",pd_o.get("order_info_id").toString());
                            pd_pp.put("num",list.get(i).get("num").toString());
                            orderKuncunService.save(pd_pp);
                            /*List<PageData> lists=kunCunService.findList(list.get(i));
                            String kucun=list.get(i).get("num").toString();
                            for (int j=0;j<lists.size();j++){
                                int in=(new Double(kucun)).intValue();
                                if(in>Integer.valueOf(lists.get(j).get("num").toString())){
                                    lists.get(j).put("num","0");
                                    lists.get(j).put("money","0");
                                    kunCunService.editKuncun(lists.get(j));
                                    Integer kuncuns=Integer.valueOf(in)-Integer.valueOf(lists.get(j).get("num").toString());
                                    kucun=kuncuns.toString();
                                    order_kuncun=lists.get(j).get("kuncun_id")+","+lists.get(j).get("num").toString()+"-";
                                }else{
                                    lists.get(j).put("num",Integer.valueOf(lists.get(j).get("num").toString())-Integer.valueOf(in));
                                    double moneys=Double.valueOf(lists.get(j).getString("purchase_price"))* Double.valueOf(in);
                                    lists.get(j).put("money",Double.valueOf(lists.get(j).getString("money"))-moneys);
                                    kunCunService.editKuncun(lists.get(j));
                                    order_kuncun=lists.get(j).get("kuncun_id")+","+lists.get(j).get("num").toString();
                                    continue;
                                }
                            }*/
                            list.get(i).put("order_kuncun",order_kuncun);
                        }
                    }
                    //orderProService.batchSave(list,pd_o.get("order_info_id").toString());
                    //kunCunService.batchSavess(list,store_id,DateUtil.getTime(),"0",customer_id,pd_o.get("order_info_id").toString(),"1");
                    kunCunService.batchSavess(list,store_id,DateUtil.getTime(),"0",customer_id,pd_o.get("order_info_id").toString(),"1",pd_o.get("order_info_id").toString());
                    Double owe=0.0;
                    if("1".equals(status)){
                        owe=Double.valueOf(pd_customer.get("owe").toString())+Double.valueOf(owe_money);
                        //pd_customer.put("owe",owe.toString());
                        pd_customer.put("owe",df.format(owe));
                        customerService.updateOwe(pd_customer);
                    }else{
                        owe=Double.valueOf(pd_customer.get("owe").toString());
                    }
                    if(medication_date!=""){
                        String[] moeication=medication_date.split(" ");
                        PageData pd_c=sysAppUserService.findById(pd);
                        pd.put("agency",moeication[0]+name+"用药.");
                        pd.put("month",moeication[0]);
                        pd.put("hour",moeication[1].split(":")[0]);
                        pd.put("name",name);
                        pd.put("openid",pd_c.getString("openid"));
                        pd.put("date",DateUtil.getTime());
                        pd.put("fstatus","0");
                        pd.put("customer_id",customer_id);
                        agencyService.save(pd);
                    }
                    pd.clear();
                    pd.put("code","1");
                    pd.put("message","正确返回数据!");
                    pd.put("status",statuss);
                    pd.put("owe",df.format(owe));
                    pd.put("customer_id",customer_id);
                    pd.put("order_number",no);
                }catch (Exception e){
                    logBefore(logger,e+"错误:customer_id="+customer_id+"data");
                    e.printStackTrace();
                    pd.clear();
                    pd.put("code","2");
                    pd.put("message","程序出错,请联系管理员!");
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
     * @param customer_id   客户ID
     * @param pageNum 页码
     * @return
     */
    @RequestMapping(value = "findOweOrder",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findOweOrder(String customer_id,String pageNum){
       logBefore(logger,"查询客户欠款订单");
       PageData pd=this.getPageData();
       Page page=new Page();
       String owe_money="0";
       String already_money="0";
       if(customer_id==null||customer_id.length()==0){
           pd.clear();
           pd.put("code","2");
           pd.put("message","缺少参数");
       }else{
           String message="正确返回数据";
            try{
                if (pageNum == null || pageNum.length() == 0) {
                    pageNum = "1";
                }
                pd.put("return_goods","0");
                page.setPd(pd);
                page.setShowCount(10);
                page.setCurrentPage(Integer.parseInt(pageNum));
                PageData pd_d=orderInfoService.findSumOweMoney(pd);//欠款金额
                PageData pd_dd=alreadyService.findSumMoney(pd);//已还金额
                if(pd_d!=null){
                    owe_money=pd_d.get("money").toString();
                }
                if(pd_dd!=null){
                    already_money=pd_dd.get("moeny").toString();
                }
                List<PageData> list=orderInfoService.owelistPage(page);
                for(int i=0;i<list.size();i++){
                    List<PageData> list_pro=orderProService.findList(list.get(i));
                    list.get(i).put("order_pro",list_pro);
                }
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
                pd.put("owe_money",owe_money);
                pd.put("already_money",already_money);
                pd.put("pageTotal",String.valueOf(page.getTotalPage()));
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
     * @param store_id 店铺ID
     * @param pageNum 页码
     * @param status 排序状态(0倒序1正询)
     * @return
     */
    @RequestMapping(value = "findOrderInfo",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findOrderInfo(String store_id,String pageNum,String status,
                                String startTime,String endTime,String remarks,String phone,Long customer_id){
       logBefore(logger,"查询账单");
       PageData pd=this.getPageData();
       Page page=new Page();
        if(store_id==null||store_id.length()==0||status.length()==0||status==null){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            String message="正确返回数据";
            try {
                if(pageNum.length()==0||pageNum==null){
                    pageNum="1";
                }
                pd.put("return_goods","0");
                page.setPd(pd);
                page.setShowCount(10);
                page.setCurrentPage(Integer.parseInt(pageNum));
                List<PageData> list=orderInfoService.datalistPage(page);
                for(int i=0;i<list.size();i++){

                    List<PageData> list_pro=orderProService.findList(list.get(i));
                    list.get(i).put("order_pro",list_pro);
                    PageData pbalance = balanceService.findBalanceDetailByOrderId(list.get(i));
                    if(pbalance!=null){
                        list.get(i).put("order_balance",pbalance.get("balance"));
                    }else {
                        list.get(i).put("order_balance",0);
                    }
                }
                PageData pdMoney=orderInfoService.findSumTotalMoney(pd);
                java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
                String totalMoney="0.0";
                if(pdMoney!=null&&!pdMoney.get("money").toString().equals("0.0")){
                    totalMoney=df.format(pdMoney.get("money"));
                }
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
                pd.put("count",String.valueOf(page.getShowCount()));
                pd.put("totalMoney",totalMoney);
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
     * @param store_id 店铺ID
     * @param name 模糊查询条件
     * @return
     */
    @RequestMapping(value = "findlikeOrderInfo",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findlikeOrderInfo(String store_id,String name,Integer pageNumber){
        logBefore(logger,"模糊查询账单");
        Integer pageSize = 10;
        PageData pd=this.getPageData();
        if(store_id==null||store_id.length()==0||name==null||name.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try {
                pd.put("return_goods","0");
                List<PageData> list=orderInfoService.findLikeOrderInfo(pd);
                for(int i=0;i<list.size();i++){
                    if(name!=null&&!name.equals("")){
                        list.get(i).put("findname",name);
                    }
                    List<PageData> list_pro=orderProService.findList(list.get(i));
                    list.get(i).put("order_pro",list_pro);
                }
                Map<String, Object> map = new HashedMap();
                map.put("data",list);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message","正确返回数据");
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
     * @param store_id 店铺ID
     * @param pageNum 页码
     * @param year 年
     * @param month 月
     * @return
     */
    @RequestMapping(value = "findOrderRanking",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findOrderRanking(String store_id,String pageNum,String year,String month){
        logBefore(logger,"查询账单排名");
        PageData pd=this.getPageData();
        if(store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            String message="正确返回数据";
            try {
                if(pageNum==null||pageNum.length()==0){
                    pageNum="1";
                }
                Integer SHU1 = Integer.valueOf(pageNum) * 10;
                pd.put("SHU1", SHU1 - 10);
                pd.put("return_goods","0");
                List<PageData> list=orderInfoService.RankinglistPage(pd);
                PageData pd_c=orderInfoService.findkingcount(pd);
                Integer pageTotal;
                if(Integer.valueOf(pd_c.get("count").toString()) % 10 == 0){
                    pageTotal = Integer.valueOf(pd_c.get("count").toString()) / 10;
                }else{
                    pageTotal = Integer.valueOf(pd_c.get("count").toString()) / 10 + 1;
                }
                Map<String, Object> map = new HashedMap();
                map.put("object",list);
                pd.clear();
                pd.put("object",map);
                pd.put("code","1");
                pd.put("message",message);
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
     * @param store_id 点ID
     * @param status 1为日  2为月
     * @param pageNum 页码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping(value = "findAnalysis",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findAnalysis(String store_id,String status,String pageNum,String startTime,String endTime,String date){
        logBefore(logger,"经营分析");
        PageData pd=this.getPageData();
        Page page=new Page();
        if(date == null || "".equals(date)){
            Date date1 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(date1);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if(store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                String message="正确返回数据!";
                Map<String,Object> map=DateUtil.ThisMonth();
                if("1".equals(status)){
                    pd.put("status",4);
                    pd.put("date",date);
                }else if("2".equals(status)){
                    //获取当前日期
                    Calendar cal_1=Calendar.getInstance();
                    cal_1.set(Calendar.DAY_OF_MONTH, 1);
                    String firstDay = format.format(cal_1.getTime());
                    System.err.println(firstDay);
                    pd.put("date",firstDay);
                }else{
                    if(startTime!=null&&startTime.length()<10){
                        pd.put("startTime",startTime+="-01");
                    }
                    if(endTime!=null&&endTime.length()<10){
                        pd.put("endTime",endTime+="-31");
                    }
                }
                pd.put("return_goods","0");
                page.setPd(pd);
                page.setShowCount(10);
                page.setCurrentPage(Integer.parseInt(pageNum));
                List<PageData> list=orderInfoService.AnalysislistPage(page);
                BigDecimal bcv = new BigDecimal(0);
                for(int i=0;i<list.size();i++){
                    List<PageData> list_pro=orderProService.findList(list.get(i));
                    list.get(i).put("order_pro",list_pro);

                    //查询赠品的成本
                    PageData pgift = new PageData();
                    pgift.put("start", MyTimesUtil.getStartTime());
                    pgift.put("end", MyTimesUtil.getEndTime());
                    pgift.put("order_info_id",list.get(i).get("order_info_id").toString());
                    PageData gites = orderGiftService.findtodaySum(pgift);
                    if(gites!=null){
                        bcv =bcv.add(new BigDecimal(gites.get("totalprice").toString()));
                    }
                }
                String total_money="0.0";
                String money="0.0";
                String owe_money="0.0";
                String discount_money="0.0";//优惠的金额
                String return_money="0.0";
                java.text.DecimalFormat df = new java.text.DecimalFormat("########.0000");
                PageData pd_money=orderInfoService.findSumAnalysisMoney(pd);
                if(pd_money!=null){
                    if(pd_money.get("total_money").toString()!=null&&!"0.0".equals(pd_money.get("total_money").toString())){
                        total_money=df.format(pd_money.get("total_money"));
                    }
                    if(pd_money.get("money").toString()!=null&&!"0.0".equals(pd_money.get("money").toString())){
                        money=df.format(pd_money.get("money"));
                    }
                    if(pd_money.get("owe_money").toString()!=null&&!"0.0".equals(pd_money.get("owe_money").toString())){
                        owe_money=df.format(pd_money.get("owe_money"));
                    }
                    if(pd_money.get("discount_money").toString()!=null&&!"0.0".equals(pd_money.get("discount_money").toString())){
                        discount_money=df.format(pd_money.get("discount_money"));
                    }
                }

                PageData re_money=returnOrderInfoService.findSumMoney(pd);
                if(re_money!=null&&re_money.get("money")!=null&&!"0.0".equals(re_money.get("money").toString())){
                    return_money=df.format(re_money.get("money"));
                }
                String receivable="0.0";
                PageData pd_r=orderProService.findSumProducts(pd);
                if(pd_r!=null){
                    if(!"0.0".equals(pd_r.get("receivable").toString())){
                        receivable=df.format(pd_r.get("receivable"));
                    }
                }
                BigDecimal a = new BigDecimal(receivable);
                List<PageData> list1 = orderProService.findOrderProNowNumber(pd);
                for (int i = 0; i < list1.size(); i++) {
                    PageData pdx = new PageData();
                    pdx.put("product_id",list1.get(i).get("product_id"));
                    PageData pageData = productService.findById(pdx);
                    BigDecimal b = new BigDecimal(list1.get(i).get("now_number").toString());
                    BigDecimal c = new BigDecimal(pageData.get("norms1").toString());
                    BigDecimal d = new BigDecimal(list1.get(i).get("purchase_price").toString());
                    a = a.add(b.divide(c,4,BigDecimal.ROUND_HALF_UP).multiply(d));
                }
                a =a.add(bcv);
                receivable = String.valueOf(a);
                List<PageData> list_count=orderInfoService.findGroupCustomer(pd);
                Map<String, Object> maps = new HashedMap();
                if (page.getCurrentPage() == Integer.parseInt(pageNum)) {
                    maps.put("data",list);
                }else{
                    maps.put("message",message);
                    maps.put("data",new ArrayList());
                }
                pd.clear();
                pd.put("object",maps);
                pd.put("code","1");
                pd.put("message",message);
                pd.put("pageTotal",String.valueOf(page.getTotalPage()));
                pd.put("total_money",total_money);
                pd.put("money",money);
                pd.put("owe_money",owe_money);
                pd.put("discount_money",discount_money);
                pd.put("count",String.valueOf(list_count.size()));
                pd.put("receivable",receivable);//成本价
                pd.put("return_money",return_money);
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

    @RequestMapping(value = "ReturnGoods",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ReturnGoods(String order_info_id){
        logBefore(logger,"退货");
        PageData pd=this.getPageData();
        if(order_info_id==null||order_info_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                pd.put("return_goods","1");
                pd.put("return_date",DateUtil.getTime());
                orderInfoService.edutReturnGoods(pd);
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

    @RequestMapping(value = "findReturnGoods",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findReturnGoods(String store_id,String pageNum){
        logBefore(logger,"查询退货");
        PageData pd=this.getPageData();
        Page page=new Page();
        if(store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            String message="正确返回数据";
            try {
                if(pageNum.length()==0||pageNum==null){
                    pageNum="1";
                }
                pd.put("status","2");
                pd.put("return_goods","1");
                page.setPd(pd);
                page.setShowCount(10);
                page.setCurrentPage(Integer.parseInt(pageNum));
                List<PageData> list=orderInfoService.datalistPage(page);
                for(int i=0;i<list.size();i++){
                    List<PageData> list_pro=orderProService.findList(list.get(i));
                    list.get(i).put("order_pro",list_pro);
                }
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



    @RequestMapping(value = "findOrderUserId",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findOrderUserId(String customer_id){
        logBefore(logger,"查询客户最后一次订单");
        PageData pd=this.getPageData();
        try{
            PageData pd_o=orderInfoService.findOrderUserId(pd);
            List<PageData> list=orderProService.findList(pd_o);
            String product="";
            String date="";
            if(pd_o!=null){
                date=pd_o.getString("date");
            }
            for(int i=0;i<list.size();i++){
                product+=list.get(i).getString("product_name")+",";
            }
            if(!"".equals(product)){
                product = product.substring(0,product.length() - 1);
            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("date",date);
            pd.put("product",product);
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

    @RequestMapping(value = "findOrderUserIds",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findOrderUserIds(String customer_id){
        logBefore(logger,"查询客户最后一次订单");
        PageData pd=this.getPageData();
        try{
            String code="1";
            String message="正确返回数据!";
            PageData pd_o=orderInfoService.findOrderUserId(pd);
            List<PageData> list=new ArrayList<>();
            if(pd_o!=null){
                list=orderProService.findList(pd_o);
            }else{
                pd_o=new PageData();
                code="2";
                message="没有购买过产品!";
            }
            pd.clear();
            pd.put("code",code);
            pd.put("message",message);
            pd.put("data",pd_o);
            pd.put("list",list);
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
     * @param store_id 店Id
     * @param status 0为15天 1为某月 2为当月
     * @param nian 年
     * @param month 月
     * @return
     */
    @RequestMapping(value = "findReportAnalysis",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findReportAnalysis(String store_id,String status,String nian,String month){
        logBefore(logger,"根据时间分组查询钱数");
        PageData pd=this.getPageData();
        try{
            if("0".equals(status)){
                pd.put("startTime",DateUtil.getSpecifiedDayBefore(DateUtil.getTime(),15));
                pd.put("endTime",DateUtil.getDay());
            }else if("1".equals(status)){
                pd.put("startTime",DateUtil.getFirstDayOfMonth(Integer.valueOf(nian),Integer.valueOf(month)));
                pd.put("endTime",DateUtil.getLastDayOfMonth(Integer.valueOf(nian),Integer.valueOf(month)));
            }else if("2".equals(status)){
                pd.put("startTime",DateUtil.getFirstDayOfMonth(Integer.valueOf(nian),Integer.valueOf(month)));
                pd.put("endTime",DateUtil.getDay());
            }else if("3".equals(status)){
                pd.put("startTime",DateUtil.getYue());
                pd.put("endTime",DateUtil.getFirstDayYue(3));
            }else{
                pd.put("startTime",DateUtil.getYue());
                pd.put("endTime",DateUtil.getFirstDayNian(1));

            }
            List<PageData> list=new ArrayList<>();
            if("0".equals(status)||"1".equals(status)||"2".equals(status)){
                list=orderInfoService.findReportAnalysisWeb(pd);
            }else{
                list=orderInfoService.findReportAnalysisYueWeb(pd);
            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            for (int i = 0; i < list.size(); i++) {
                BigDecimal price = new BigDecimal(list.get(i).get("total_money").toString()).setScale(2,BigDecimal.ROUND_HALF_UP);
                list.get(i).put("total_money",String.valueOf(price));
            }
            pd.put("data",list);
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
    @RequestMapping(value = "findReportAnalysiss",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findReportAnalysiss(String store_id,String status,String startTime,String endTime){
        logBefore(logger,"根据时间分组查询钱数");
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
            String total_money="0.0";
            String money="0.0";
            String owe_money="0.0";
            String discount_money="0.0";
            String return_money="0.0";
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.0");
            PageData pd_money=orderInfoService.findSumAnalysisMoney(pd);
            if(pd_money!=null){
                if(pd_money.get("total_money").toString()!=null&&!"0.0".equals(pd_money.get("total_money").toString())){
                    total_money=df.format(pd_money.get("total_money"));
                }
                if(pd_money.get("money").toString()!=null&&!"0.0".equals(pd_money.get("money").toString())){
                    money=df.format(pd_money.get("money"));
                }
                if(pd_money.get("owe_money").toString()!=null&&!"0.0".equals(pd_money.get("owe_money").toString())){
                    owe_money=df.format(pd_money.get("owe_money"));
                }
                if(pd_money.get("discount_money").toString()!=null&&!"0.0".equals(pd_money.get("discount_money").toString())){
                    discount_money=df.format(pd_money.get("discount_money"));
                }
            }
            PageData re_money=returnOrderInfoService.findSumMoney(pd);
            if(re_money!=null&&re_money.get("money")!=null&&!"0.0".equals(re_money.get("money").toString())){
                return_money=df.format(re_money.get("money"));
            }
            String receivable="0.0";
            PageData pd_r=orderProService.findSumProduct(pd);
            if(pd_r!=null){
                if(!"0.0".equals(pd_r.get("receivable").toString())){
                    receivable=df.format(pd_r.get("receivable"));
                }
            }
            List<PageData> list=new ArrayList<>();
            if("1".equals(status)){
                list=orderInfoService.findReportAnalysis(pd);
            }else if("4".equals(status)){
                list=orderInfoService.findReportAnalysisXiaoShi(pd);
            }else{
                list=orderInfoService.findReportAnalysisYue(pd);
            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("data",list);
            pd.put("total_money",total_money);
            pd.put("money",money);
            pd.put("owe_money",owe_money);
            pd.put("discount_money",discount_money);
            pd.put("receivable",receivable);
            pd.put("return_money",return_money);
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
     * 撤销订单
     * @param order_info_id
     * @return
     */
    @RequestMapping(value = "revokesOrderInfo",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String revokesOrderInfo(String order_info_id){
        logBefore(logger,"撤销订单");
        PageData pd=this.getPageData();
        ObjectMapper mapper=new ObjectMapper();
        try{
            //查询主订单信息
            PageData pd_o=orderInfoService.findById(pd);
            //处理积分的问题

            Integer count = integralService.findIntegralDetailCountByOrderId(pd_o);//查询订单是否产生了积分
            if(count!=null&&count>0){
                pd_o.put("undo",2);
                integralService.editIntegralDetailByOrderId(pd_o);//撤销积分订单

                PageData pageData = integralService.findIntegralDetailByOrderId(pd_o);//查询出来积分订单详情
                PageData pdx = new PageData();
                pdx.put("user_id",pageData.get("user_id"));
                pdx.put("integral",pageData.get("integral"));
                integralService.editIntegralUser(pdx);//把积分减去
            }
            //处理赠品问题
            PageData giftPage = new PageData();
            giftPage.put("order_info_id",order_info_id);
            giftPage.put("is_back",1);
            orderGiftService.editGiftBack(giftPage);

            //处理账户余额问题
            PageData orderbalance =balanceService.findBalanceDetailByOrderId(pd);
            if(orderbalance!=null){
                PageData pageup = new PageData();
                pageup.put("user_id",orderbalance.get("user_id"));
                pageup.put("balance",orderbalance.get("balance"));
                balanceService.editBalanceByUserIdUp(pageup);
                orderbalance.put("is_pass",1);
                balanceService.editBalanceDetailIsPassByOrderOId(orderbalance);
            }

            /*if(!pd_o.getString("date").split(" ")[0].equals(DateUtil.getDay())){
                pd.clear();
                pd.put("code","2");
                pd.put("message","订单不是今天订单不可撤销!");
                return mapper.writeValueAsString(pd);
            }*/
            //查询当时开单时候的信息
            List<PageData> returnOrder=kunCunService.findCheXiao(pd);
            if(returnOrder!=null&&returnOrder.size()!=0){
                pd.clear();
                pd.put("code","2");
                pd.put("message","该订单有退货,不可撤销!");
                return mapper.writeValueAsString(pd);
            }
            //修改主订单信息
            orderInfoService.editrevokes(pd);
            //修改库存信息
            orderKuncunService.editrevokes(pd);
            //修改oderpro表信息
            orderProService.editRevokes(pd);
            //修改用户欠款金额
            if(pd_o.get("status").toString().equals("1")){
                java.text.DecimalFormat df = new java.text.DecimalFormat("########.0");
                //查询自己的欠款
                PageData byId = customerService.findById(pd_o);
                Double owe=Double.valueOf(byId.get("owe").toString())-Double.valueOf(pd_o.get("owe_money").toString());
                PageData pd_cc=new PageData();
                pd_cc.put("customer_id",pd_o.get("customer_id").toString());
                pd_cc.put("owe",df.format(owe));
                customerService.updateOwe(pd_cc);
            }
            List<PageData> list=orderProService.findList(pd);
            for(int i=0;i<list.size();i++){
                List<PageData> lists=orderKuncunService.findOrderIdList(list.get(i));
                for(int j=0;j<lists.size();j++){
                    if(lists.get(j).get("now_number")==null){
                        lists.get(j).put("now_number",new BigDecimal(0));
                    }
                    kunCunService.editJiaNums(lists.get(j));
                }
                if(list.get(i).get("isThreeSales").equals(0)){ //如果是按三级单位购买的
                    productService.editJiaNums(list.get(i));//更新商品库存信息
                }else {
                    BigDecimal bigDecimal = new BigDecimal(list.get(i).get("num").toString()).multiply(new BigDecimal(list.get(i).get("norms4").toString()));
                    list.get(i).put("num",bigDecimal);
                    productService.editJiaNums(list.get(i));//更新商品库存信息
                }

            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
        }catch (Exception e) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
            e.printStackTrace();
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
     * 添加订单
     * @param name 客户姓名
     * @param phone 客户电话
     * @param status 状态（0实收,1欠款）
     * @param total_money  总价
     * @param money 实收
     * @param discount_money    优惠（没有优惠传0）
     * @param owe_money 余欠(不欠传0)
     * @param data  商品数据源（product_name:商品名字:product_price:商品单价:num:
     *              数量:total:商品总价:norms1:规格1:norms2:规格2:norms3:规格3,purchase_price:进货价格）
     * @param  customer_id 客户ID(没有传空字符串)
     * @param  store_id     店面ID
     * @param medication_date 下次用药时间
     * @param remarks 备注
     * @param uid 用户IDxxx
     * @return isli是否拆拆袋儿销售
     * @return integral 赠送的积分
     * @return open_user 开单人
     */
    @RequestMapping(value = "saveOrders",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveOrders(String name, String phone, String status,
                             String total_money, String money, String discount_money,
                             String owe_money, String data, String customer_id, String store_id,
                             String medication_date, String remarks, String uid, String open_bill, String date, Integer isli
                , BigDecimal integral, Long open_user, String remark, BigDecimal balance,String orderGifts,Integer lose,Integer isfast,Integer isSend){
        PageData pd=new PageData();
        String str="";
        try {
            List<OrderGift> orderGift = JSONObject.parseArray(orderGifts,OrderGift.class);
            /*String myJson=gson.toJson(data.trim());
            myJson = DateUtil.delHTMLTag(data);
            myJson = myJson.replace("\r", "");
            myJson = myJson.replace("\n", "");
            myJson = myJson.replace("\\", "");
            logBefore(logger,"再次处理过的参数"+myJson);*/
            System.out.println(data);
            if(date == null || "".equals(date)){
                date = DateUtil.getTime();
            }
            str=orderKuncunService.saveOrder(name,phone,status,total_money,money,discount_money,
                    owe_money,data.trim(),customer_id,store_id,
                    medication_date,remarks,uid,open_bill,date,isli,integral,open_user,remark,balance,orderGift,lose,isfast,isSend);
            return str;
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            logBefore(logger,"订单出错,"+e);
            logBefore(logger,"订单出错,"+data);
            e.printStackTrace();
            ObjectMapper mapper=new ObjectMapper();
            try {
                str= mapper.writeValueAsString(pd);
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
            return str;
        }

    }

    /**
     *
     * @param product_id 查询商品销售详情
     * @return
     */
    @RequestMapping(value = "findProductSaleDetails",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findProductSaleDetails(String product_id){
        logBefore(logger,"查询商品销售详情");
        PageData pd=this.getPageData();
        java.text.DecimalFormat df = new java.text.DecimalFormat("########.0");
        try{
            PageData pd_p=productService.findById(pd);
            String sale_money="0.0";
            String purchase_money="0.0";
            PageData pd_s=orderKuncunService.findSumProduct(pd);
            if(pd_s!=null){
                if(pd_s.get("money")!=null&&!"0.0".equals(pd_s.get("money").toString())){
                    sale_money=df.format(pd_s.get("money"));
                }
                if(pd_s.get("money1")!=null&&!"0.0".equals(pd_s.get("money1").toString())){
                    purchase_money=df.format(pd_s.get("money1"));
                }
            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("sale_money",sale_money);
            pd.put("purchase_money",purchase_money);
            pd.put("data",pd_p);
       }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
        String str="";
        ObjectMapper mapper=new ObjectMapper();
        try {
            str= mapper.writeValueAsString(pd);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        return str;
    }

    @RequestMapping(value = "findSupplierAnalysisId",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findSupplierAnalysisId(String product_id,String pageNum){
        logBefore(logger,"查询单个商品的销售列表");
        PageData pd=this.getPageData();
        try {
            if(pageNum==null||pageNum.length()==0){
                pageNum="1";
            }
            Integer SHU1 = Integer.valueOf(pageNum) * 10;
            pd.put("SHU1", SHU1 - 10);
            List<PageData> list=orderProService.findProductList(pd);
            PageData pd_c=orderProService.findCount(pd);
            Integer pageTotal;
            if (Integer.valueOf(pd_c.get("count").toString()) % 10 == 0) {
                pageTotal = Integer.valueOf(pd_c.get("count").toString()) / 10;
            } else {
                pageTotal = Integer.valueOf(pd_c.get("count").toString()) / 10 + 1;
            }
            pd.clear();
            pd.put("code","1");
            pd.put("data",list);
            pd.put("message","正确返回数据!");
            pd.put("pageTotal",pageTotal);
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


    @RequestMapping(value = "findExcelUtil",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void findExcelUtil(String store_id,String startTime,String endTime,HttpServletRequest request,HttpServletResponse response){
        try{
            PageData pd=this.getPageData();
            List<PageData> list=orderProService.findQuanBu(pd);
            ExcelUtil_Extend.printExcle(list,"E://ceshi.xls",request,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     *
     * @param uid ID
     * @param pageNum 页码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param name 模糊查询
     * @param store_id 不传查询全部  传就查询某个店的
     * @return
     */
    @RequestMapping(value = "findAdminRelationOrderInfo",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findAdminRelationOrderInfo(String uid,String pageNum,String startTime,
                                             String endTime,String name,String store_id){
        logBefore(logger,"查询所有订单");
        PageData pd=this.getPageData();
        Page page=new Page();
        try{
            if (pageNum == null || pageNum.length() == 0) {
                pageNum = "1";
            }
            page.setPd(pd);
            page.setShowCount(10);
            page.setCurrentPage(Integer.parseInt(pageNum));
            List<PageData> list=orderInfoService.AdminlistPage(page);
            for (int i = 0, len = list.size(); i < len; i++) {
                List<PageData> list_pro=orderProService.findList(list.get(i));
                list.get(i).put("pro",list_pro);
            }
            Map<String, Object> map = new HashedMap();
            if (page.getCurrentPage() == Integer.parseInt(pageNum)) {
                map.put("data",list);
            }else{
                map.put("message","正确返回数据!");
                map.put("data",new ArrayList());
            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("pageTotal",String.valueOf(page.getTotalPage()));
            pd.put("data",map);
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

    @RequestMapping(value = "findAdminExcelUtil",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void findAdminExcelUtil(String uid,String pageNum,String startTime,
                                   String endTime,String name,String store_id,
                                   HttpServletRequest request,HttpServletResponse response){
        try{
            PageData pd=this.getPageData();
            List<PageData> list=orderProService.findAdminQuanBu(pd);
            AdminExtend.printExcle(list,"E://ceshi.xls",request,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
