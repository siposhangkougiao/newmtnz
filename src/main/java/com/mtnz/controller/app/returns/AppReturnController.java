package com.mtnz.controller.app.returns;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mtnz.controller.base.BaseController;
import com.mtnz.entity.Page;
import com.mtnz.service.system.balance.BalanceService;
import com.mtnz.service.system.customer.CustomerService;
import com.mtnz.service.system.integral.IntegralService;
import com.mtnz.service.system.order_info.OrderGiftService;
import com.mtnz.service.system.order_info.OrderInfoService;
import com.mtnz.service.system.order_kuncun.OrderKuncunService;
import com.mtnz.service.system.order_pro.OrderProService;
import com.mtnz.service.system.product.KunCunService;
import com.mtnz.service.system.product.ProductService;
import com.mtnz.service.system.returns.ReturnOrderInfoService;
import com.mtnz.service.system.returns.ReturnOrderProService;
import com.mtnz.util.DateUtil;
import com.mtnz.util.PageData;
import com.mtnz.util.ReturnOrderExtend;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "app/return",produces = "text/html;charset=UTF-8")
public class AppReturnController extends BaseController{

    @Resource(name = "returnOrderInfoService")
    private ReturnOrderInfoService returnOrderInfoService;
    @Resource(name = "returnOrderProService")
    private ReturnOrderProService returnOrderProService;
    @Resource(name = "orderInfoService")
    private OrderInfoService orderInfoService;
    @Resource(name = "orderProService")
    private OrderProService orderProService;
    @Resource(name = "kunCunService")
    private KunCunService kunCunService;
    @Resource(name = "orderKuncunService")
    private OrderKuncunService orderKuncunService;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "integralService")
    private IntegralService integralService;
    @Resource(name = "balanceService")
    private BalanceService balanceService;
    @Resource(name = "customerService")
    private CustomerService customerService;
    @Resource(name = "orderGiftService")
    private OrderGiftService orderGiftService;


    /**
     *退货操作
     * @param order_info_id 订单ID
     * @param data (order_pro_id 订单商品ID,product_name(商品名称),product_price(商品价格)
     *             ,num(数量),total(总价),norms1(规格1),norms2(规格2),norms3(规格3),product_id(商品ID))
     * @param money 总价
     * @param store_id 店ID
     * @param remarks 备注
     * @Param       user_id 客户id
     * @return
     */
    @RequestMapping(value = "returnOrderInfos",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String returnOrderInfos(String order_info_id,String data,String money,String store_id,
                                   String remarks,String open_bill,BigDecimal integral,Long customer_id,Integer isBalance){
        logBefore(logger,"订单退货");
        PageData pd=this.getPageData();
        try{
            //处理积分的问题
            if(integral!=null){
                PageData inorder = integralService.findIntegralDetailByOrderId(pd);
                if(inorder!=null){
                    pd.put("user_id",customer_id);
                    integralService.editIntegralUser(pd);
                    PageData pdr = new PageData();
                    pdr.put("order_id",order_info_id);
                    pdr.put("status",1);
                    pdr.put("integral",integral);
                    pdr.put("user_id",customer_id);
                    pdr.put("type",1);
                    pdr.put("open_user",inorder.get("open_user"));
                    integralService.saveIntegralReturn(pdr);
                }
            }
            //处理赠品的问题
            PageData giftPage = new PageData();
            giftPage.put("order_info_id",order_info_id);
            giftPage.put("is_back",1);
            orderGiftService.editGiftBack(giftPage);
            //查询订单信息对象
            PageData pd_o=orderInfoService.findById(pd);
            if(remarks==null||remarks.length()==0){
                pd_o.put("remarks","");
            }else{
                pd_o.put("remarks",remarks);
            }
            if(open_bill==null||open_bill.length()==0){
                pd_o.put("open_bill","");
            }else{
                pd_o.put("open_bill",open_bill);
            }
            pd_o.put("total_money",money);
            Date now = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String s = String.valueOf((int) ((Math.random() * 9 + 1) * 100));
            // 订单号
            String no = s + sdf1.format(now).substring(2);
            pd_o.put("order_number",no);
            pd_o.put("money",money);
            pd_o.put("return_date",DateUtil.getTime());
            pd_o.put("status","0");
            //添加退货信息对象
            returnOrderInfoService.save(pd_o);
            //开始处理退款冲抵问题
            PageData p3 = new PageData();
            p3.put("customer_id",customer_id);
            PageData puser = customerService.findById(p3);//查询客户的欠款金额
            PageData pd_s = pd_o;
            //比例
            BigDecimal bili = new BigDecimal(money).divide(new BigDecimal(pd_s.get("total_money").toString()),4,BigDecimal.ROUND_HALF_UP);
            //需要减去的欠款
            BigDecimal qiankuan = new BigDecimal(pd_s.get("owe_money").toString()).multiply(bili);
            //把钱先给用户减去
            if(qiankuan.compareTo(new BigDecimal(0))==1){
                PageData p4 = new PageData();
                p4.put("customer_id",customer_id);
                p4.put("owe",new BigDecimal(puser.get("owe").toString()).subtract(qiankuan));
                customerService.editOwnByCustomerId(p4);//减去欠款金额
                //记录退款时的账户变动
                PageData pp4 = new PageData();
                pp4.put("return_order_info_id",pd_o.get("return_order_info_id"));
                pp4.put("number",qiankuan);
                pp4.put("type",1);
                pp4.put("customer_id",customer_id);
                balanceService.saveBalanceReturn(pp4);
            }
            //要退的付的钱
            BigDecimal tuiqian = new BigDecimal(pd_s.get("total_money").toString()).subtract(new BigDecimal(pd_s.get("owe_money").toString())).multiply(bili);
            //要退的账户余额
            BigDecimal yue = new BigDecimal(0);
            PageData p1 = new PageData();
            p1.put("order_info_id",order_info_id);
            PageData badetail = balanceService.findBalanceDetailByOrderId(p1);//查询下单时用的账户余额
            if(badetail!=null){
                yue = new BigDecimal(badetail.get("balance").toString()).multiply(bili);
            }
            //客户现在欠的钱
            BigDecimal own = new BigDecimal(puser.get("owe").toString()).subtract(qiankuan);
            BigDecimal be_own = own;
            BigDecimal be_yue = yue;
            BigDecimal be_tuiqian = tuiqian;
            if(yue.compareTo(new BigDecimal(0))>0){//判断是否有余额可扣
                if(own.compareTo(yue)>-1){//大于等于
                    own = own.subtract(yue);
                    yue = new BigDecimal(0);
                }else {
                    yue = yue.subtract(own);
                    own = new BigDecimal(0);
                }
            }
            //如果还有欠款要还
            if(own.compareTo(new BigDecimal(0))==1){
                if(own.compareTo(tuiqian)>-1){
                    own = own.subtract(tuiqian);
                    tuiqian = new BigDecimal(0);
                }else {
                    tuiqian = tuiqian.subtract(own);
                    own = new BigDecimal(0);
                }
            }
            PageData last_p4 = new PageData();
            last_p4.put("customer_id",customer_id);
            last_p4.put("owe",own);
            customerService.editOwnByCustomerId(last_p4);//减去欠款金额

            PageData pc6 = new PageData();
            pc6.put("return_order_info_id",pd_o.get("return_order_info_id"));
            pc6.put("number",be_own.subtract(own));
            pc6.put("type",1);
            pc6.put("customer_id",customer_id);
            balanceService.saveBalanceReturn(pc6);//记录退款是的账户变动

            if(yue.compareTo(new BigDecimal(0))==1){
                PageData p7 = new PageData();
                p7.put("user_id",customer_id);
                p7.put("balance",yue);
                balanceService.editBalanceByUserIdUp(p7);

                PageData pp6 = new PageData();
                pp6.put("return_order_info_id",pd_o.get("return_order_info_id"));
                pp6.put("number",be_yue.subtract(yue));
                pp6.put("type",2);
                pp6.put("customer_id",customer_id);
                balanceService.saveBalanceReturn(pp6);//记录退款是的账户变动
            }
            if(isBalance!=null&&isBalance==1){//选择退回余额
                PageData p8 = new PageData();
                p8.put("user_id",customer_id);
                p8.put("balance",tuiqian);
                balanceService.editBalanceByUserIdUp(p8);

                //记录退单明细
                PageData pp7 = new PageData();
                pp7.put("return_order_info_id",pd_o.get("return_order_info_id"));
                pp7.put("number",tuiqian);
                pp7.put("type",2);
                pp7.put("customer_id",customer_id);
                balanceService.saveBalanceReturn(pp7);
            }

            data = DateUtil.delHTMLTag(data);
            data = data.replace("\r", "");
            data = data.replace("\n", "");
            data = data.replace("\\", "");
            data = data.replace(" ", "XINg");
            Gson gson = new Gson();
            List<PageData> list = gson.fromJson(data, new TypeToken<List<PageData>>() {
            }.getType());   //获取订单商品列表
            for(int i=0;i<list.size();i++){

                //list.get(i).put("purchase_price",list.get(i).get("product_price").toString());
                if(new BigDecimal(list.get(i).get("isli").toString()).compareTo(new BigDecimal(0))>0){// isli 0是没有拆单  1拆单
                    //原来退货的数量
                    BigDecimal backnumber = new BigDecimal(list.get(i).get("num").toString());
                    BigDecimal kucun = new BigDecimal(list.get(i).get("num").toString());//需要退货的数量
                    list.get(i).put("return_order_info_id",pd_o.get("return_order_info_id").toString());
                    list.get(i).put("order_info_id",order_info_id);
                    list.get(i).put("order_pro_id",new Double(list.get(i).get("order_pro_id").toString()).intValue());
                    list.get(i).put("product_id",new Double(list.get(i).get("product_id").toString()).intValue());
                    list.get(i).put("num","0");
                    list.get(i).put("li_num",backnumber);
                    //添加退货产品信息详情
                    returnOrderProService.save(list.get(i));
                    //查询订单库存的信息
                    //List<PageData> lists=orderKuncunService.findList(list.get(i));.
                    List<PageData> lists=orderKuncunService.findListli(list.get(i));
                    if(lists!=null&&lists.size()!=0){
                        for(int j=0;j<lists.size();j++){
                            BigDecimal in=kucun;//需要退货的数量
                            //如果订单数量大于等于要退货的数量
                            if(new BigDecimal(lists.get(j).get("now_number").toString()).compareTo(in)>0){//部分退货
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>走第一步");
                                lists.get(j).put("now_number",new BigDecimal(lists.get(j).get("now_number").toString()).subtract(in));
                                //修改订单的库存
                                orderKuncunService.editNumsli(lists.get(j));
                                lists.get(j).put("nums","0");
                                //修改库存表的数据
                                lists.get(j).put("now_number",in);
                                kunCunService.editJiaNums(lists.get(j));
                                kucun=new BigDecimal(0);
                            }else{//全部退货
                                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>走第二步");
                                BigDecimal kuncuns=in.subtract(new BigDecimal(lists.get(j).get("nums").toString()));
                                kucun=kuncuns;
                                //修改订单的库存
                                lists.get(j).put("now_number",new BigDecimal(0));
                                orderKuncunService.editNumsli(lists.get(j));
                                lists.get(j).put("nums","0");
                                lists.get(j).put("now_number",in);
                                //修改库存表的数据
                                kunCunService.editJiaNums(lists.get(j));
                                lists.get(j).put("nums",0);

                            }
                        }
                    }
                    list.get(i).put("now_number",backnumber);
                    //修改订单详情表数据
                    orderProService.editOrderKuncunli(list.get(i));

                    List<PageData> pageDataList = new ArrayList<>();
                    pageDataList.add(list.get(i));
                    if(list.get(i).get("isThreeSales").toString().equals("0.0")){ //如果是按三级单位购买的
                        list.get(i).put("all_number",backnumber);
                        //修改商品库存数量
                        productService.editJiaNumsli(list.get(i));
                    }else {
                        BigDecimal bigDecimal = new BigDecimal(list.get(i).get("num").toString()).multiply(new BigDecimal(list.get(i).get("norms4").toString()));
                        list.get(i).put("num",bigDecimal);
                        list.get(i).put("all_number",backnumber);
                        //修改商品库存数量
                        productService.editJiaNumsli(list.get(i));
                    }

                    //添加库存表记录  status=5表示退货加数量
                    kunCunService.batchSavesli(pageDataList,store_id,DateUtil.getTime(),"5","0",order_info_id,pd_o.get("return_order_info_id").toString());

                }else {//没有拆单的商品
                    list.get(i).put("return_order_info_id",pd_o.get("return_order_info_id").toString());
                    list.get(i).put("order_info_id",order_info_id);
                    list.get(i).put("order_pro_id",new Double(list.get(i).get("order_pro_id").toString()).intValue());
                    list.get(i).put("product_id",new Double(list.get(i).get("product_id").toString()).intValue());
                    //添加退货产品信息详情
                    list.get(i).put("li_num",new BigDecimal(0));
                    returnOrderProService.save(list.get(i));
                    //查询订单库存的信息
                    List<PageData> lists=orderKuncunService.findList(list.get(i));
                    String kucun=list.get(i).get("num").toString();//需要退货的数量
                    if(lists!=null&&lists.size()!=0){
                        for(int j=0;j<lists.size();j++){
                            //int in=(new Double(kucun)).intValue();//需要退货的数量
                            BigDecimal in=new BigDecimal(kucun);//需要退货的数量
                            //如果订单数量大于等于要退货的数量
                            //if(Integer.valueOf(lists.get(j).get("nums").toString())>in){
                            if(new BigDecimal(lists.get(j).get("nums").toString()).compareTo(in)==1){
                                //lists.get(j).put("nums",Integer.valueOf(lists.get(j).get("nums").toString())-in);
                                lists.get(j).put("nums",new BigDecimal(lists.get(j).get("nums").toString()).subtract(in));
                                //修改订单的库存
                                orderKuncunService.editNums(lists.get(j));
                                lists.get(j).put("nums",in);
                                //修改库存表的数据
                                kunCunService.editJiaNums(lists.get(j));
                                kucun="0";
                            }else{//全部退货
                                //Integer kuncuns=in-Integer.valueOf(lists.get(j).get("nums").toString());
                                BigDecimal kuncuns=in.subtract(new BigDecimal(lists.get(j).get("nums").toString()));
                                kucun=String.valueOf(kuncuns);
                                //修改库存表的数据
                                kunCunService.editJiaNums(lists.get(j));
                                lists.get(j).put("nums",0);
                                //修改订单的库存
                                orderKuncunService.editNums(lists.get(j));
                            }
                        }
                    }
                    //修改订单详情表数据
                    orderProService.editOrderKuncun(list.get(i));
                    List<PageData> pageDataList = new ArrayList<>();
                    pageDataList.add(list.get(i));

                    if(list.get(i).get("isThreeSales").toString().equals("0.0")){ //如果是按三级单位购买的
                        list.get(i).put("now_number",new BigDecimal(0));
                        //修改商品库存数量
                        productService.editJiaNums(list.get(i));
                    }else {
                        BigDecimal bigDecimal = new BigDecimal(list.get(i).get("num").toString()).multiply(new BigDecimal(list.get(i).get("norms4").toString()));
                        list.get(i).put("num",bigDecimal);
                        list.get(i).put("now_number",new BigDecimal(0));
                        //修改商品库存数量
                        productService.editJiaNums(list.get(i));
                    }

                    //添加库存表记录  status=5表示退货加数量
                    kunCunService.batchSaves(pageDataList,store_id,DateUtil.getTime(),"5","0",order_info_id,pd_o.get("return_order_info_id").toString());
                }
            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("order_number",no);
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
     * @param order_info_id 订单ID
     * @param data (product_name(商品名称),product_price(商品价格)
     *             ,num(数量),total(总价),norms1(规格1),norms2(规格2),norms3(规格3),product_id(商品ID))
     * @param money 总价
     * @param store_id 店ID
     * @return
     */
    @RequestMapping(value = "returnOrderInfo",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String returnOrderInfo(String order_info_id,String data,String money,String store_id){
        logBefore(logger,"订单退货");
        PageData pd=this.getPageData();
        try{
            PageData pd_o=orderInfoService.findById(pd);
            pd_o.put("money",money);
            pd_o.put("return_date",DateUtil.getTime());
            returnOrderInfoService.save(pd_o);
            data = DateUtil.delHTMLTag(data);
            data = data.replace("\r", "");
            data = data.replace("\n", "");
            data = data.replace("\\", "");
            Gson gson = new Gson();
            List<PageData> list = gson.fromJson(data, new TypeToken<List<PageData>>() {
            }.getType());   //获取订单商品列表
            for(int i=0;i<list.size();i++){
                list.get(i).put("return_order_info_id",pd_o.get("return_order_info_id").toString());
                list.get(i).put("order_info_id",order_info_id);
                returnOrderProService.save(list.get(i));
            }
            kunCunService.batchSave(list,store_id,DateUtil.getTime(),"5",pd_o.get("customer_id").toString(),pd_o.get("return_order_info_id").toString(),"0");
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

    /**
     * 查询退货单详情
     * @param return_order_info_id
     * @return
     */
    @RequestMapping(value = "findReturnOrderInfoId",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findReturnOrderInfoId(String return_order_info_id){
        logBefore(logger,"查询详情");
        PageData pd=this.getPageData();
        try{
            PageData pd_o=returnOrderInfoService.findById(pd);
            List<PageData> list=returnOrderProService.findList(pd_o);
            pd.put("order_info_id",pd_o.get("order_info_id"));
            PageData pageData = integralService.findIntegralDetailReturnByOrderId(pd);
            pd.clear();
            if(pageData!=null){
                pd.put("integral",new BigDecimal(pageData.get("integral").toString()));
            }
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("order",pd_o);
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

    @RequestMapping(value = "findReturnOrderInfo",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findReturnOrderInfo(String store_id,String pageNum,String startTime,
                                      String endTime,String customer_id,String name){
        logBefore(logger,"查询退货订单");
        PageData pd=this.getPageData();
        Page page=new Page();
        String message="正确返回数据!";
        try{
            if (pageNum == null || pageNum.length() == 0) {
                pageNum = "1";
            }
            pd.put("return_goods","0");
            page.setPd(pd);
            page.setShowCount(10);
            page.setCurrentPage(Integer.parseInt(pageNum));
            List<PageData> list=returnOrderInfoService.datalistPage(page);
            for(int i=0;i<list.size();i++){
                List<PageData> list_pro=returnOrderProService.findList(list.get(i));
                list.get(i).put("order_pro",list_pro);
            }
            PageData pdMoney=returnOrderInfoService.findDateSumMoney(pd);
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.0");
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
            pd.put("totalMoney",totalMoney);
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


    @RequestMapping(value = "findlikeReturnOrderInfo",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findlikeReturnOrderInfo(String store_id,String name){
        logBefore(logger,"查询退货订单");
        PageData pd=this.getPageData();
        try{
            List<PageData> list=returnOrderInfoService.findLikeOrderInfo(pd);
            for(int i=0;i<list.size();i++){
                List<PageData> list_pro=orderProService.findList(list.get(i));
                list.get(i).put("order_pro",list_pro);
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

    /**
     * 撤销退货
     * @param return_order_info_id
     * @return
     */
    @RequestMapping(value = "revokesReturnOrderInfo",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String revokesReturnOrderInfo(String return_order_info_id){
        logBefore(logger,"撤销退货订单");
        PageData pd=this.getPageData();
        try{
            //查询退货单详情
            PageData pdReturn=returnOrderInfoService.findById(pd);
            //处理积分问题
            pd.put("is_pass",0);
            pd.put("order_info_id",pdReturn.get("order_info_id"));
            Integer aax = integralService.findIntegralDetailsCountByOrderId(pd);
            if(aax>0){
                //查找已退货未撤销积分明细
                PageData pad = integralService.findIntegralDetailByOrderIdAndType(pd);
                pad.put("status",2); //2是增加积分
                pad.put("customer_id",pad.get("user_id"));
                //integralService.saveIntegralDetail(pad);
                pad.put("is_pass",1);
                integralService.editIntegralDetailPassById(pad); //把积分类型置为撤销
                PageData pageData = new PageData();
                pageData.put("customer_id",pad.get("user_id"));
                pageData.put("integral",pad.get("integral"));
                integralService.editIntegral(pageData);//增加积分
            }
            //处理赠品问题
            PageData giftPage = new PageData();
            giftPage.put("order_info_id",pdReturn.get("order_info_id"));
            giftPage.put("is_back",0);
            orderGiftService.editGiftBack(giftPage);
            //处理账户余额问题
            PageData pp2 = new PageData();
            pp2.put("return_order_info_id",return_order_info_id);
            List<PageData> balancelist = balanceService.findBalanceRetuenByOrderId(pp2);
            if(balancelist!=null&&balancelist.size()>0){
                for (int i = 0; i < balancelist.size(); i++) {
                    Integer ac = Integer.valueOf(balancelist.get(i).get("type").toString());
                    if(ac==1){
                        PageData pa = new PageData();
                        pa.put("customer_id",balancelist.get(i).get("customer_id"));
                        pa.put("owe",balancelist.get(i).get("number"));
                        customerService.editOwnByCustomerIdUp(pa);
                    }else if(ac==2){
                        PageData pa = new PageData();
                        pa.put("user_id",balancelist.get(i).get("customer_id"));
                        pa.put("balance",balancelist.get(i).get("number"));
                        balanceService.editBalanceByUserIdDown(pa);
                    }
                }
            }
            pd.put("revokes","1");
            //修改退货单状态 1已撤销
            returnOrderInfoService.editRevokes(pd);
            //查询退货单信息
            List<PageData> list=returnOrderProService.findList(pd);
            for(int i=0;i<list.size();i++){
                if(new BigDecimal(list.get(i).get("li_num").toString()).compareTo(new BigDecimal(0))>0){//表示拆袋的
                    list.get(i).put("order_info_id",pdReturn.get("order_info_id").toString());
                    PageData pdOrderpro=orderProService.findOrderInfoProduct(list.get(i));
                    List<PageData> listKun=orderKuncunService.findOrderProList(pdOrderpro);
                    //String kucun=list.get(i).get("num").toString();
                    BigDecimal kucun = new BigDecimal(list.get(i).get("li_num").toString());
                    Integer aa = 0;
                    for(int j=0;j<listKun.size();j++){
                        System.out.println("查询出来的库存id："+listKun.get(j).get("kuncun_id"));

                        //int tuihuo=Integer.valueOf(listKun.get(j).get("num").toString())-Integer.valueOf(listKun.get(j).get("nums").toString());
                        //现有的库存的数量
                        BigDecimal tuihuo = new BigDecimal(listKun.get(j).get("now_number").toString());
                        if(tuihuo.compareTo(kucun)>-1){//
                            listKun.get(j).put("now_number",tuihuo.add(kucun));
                            orderKuncunService.editNumsli(listKun.get(j));
                            if(aa==1){
                                System.out.println("走这里了");
                                listKun.get(j).put("now_number",kucun);
                            }else {
                                listKun.get(j).put("now_number",new BigDecimal(list.get(i).get("li_num").toString()));
                            }
                            kunCunService.editJianNumssli(listKun.get(j));
                            kucun=new BigDecimal(0);
                        }else{
                            //listKun.get(j).put("nums",Integer.valueOf(listKun.get(j).get("nums").toString())+tuihuo);
                            listKun.get(j).put("now_number",new BigDecimal(listKun.get(j).get("now_number").toString()).add(tuihuo));
                            orderKuncunService.editNumsli(listKun.get(j));
                            listKun.get(j).put("now_number",tuihuo);
                            System.out.println(">>>>>>>222222"+listKun.get(j).get("li_num"));
                            kunCunService.editJianNumssli(listKun.get(j));
                            //int kucuns=Integer.valueOf(kucun)-tuihuo;
                            BigDecimal kucuns = kucun.subtract(tuihuo);
                            kucun=kucuns;
                            aa = 1;
                        }
                    }
                    pdOrderpro.put("now_number",new BigDecimal(list.get(i).get("li_num").toString()));
                    orderProService.editOrderKuncunsli(pdOrderpro);
                    if(list.get(i).get("isThreeSales").toString().equals("0.0")){ //如果是按三级单位购买的
                        pdOrderpro.put("now_number",new BigDecimal(list.get(i).get("li_num").toString()));
                        productService.editJianNumsli(pdOrderpro);//更新商品库存信息
                    }else {
                        BigDecimal bigDecimal = new BigDecimal(list.get(i).get("li_num").toString()).multiply(new BigDecimal(list.get(i).get("norms4").toString()));
                        pdOrderpro.put("now_number",bigDecimal);
                        productService.editJianNumsli(pdOrderpro);//更新商品库存信息
                    }
//                    productService.editJianNumsli(pdOrderpro);
                }else {
                    list.get(i).put("order_info_id",pdReturn.get("order_info_id").toString());
                    PageData pdOrderpro=orderProService.findOrderInfoProduct(list.get(i));
                    List<PageData> listKun=orderKuncunService.findOrderProList(pdOrderpro);
                    String kucun=list.get(i).get("num").toString();
                    Integer aa = 0;
                    for(int j=0;j<listKun.size();j++){
                        System.out.println("查询出来的库存id："+listKun.get(j).get("kuncun_id"));
                        //int tuihuo=Integer.valueOf(listKun.get(j).get("num").toString())-Integer.valueOf(listKun.get(j).get("nums").toString());
                        BigDecimal tuihuo= new BigDecimal(listKun.get(j).get("num").toString()).subtract(new BigDecimal(listKun.get(j).get("nums").toString()));
                        //if(tuihuo>=Integer.valueOf(kucun)){
                        if(tuihuo.compareTo(new BigDecimal(kucun))>-1){
                            //listKun.get(j).put("nums",Integer.valueOf(listKun.get(j).get("nums").toString())+Integer.valueOf(kucun));
                            listKun.get(j).put("nums",new BigDecimal(listKun.get(j).get("nums").toString()).add(new BigDecimal(kucun)));
                            orderKuncunService.editNums(listKun.get(j));
                            if(aa==1){
                                System.out.println("走这里了");
                                //listKun.get(j).put("nums",Integer.valueOf(kucun));
                                listKun.get(j).put("nums",new BigDecimal(kucun));
                            }else {
                                //listKun.get(j).put("nums",Integer.valueOf(list.get(i).get("num").toString()));
                                listKun.get(j).put("nums",new BigDecimal(list.get(i).get("num").toString()));
                            }
                            System.out.println(">>>>>>>1111111"+listKun.get(j).get("nums"));
                            kunCunService.editJianNumss(listKun.get(j));
                            kucun="0";
                        }else{
                            //listKun.get(j).put("nums",Integer.valueOf(listKun.get(j).get("nums").toString())+tuihuo);
                            listKun.get(j).put("nums",new BigDecimal(listKun.get(j).get("nums").toString()).add(tuihuo));
                            orderKuncunService.editNums(listKun.get(j));
                            listKun.get(j).put("nums",tuihuo);
                            System.out.println(">>>>>>>222222"+listKun.get(j).get("nums"));
                            kunCunService.editJianNumss(listKun.get(j));
                            //int kucuns=Integer.valueOf(kucun)-tuihuo;
                            BigDecimal kucuns=new BigDecimal(kucun).subtract(tuihuo);
                            kucun=String.valueOf(kucuns);
                            aa = 1;
                        }
                    }
                    //pdOrderpro.put("num",list.get(i).get("num").toString());
                    pdOrderpro.put("num",new BigDecimal(list.get(i).get("num").toString()));
                    orderProService.editOrderKuncuns(pdOrderpro);

                    if(list.get(i).get("isThreeSales").equals(0)){ //如果是按三级单位购买的
                        productService.editJianNums(pdOrderpro);//更新商品库存信息
                    }else {
                        BigDecimal bigDecimal = new BigDecimal(list.get(i).get("num").toString()).multiply(new BigDecimal(list.get(i).get("norms4").toString()));
                        pdOrderpro.put("num",bigDecimal);
                        productService.editJianNums(pdOrderpro);//更新商品库存信息
                    }

//                    productService.editJianNums(pdOrderpro);
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


    @RequestMapping(value = "findReturnExcelUtil",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void findReturnExcelUtil(String store_id, HttpServletRequest request, HttpServletResponse response){
        try{
            PageData pd=this.getPageData();
            List<PageData> list=returnOrderProService.findQuanBu(pd);
            ReturnOrderExtend.printExcle(list,"E://ceshi.xls",request,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
