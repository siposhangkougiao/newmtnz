package com.mtnz.service.system.order_kuncun;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mtnz.controller.app.order.model.OrderGift;
import com.mtnz.controller.app.store.model.Store;
import com.mtnz.controller.app.store.model.StoreLose;
import com.mtnz.controller.base.BaseController;
import com.mtnz.dao.DaoSupport;
import com.mtnz.service.system.agency.AgencyService;
import com.mtnz.service.system.balance.BalanceService;
import com.mtnz.service.system.customer.CustomerService;
import com.mtnz.service.system.integral.IntegralService;
import com.mtnz.service.system.order_info.OrderGiftService;
import com.mtnz.service.system.order_info.OrderInfoService;
import com.mtnz.service.system.order_pro.OrderProService;
import com.mtnz.service.system.product.KunCunService;
import com.mtnz.service.system.product.ProductService;
import com.mtnz.service.system.sys_app_user.SysAppUserService;
import com.mtnz.sql.system.store.StoreLoseMapper;
import com.mtnz.sql.system.store.StoreMapper;
import com.mtnz.util.DateUtil;
import com.mtnz.util.PageData;
import com.mtnz.util.SmsBao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.rpc.ServiceException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/*
    Created by xxj on 2018\6\12 0012.  
 */
@Service
@Resource(name = "orderKuncunService")
public class OrderKuncunService extends BaseController{
    @Resource(name = "daoSupport")
    private DaoSupport daoSupport;
    @Resource(name = "orderInfoService")
    private OrderInfoService orderInfoService;
    @Resource(name = "customerService")
    private CustomerService customerService;
    @Resource(name = "orderProService")
    private OrderProService orderProService;
    @Resource(name = "kunCunService")
    private KunCunService kunCunService;
    @Resource(name = "sysAppUserService")
    private SysAppUserService sysAppUserService;
    @Resource(name = "agencyService")
    private AgencyService agencyService;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "integralService")
    private IntegralService integralService;
    @Resource(name = "balanceService")
    private BalanceService balanceService;
    @Resource(name = "orderGiftService")
    private OrderGiftService orderGiftService;
    @Autowired
    private StoreLoseMapper storeLoseMapper;
    @Autowired
    private StoreMapper storeMapper;


    public void save(PageData pd) throws Exception {
        daoSupport.save("OrderKunCunMapper.save",pd);
    }


    public List<PageData> findList(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderKunCunMapper.findList",pd);
    }

    public void editNums(PageData pd) throws Exception {
        daoSupport.update("OrderKunCunMapper.editNums",pd);
    }

    public PageData findById(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("OrderKunCunMapper.findById",pd);
    }

    public PageData findSupplierProduct(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("OrderKunCunMapper.findSupplierProduct",pd);
    }

    public List<PageData> findOrderIdList(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderKunCunMapper.findOrderIdList",pd);
    }

    public void editrevokes(PageData pd) throws Exception {
        daoSupport.update("OrderKunCunMapper.editrevokes",pd);
    }

    public List<PageData> findOrderProList(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderKunCunMapper.findOrderProList",pd);
    }

    public PageData findSumProduct(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("OrderKunCunMapper.findSumProduct",pd);
    }



    public String saveOrder(String name,String phone,String status,
                            String total_money,String money,String discount_money,
                            String owe_money,String data,String customer_id,String store_id,
                            String medication_date,String remarks,String uid,String open_bill ,String date
            ,Integer isli,BigDecimal integral,Long open_user,String remark,BigDecimal balance,
                            List<OrderGift> orderGifts,Integer lose,Integer isfast,Integer isSend) throws Exception {
        logBefore(logger,"销售开单");
        java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
        PageData pd=this.getPageData();
        ObjectMapper mapper=new ObjectMapper();
        if(name==null||name.length()==0||status==null||status.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            PageData pd_customer=new PageData();//存放的是客户的信息
            String statuss="0";
            if(customer_id.length()==0){
                //判断客户存在不存在
                pd_customer=customerService.findNameCustomer(pd);
                if(pd_customer!=null){
                    customer_id=pd_customer.get("customer_id").toString();
                }else{//如果不存在的话就添加一个客户
                    pd_customer=new PageData();
                    pd_customer.put("name",name);
                    pd_customer.put("phone",phone);
                    pd_customer.put("address","");
                    pd_customer.put("crop","");
                    pd_customer.put("area","");
                    pd_customer.put("input_date", date);
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
                pd_customer=customerService.findById(pd);//如果存在就查找出来客户
            }
            Date now = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String s = String.valueOf((int) ((Math.random() * 9 + 1) * 100));
            // 订单号
            String no = "";
            if(isfast!=null&&isfast==1){//计算开单的订单
                no = "FA" + s + sdf1.format(now).substring(2);
            }else {
                no = s + sdf1.format(now).substring(2);
            }
            PageData pd_o=new PageData();
            pd_o.put("store_id",store_id);
            pd_o.put("customer_id",customer_id);
            pd_o.put("name",name);
            pd_o.put("phone",phone);
            pd_o.put("address",pd_customer.getString("province")+pd_customer.getString("city")+
                    pd_customer.getString("province")+pd_customer.getString("county")+pd_customer.getString("street")+pd_customer.getString("address"));
            pd_o.put("money",money);
            pd_o.put("date",date);
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
            System.out.println("json参数===============》"+data);
            data = DateUtil.delHTMLTag(data);
            data = data.replace("\r", "");
            data = data.replace("\n", "");
            data = data.replace("\\", "");
            data = data.replace("\\\\", "");
            data = data.replace("/", "");
            data = data.replace(" ", "XINg");
            Gson gson = new Gson();
            logger.error("解析之后的json是："+data);

            List<PageData> list = gson.fromJson(data, new TypeToken<List<PageData>>() {
            }.getType());   //获取订单商品列表

            logger.error("解析之后的json是222："+data);
            BigDecimal oneprice = new BigDecimal(0);
            //查询员工的销售提成比例
            for (int i = 0; i <list.size() ; i++) {
                PageData dataone = new PageData();
                dataone.put("uid",open_user);
                dataone.put("product_id",list.get(i).get("product_id"));
                PageData saleLevel = productService.findSaleLevel(dataone);
                if(saleLevel!=null){
                    PageData product = productService.findById(dataone);//查询出来商品的信息
                    if(list.get(i).get("isli")!=null&&new BigDecimal(list.get(i).get("isli").toString()).compareTo(new BigDecimal(0))>0){//拆袋
                        BigDecimal level = new BigDecimal(saleLevel.get("level").toString());
                        BigDecimal price = new BigDecimal(list.get(i).get("num").toString())
                                .divide(new BigDecimal(product.get("norms1").toString()),4,BigDecimal.ROUND_HALF_UP)
                                .multiply(new BigDecimal(product.get("product_price").toString())).multiply(level);
                        oneprice = oneprice.add(price);
                        list.get(i).put("product_sale",price);
                    }else {
                        BigDecimal level = new BigDecimal(saleLevel.get("level").toString());
                        BigDecimal price = new BigDecimal(list.get(i).get("num").toString())
                                .multiply(new BigDecimal(product.get("product_price").toString()))
                                .multiply(level);
                        oneprice = oneprice.add(price);
                        list.get(i).put("product_sale",price);
                    }
                }
            }
            BigDecimal allprice = new BigDecimal(0);
            PageData selectSaleLevel = new PageData();
            selectSaleLevel.put("uid",open_user);
            PageData pageDatasa = productService.selectSaleLevel(selectSaleLevel);
            if(pageDatasa!=null){
                BigDecimal salelevel = new BigDecimal(pageDatasa.get("level").toString());
                allprice = allprice.add(new BigDecimal(money).multiply(salelevel));
            }
            pd_o.put("product_sale",oneprice);
            pd_o.put("total_sale",allprice);

            StoreLose storeLose = new StoreLose();
            storeLose.setStoreId(Long.valueOf(store_id));
            StoreLose selectLose = storeLoseMapper.selectOne(storeLose);
            if(selectLose!=null){
                if(selectLose.getAutomatic()==1){
                    pd_o.put("outboundStatus",1);
                }else {
                    pd_o.put("outboundStatus",0);
                }
            }
            orderInfoService.save(pd_o);//添加主表订单信息
            //处理赠品
            if(orderGifts!=null&&orderGifts.size()>0){
                orderGiftService.saveGift(orderGifts,Long.valueOf(pd_o.get("order_info_id").toString()));
            }
            /**/
            // 下面处理积分的问题
            if(integral!=null){
                PageData pageda = new PageData();
                pageda.put("order_id",pd_o.get("order_info_id"));
                pageda.put("status",2);
                pageda.put("integral",integral);
                pageda.put("customer_id",customer_id);
                if(open_user!=null){
                    pageda.put("open_user",open_user);
                }else {
                    pageda.put("open_user",uid);
                }
                pageda.put("remark",remark);
                integralService.saveIntegralDetail(pageda);//积分详情表
                Integer count = integralService.findIntegralUserById(pageda);//查询用户积分余额
                if(count==null||count<1){//没有的话添加一个
                    PageData pageData = new PageData();
                    pageData.put("user_id",customer_id);
                    pageData.put("remain_integral",integral);
                    pageData.put("all_integral",integral);
                    pageData.put("use_integral",new BigDecimal(0));
                    pageData.put("name",name);
                    integralService.saveIntegralUser(pageData);
                }else {//有的话就直接修改了
                    integralService.editIntegral(pageda);
                }
            }
            //处理账户余额问题
            if(balance!=null&&balance.compareTo(new BigDecimal(0))>0){
                PageData balanceuser = new PageData();
                if(customer_id==null){
                    return getMessage("-101","客户id不能为空");
                }
                balanceuser.put("user_id",customer_id);
                PageData pda = balanceService.findUserbalanceByUserId(balanceuser);
                BigDecimal ubalnce =new BigDecimal(0);
                if(pda!=null){
                    ubalnce = new BigDecimal(pda.get("balance").toString());
                    if(balance.compareTo(ubalnce)>0){
                        throw new ServiceException("余额不足");
                    }
                    PageData pdda = new PageData();
                    pdda.put("user_id",customer_id);
                    pdda.put("balance",balance);
                    balanceService.editBalanceByUserIdDown(pdda);
                    PageData pddetail = new PageData();
                    pddetail.put("balance",balance);
                    pddetail.put("user_id",customer_id);
                    pddetail.put("open_id",open_user);
                    pddetail.put("order_info_id",pd_o.get("order_info_id"));
                    pddetail.put("type",1);
                    balanceService.saveBalanceDetailSaveOrder(pddetail);
                }else {
                    PageData elpda = new PageData();
                    elpda.put("customer_id",customer_id);
                    PageData cuspage = customerService.findById(elpda);
                    elpda.put("name",cuspage.get("name"));
                    elpda.put("balance",new BigDecimal(0));
                    elpda.put("user_id",customer_id);
                    balanceService.saveBalance(elpda);
                    if(balance.compareTo(ubalnce)>0){
                        throw new ServiceException("余额不足");
                    }
                    /*PageData pddetail = new PageData();
                    pddetail.put("balance",balance);
                    pddetail.put("user_id",customer_id);
                    pddetail.put("open_id",open_user);
                    pddetail.put("order_info_id",pd_o.get("order_info_id"));
                    pddetail.put("type",1);
                    balanceService.saveBalanceDetailSaveOrder(pddetail);*/
                }

            }


            PageData pd_cc=new PageData();
            pd_cc.put("customer_id",customer_id);
            pd_cc.put("billing_date",pd_o.getString("date"));
            customerService.updateBillingDate(pd_cc);//这里更新用户的最新下单时间
            for (int i = 0; i < list.size(); i++) {//遍历获取的商品信息
                BigDecimal zong = new BigDecimal(list.get(i).get("num").toString());//未转换的零售数量
                BigDecimal zong1 = new BigDecimal(list.get(i).get("num").toString());
                PageData product = productService.findById(list.get(i));//查询出来商品的信息
                if(list.get(i).get("isli")!=null&&new BigDecimal(list.get(i).get("isli").toString()).compareTo(new BigDecimal(0))>0){
                    BigDecimal goumai = new BigDecimal(list.get(i).get("num").toString());

                    //判断是否支持负库存销售
                    if(lose!=null){
                        BigDecimal allNum = new BigDecimal(product.get("kucun").toString()).add(
                                new BigDecimal(product.get("likucun").toString()).divide(new BigDecimal(product.get("norms1").toString()),4,BigDecimal.ROUND_HALF_UP)
                        );
                        if(allNum.compareTo(goumai)<0){
                            return getMessage("-102","不支持负库存");
                        }
                    }
                    if(isnumber(product.get("norms1").toString())){//判断是否可以零售
                        System.out.println(">>>>>>想要购买的总的数量："+zong.toString());
                        if(zong.compareTo(new BigDecimal(0))>0){
                            list.get(i).put("num","0");
                            list.get(i).put("order_kuncun","0");
                            //list.get(i).put("li_num",zong.divide(new BigDecimal(product.get("norms1").toString())).setScale(2,BigDecimal.ROUND_UP));
                            //list.get(i).put("li_nums",zong.divide(new BigDecimal(product.get("norms1").toString())).setScale(2,BigDecimal.ROUND_UP));
                            list.get(i).put("all_number",zong1);
                            list.get(i).put("now_number",zong1);
                            list.get(i).put("order_info_id",pd_o.get("order_info_id").toString());
                            list.get(i).put("orde_by","1");
                            orderProService.save(list.get(i));//添加商品详情表
                            list.get(i).put("product_id",(new Double(list.get(i).get("product_id").toString())).intValue());
                            list.get(i).put("store_id",store_id);
                            List<PageData> lists=kunCunService.findList(list.get(i));//查找库存
                            if(lists!=null&&lists.size()!=0){//遍历库存
                                for (int j = 0; j <lists.size() ; j++) {
                                    //现在有的库存
                                    //BigDecimal nowkucun = goumai.add(new BigDecimal(lists.get(j).get("likucun").toString())).multiply(new BigDecimal(product.get("norms1").toString())).setScale(2,BigDecimal.ROUND_UP);
                                    BigDecimal nowkucun = new BigDecimal(lists.get(j).get("nums").toString())
                                            .multiply(new BigDecimal(product.get("norms1").toString())).add(new BigDecimal(lists.get(j).get("likucun").toString()));
                                    BigDecimal [] quyu = goumai.divideAndRemainder(new BigDecimal(product.get("norms1").toString()));
                                    //转换之后的整袋
                                    //BigDecimal numstotal = goumai.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(0,BigDecimal.ROUND_DOWN);
                                    BigDecimal numstotal = quyu[0];
                                    //转换之后的零装
                                    //BigDecimal numsli = goumai.divide(new BigDecimal(product.get("norms1").toString()).subtract(numstotal),2);
                                    BigDecimal numsli = quyu[1];
                                    if(nowkucun.compareTo(zong)>-1){//现有库存大于实际购买的数量时
                                        lists.get(j).put("order_pro_id",list.get(i).get("order_pro_id").toString());
                                        lists.get(j).put("product_price",list.get(i).get("product_price").toString());
                                        lists.get(j).put("purchase_price",lists.get(j).get("purchase_price").toString());
                                        lists.get(j).put("order_info_id",pd_o.get("order_info_id").toString());
                                        BigDecimal xianyou = new BigDecimal(lists.get(j).get("nums").toString());
                                        lists.get(j).put("num","0");
                                        lists.get(j).put("nums","0");
                                        System.out.println(">>>>>>>>zong:" + zong.toString() + "   norms1:"+product.get("norms1").toString());
                                        //lists.get(j).put("li_num",zong.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(2,BigDecimal.ROUND_UP));
                                        //lists.get(j).put("li_nums",zong.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(2,BigDecimal.ROUND_UP));
                                        lists.get(j).put("all_number",zong);
                                        lists.get(j).put("now_number",zong);
                                        lists.get(j).put("store_id",store_id);
                                        lists.get(j).put("date",date);
                                        //给orderKucun表添加数据
                                        save(lists.get(j));
                                        zong=new BigDecimal(0);
                                        //库存表现有的拆袋量
                                        BigDecimal condition = new BigDecimal(lists.get(j).get("likucun").toString());
                                        if(condition.compareTo(zong1)>-1){
                                            System.out.println("》》》》》111走这里参与计算了");
                                            lists.get(j).put("likucun",condition.subtract(numsli));
                                            lists.get(j).put("nums",xianyou.toString());
                                        }else {
                                            System.out.println("》》》》》222走这里参与计算了");
                                            lists.get(j).put("likucun",condition.add(new BigDecimal(product.get("norms1").toString())).subtract(numsli));
                                            lists.get(j).put("nums",xianyou.subtract(numstotal).subtract(new BigDecimal(1)).toString());
                                        }
                                        System.out.println(">>>>>更新的库存数量："+lists.toString());
                                        kunCunService.editNumli(lists.get(j));
                                    }else{
                                        if(j==lists.size()-1){
                                            lists.get(j).put("order_pro_id",list.get(i).get("order_pro_id").toString());
                                            lists.get(j).put("product_price",list.get(i).get("product_price").toString());
                                            lists.get(j).put("purchase_price",lists.get(j).get("purchase_price").toString());
                                            lists.get(j).put("order_info_id",pd_o.get("order_info_id").toString());
                                            lists.get(j).put("num","0");
                                            lists.get(j).put("nums","0");
                                            //lists.get(j).put("li_num",zong.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(2,BigDecimal.ROUND_UP));
                                            //lists.get(j).put("li_nums",zong.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(2,BigDecimal.ROUND_UP));
                                            lists.get(j).put("all_number",zong);
                                            lists.get(j).put("now_number",zong);
                                            lists.get(j).put("store_id",store_id);
                                            lists.get(j).put("date",date);
                                            save(lists.get(j));
                                            zong=new BigDecimal(0);
                                            lists.get(j).put("nums","0");
                                            lists.get(j).put("likucun",new BigDecimal(0));
                                            kunCunService.editNumli(lists.get(j));
                                        }else{
                                            lists.get(j).put("order_pro_id",list.get(i).get("order_pro_id").toString());
                                            lists.get(j).put("product_price",list.get(i).get("product_price").toString());
                                            lists.get(j).put("purchase_price",lists.get(j).get("purchase_price").toString());
                                            lists.get(j).put("order_info_id",pd_o.get("order_info_id").toString());
                                            lists.get(j).put("store_id",store_id);
                                            lists.get(j).put("num","0");
                                            lists.get(j).put("nums","0");
                                            //lists.get(j).put("li_num",zong.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(2,BigDecimal.ROUND_UP));
                                            //lists.get(j).put("li_nums",zong.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(2,BigDecimal.ROUND_UP));
                                            lists.get(j).put("date",date);
                                            lists.get(j).put("all_number",zong);
                                            lists.get(j).put("now_number",zong);
                                            save(lists.get(j));
                                            zong = zong.subtract(new BigDecimal(lists.get(j).get("nums").toString()));
                                            lists.get(j).put("nums","0");
                                            lists.get(j).put("likucun","0");
                                            kunCunService.editNum(lists.get(j));
                                        }
                                    }
                                }
                            }else {//如果没有库存信息
                                PageData pd_p=kunCunService.findByproduct_id(list.get(i));
                                if (pd_p!=null){
                                    pd_p.put("order_pro_id",list.get(i).get("order_pro_id").toString());
                                    pd_p.put("product_price",list.get(i).get("product_price").toString());
                                    pd_p.put("purchase_price",pd_p.get("purchase_price").toString());
                                    pd_p.put("order_info_id",pd_o.get("order_info_id").toString());
                                    pd_p.put("num","0");
                                    pd_p.put("nums","0");
                                    //pd_p.put("li_num",zong.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(2,BigDecimal.ROUND_UP));
                                    //pd_p.put("li_nums",zong.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(2,BigDecimal.ROUND_UP));
                                    pd_p.put("all_number",zong);
                                    pd_p.put("now_number",zong);
                                    pd_p.put("date",date);
                                    save(pd_p);
                                }else{
                                    PageData pd_pp=productService.findById(list.get(i));
                                    pd_pp.put("order_pro_id",list.get(i).get("order_pro_id").toString());
                                    pd_pp.put("product_price",pd_pp.get("product_price").toString());
                                    if(pd_pp!=null&&!pd_pp.get("purchase_price").toString().equals("0")){
                                        pd_pp.put("purchase_price",pd_pp.get("purchase_price").toString());
                                    }else{
                                        pd_pp.put("purchase_price",list.get(i).get("product_price").toString());
                                    }
                                    pd_pp.put("kuncun_id","0");
                                    pd_pp.put("store_id",store_id);
                                    pd_pp.put("order_info_id",pd_o.get("order_info_id").toString());
                                    pd_pp.put("num","0");
                                    pd_pp.put("nums","0");
                                    //pd_p.put("li_num",zong.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(2,BigDecimal.ROUND_UP));
                                    //pd_p.put("li_nums",zong.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(2,BigDecimal.ROUND_UP));
                                    pd_pp.put("all_number",zong);
                                    pd_pp.put("now_number",zong);
                                    pd_pp.put("date",date);
                                    save(pd_pp);//添加订单库存信息
                                }
                            }
                        }
                    }else {
                        throw new ServiceException("此商品不可拆袋销售");
                    }
                    BigDecimal [] quyu = zong1.divideAndRemainder(new BigDecimal(product.get("norms1").toString()));
                    //转换之后的整袋
                    //BigDecimal numstotal = zong1.divide(new BigDecimal(product.get("norms1").toString()),2).setScale(0,BigDecimal.ROUND_DOWN);
                    BigDecimal numstotal = quyu[0];
                    //转换之后的零装
                    //BigDecimal numsli = zong1.divide(new BigDecimal(product.get("norms1").toString()).subtract(numstotal),2);
                    BigDecimal numsli = quyu[1];
                    //list.get(i).put("li_num",numstotal);
                    if(new BigDecimal(product.get("likucun").toString()).compareTo(numsli)>-1){
                        list.get(i).put("kucun",new BigDecimal(product.get("kucun").toString()).subtract(numstotal));
                        list.get(i).put("likucun",new BigDecimal(product.get("likucun").toString()).subtract(numsli));
                    }else {
                        list.get(i).put("kucun",new BigDecimal(product.get("kucun").toString()).subtract(new BigDecimal(1)).subtract(numstotal));
                        list.get(i).put("likucun",new BigDecimal(product.get("likucun").toString()).add(new BigDecimal(product.get("norms1").toString()))
                                .subtract(numsli));
                    }
                    productService.editNumli(list.get(i));//更新商品库存信息


                    //添加库存记录
                    List<PageData> listone = new ArrayList<>();
                    //list.get(i).put("li_num",zong1.divide(new BigDecimal(product.get("norms1").toString()),2));
                    list.get(i).put("likucun",new BigDecimal(0));
                    list.get(i).put("all_number",zong1);
                    list.get(i).put("now_number",zong1);
                    listone.add(list.get(i));
                    kunCunService.batchSavessli(listone,store_id,DateUtil.getTime(),"0",customer_id,pd_o.get("order_info_id").toString(),"1",pd_o.get("order_info_id").toString());
                }else {//这件商品不是拆袋销售的
                    //判断是否支持负库存销售
                    if(lose!=null){
                        BigDecimal allNum = new BigDecimal(product.get("kucun").toString());
                        if(allNum.compareTo(new BigDecimal(list.get(i).get("num").toString()))<0){
                            return getMessage("-102","不支持负库存");
                        }
                    }
                    list.get(i).put("order_info_id",pd_o.get("order_info_id").toString());
                    list.get(i).put("orde_by","1");
                    list.get(i).put("order_kuncun",list.get(i).get("num"));
                    list.get(i).put("likucun",new BigDecimal(0));
                    //list.get(i).put("li_num",new BigDecimal(0));
                    //list.get(i).put("li_nums",new BigDecimal(0));
                    list.get(i).put("all_number",new BigDecimal(0));
                    list.get(i).put("now_number",new BigDecimal(0));
                    orderProService.save(list.get(i));//添加商品详情表
                    list.get(i).put("product_id",(new Double(list.get(i).get("product_id").toString())).intValue());
                    list.get(i).put("store_id",store_id);
                    List<PageData> lists=kunCunService.findList(list.get(i));//查找库存
                    String kucun=list.get(i).get("num").toString();//商品购买的数量（这里是需要修改的）

                    if(lists!=null&&lists.size()!=0){//遍历库存

                        for (int j=0;j<lists.size();j++){
                            /*int in=(new Double(kucun)).intValue();*/
                            BigDecimal in = new BigDecimal(kucun);
                            /*if(Integer.valueOf(lists.get(j).get("nums").toString())>=in){//现有库存大于实际购买的数量时*/
                            if(new BigDecimal(lists.get(j).get("nums").toString()).compareTo(in)>-1){//现有库存大于实际购买的数量时
                                lists.get(j).put("order_pro_id",list.get(i).get("order_pro_id").toString());
                                lists.get(j).put("product_price",list.get(i).get("product_price").toString());
                                PageData pd_pp=productService.findById(list.get(i));

                                if(list.get(i).get("isThreeSales").toString().equals("1.0")){
                                    lists.get(j).put("purchase_price",new BigDecimal(list.get(i).get("purchase_price").toString()).multiply(new BigDecimal(pd_pp.get("norms4").toString())));
                                }else {
                                    lists.get(j).put("purchase_price",lists.get(j).get("purchase_price").toString());
                                }
//                                lists.get(j).put("purchase_price",lists.get(j).get("purchase_price").toString());
                                lists.get(j).put("order_info_id",pd_o.get("order_info_id").toString());
                                lists.get(j).put("num",kucun);
                                lists.get(j).put("store_id",store_id);
                                lists.get(j).put("date",date);
                                //lists.get(j).put("li_num",new BigDecimal(0));
                                //lists.get(j).put("li_nums",new BigDecimal(0));
                                lists.get(j).put("likucun",new BigDecimal(0));
                                lists.get(j).put("all_number",new BigDecimal(0));
                                lists.get(j).put("now_number",new BigDecimal(0));
                                save(lists.get(j));
                                kucun="0";
                                /*lists.get(j).put("nums",Integer.valueOf(lists.get(j).get("nums").toString())-Integer.valueOf(in));*/
                                lists.get(j).put("nums",new BigDecimal(lists.get(j).get("nums").toString()).subtract(in));
                                kunCunService.editNum(lists.get(j));
                            }else{
                                if(j==lists.size()-1){
                                    lists.get(j).put("order_pro_id",list.get(i).get("order_pro_id").toString());
                                    lists.get(j).put("product_price",list.get(i).get("product_price").toString());
                                    lists.get(j).put("purchase_price",lists.get(j).get("purchase_price").toString());
                                    lists.get(j).put("order_info_id",pd_o.get("order_info_id").toString());
                                    lists.get(j).put("num",kucun);
                                    lists.get(j).put("store_id",store_id);
                                    lists.get(j).put("date",date);
                                    //lists.get(j).put("li_num",new BigDecimal(0));
                                    //lists.get(j).put("li_nums",new BigDecimal(0));
                                    lists.get(j).put("all_number",new BigDecimal(0));
                                    lists.get(j).put("now_number",new BigDecimal(0));
                                    save(lists.get(j));
                                    kucun="0";
                                    lists.get(j).put("nums","0");
                                    kunCunService.editNum(lists.get(j));
                                }else{
                                    lists.get(j).put("order_pro_id",list.get(i).get("order_pro_id").toString());
                                    lists.get(j).put("product_price",list.get(i).get("product_price").toString());
                                    lists.get(j).put("purchase_price",lists.get(j).get("purchase_price").toString());
                                    lists.get(j).put("order_info_id",pd_o.get("order_info_id").toString());
                                    lists.get(j).put("store_id",store_id);
                                    lists.get(j).put("num",lists.get(j).get("nums").toString());
                                    lists.get(j).put("date",date);
                                    lists.get(j).put("all_number",new BigDecimal(0));
                                    lists.get(j).put("now_number",new BigDecimal(0));
                                    //lists.get(j).put("li_num",new BigDecimal(0));
                                    //lists.get(j).put("li_nums",new BigDecimal(0));
                                    save(lists.get(j));
                                    /*Integer kuncuns=Integer.valueOf(in)-Integer.valueOf(lists.get(j).get("nums").toString());*/
                                    BigDecimal kuncuns=in.subtract(new BigDecimal(lists.get(j).get("nums").toString()));
                                    kucun=kuncuns.toString();
                                    lists.get(j).put("nums","0");
                                    kunCunService.editNum(lists.get(j));
                                }
                            }
                        }
                    }else{//如果商品没有库存信息
                        PageData pd_p=kunCunService.findByproduct_id(list.get(i));
                        if (pd_p!=null){
                            pd_p.put("order_pro_id",list.get(i).get("order_pro_id").toString());
                            pd_p.put("product_price",list.get(i).get("product_price").toString());
                            pd_p.put("purchase_price",pd_p.get("purchase_price").toString());
                            pd_p.put("order_info_id",pd_o.get("order_info_id").toString());
                            pd_p.put("num",kucun);
                            pd_p.put("date",date);
                            pd_p.put("all_number",new BigDecimal(0));
                            pd_p.put("now_number",new BigDecimal(0));
                            //pd_p.put("li_num",new BigDecimal(0));
                            //pd_p.put("li_nums",new BigDecimal(0));
                            save(pd_p);
                        }else{
                            PageData pd_pp=productService.findById(list.get(i));
                            pd_pp.put("order_pro_id",list.get(i).get("order_pro_id").toString());
                            pd_pp.put("product_price",pd_pp.get("product_price").toString());
                            if(pd_pp!=null&&!pd_pp.get("purchase_price").toString().equals("0")){
                                pd_pp.put("purchase_price",pd_pp.get("purchase_price").toString());
                            }else{
                                pd_pp.put("purchase_price",list.get(i).get("product_price").toString());
                            }
                            pd_pp.put("kuncun_id","0");
                            pd_pp.put("store_id",store_id);
                            pd_pp.put("order_info_id",pd_o.get("order_info_id").toString());
                            pd_pp.put("num",kucun);
                            pd_pp.put("date",date);
                            pd_pp.put("all_number",new BigDecimal(0));
                            pd_pp.put("now_number",new BigDecimal(0));
                            //pd_pp.put("li_num",new BigDecimal(0));
                            //pd_pp.put("li_nums",new BigDecimal(0));
                            save(pd_pp);//添加订单库存信息
                        }
                    }

                    //添加库存记录
                    List<PageData> listone = new ArrayList<>();
                    //  list.get(i).put("li_num",new BigDecimal(0));
                    list.get(i).put("likucun",new BigDecimal(0));
                    list.get(i).put("all_number",new BigDecimal(0));
                    list.get(i).put("now_number",new BigDecimal(0));
                    if(list.get(i).get("isThreeSales").toString().equals("0.0")){ //如果是按三级单位购买的
                        productService.editNum(list.get(i));//更新商品库存信息
                    }else {
                        BigDecimal bigDecimal = new BigDecimal(list.get(i).get("num").toString()).multiply(new BigDecimal(list.get(i).get("norms4").toString()));
                        list.get(i).put("num",bigDecimal);
                        productService.editNum(list.get(i));//更新商品库存信息
                    }
                    listone.add(list.get(i));
                    kunCunService.batchSavess(listone,store_id,DateUtil.getTime(),"0",customer_id,pd_o.get("order_info_id").toString(),"1",pd_o.get("order_info_id").toString());

                }
            }
            Double owe=0.0;
            if(status.equals("1")){// 状态 0实收款  1欠款
                owe=Double.valueOf(pd_customer.get("owe").toString())+Double.valueOf(owe_money);
                pd_customer.put("owe",df.format(owe));
                customerService.updateOwe(pd_customer);//更新客户欠款信息
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
                pd.put("date", DateUtil.getTime());
                pd.put("fstatus","0");
                pd.put("customer_id",customer_id);
                agencyService.save(pd);//添加代办实行，提醒下次用药时间
            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("status",statuss);
            if(owe>0){
                pd.put("owe",df.format(owe));
            }else{
                pd.put("owe","0.0");
            }
            pd.put("customer_id",customer_id);
            pd.put("order_number",no);
            pd.put("order_info_id",pd_o.get("order_info_id"));

            if (isSend!=null){
                if(isSend==1){
                    Store store = new Store();
                    store.setStoreId(Long.valueOf(store_id));
                    Store stores = storeMapper.selectByPrimaryKey(store);
                    if(stores.getNumber().compareTo(0)==0){
                        pd.clear();
                        pd.put("code","2");
                        pd.put("message","短信剩余条数不足，请充值!");
                        pd.put("status",statuss);
                        return mapper.writeValueAsString(pd);
                    }
                    StringBuffer product_name = new StringBuffer();
                    //List<PageData>
                    BigDecimal flag= new BigDecimal("0");
                    for (int i = 0; i <list.size() ; i++) {
                        product_name.append(list.get(i).get("product_name").toString()).append("、");
                        flag =  flag.add(new BigDecimal(list.get(i).get("product_price").toString()));
                    }
                    product_name = product_name.deleteCharAt(product_name.length()-1);
                    SmsBao smsBao = new SmsBao();
                    smsBao.sendSms(stores.getName(),phone,product_name.toString(),flag.toString());
                }
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

    public String saveSupplierOrder(String name,String phone,String status,
                             String total_money,String money,String discount_money,
                             String owe_money,String data,String customer_id,String store_id,
                             String medication_date,String remarks,String uid) throws Exception{
            BaseController.logBefore(logger,"销售开单");
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.0");
            PageData pd=this.getPageData();
            ObjectMapper mapper=new ObjectMapper();
            Date now = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String s = String.valueOf((int) ((Math.random() * 9 + 1) * 100));
            String no = s + sdf1.format(now).substring(2); // 订单号
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
                list.get(i).put("order_info_id",pd_o.get("order_info_id").toString());
                list.get(i).put("orde_by","1");
                orderProService.save(list.get(i));
                list.get(i).put("product_id",(new Double(list.get(i).get("product_id").toString())).intValue());
                list.get(i).put("store_id",store_id);
                List<PageData> lists=kunCunService.findList(list.get(i));
                String kucun=list.get(i).get("num").toString();
                if(list.get(i).getString("status").equals("0")){
                    for (int j=0;j<lists.size();j++){
                        int in=(new Double(kucun)).intValue();
                        if(Integer.valueOf(lists.get(j).get("nums").toString())>=in){
                            lists.get(j).put("order_pro_id",list.get(i).get("order_pro_id").toString());
                            lists.get(j).put("product_price",list.get(i).get("product_price").toString());
                            lists.get(j).put("purchase_price",lists.get(j).getString("purchase_price"));
                            lists.get(j).put("order_info_id",pd_o.get("order_info_id").toString());
                            lists.get(j).put("num",kucun);
                            lists.get(j).put("store_id",store_id);
                            save(lists.get(j));
                            kucun="0";
                            lists.get(j).put("nums",Integer.valueOf(lists.get(j).get("nums").toString())-Integer.valueOf(in));
                            kunCunService.editNum(lists.get(j));
                            continue;
                        }else{
                            if(j==lists.size()-1){
                                lists.get(j).put("order_pro_id",list.get(i).get("order_pro_id").toString());
                                lists.get(j).put("product_price",list.get(i).get("product_price").toString());
                                lists.get(j).put("purchase_price",lists.get(j).getString("purchase_price"));
                                lists.get(j).put("order_info_id",pd_o.get("order_info_id").toString());
                                lists.get(j).put("num",kucun);
                                lists.get(j).put("store_id",store_id);
                                save(lists.get(j));
                                kucun="0";
                                lists.get(j).put("nums","0");
                                kunCunService.editNum(lists.get(j));
                            }else{
                                lists.get(j).put("order_pro_id",list.get(i).get("order_pro_id").toString());
                                lists.get(j).put("product_price",list.get(i).get("product_price").toString());
                                lists.get(j).put("purchase_price",lists.get(j).getString("purchase_price"));
                                lists.get(j).put("order_info_id",pd_o.get("order_info_id").toString());
                                lists.get(j).put("store_id",store_id);
                                lists.get(j).put("num",lists.get(j).get("nums").toString());
                                save(lists.get(j));
                                Integer kuncuns=Integer.valueOf(in)-Integer.valueOf(lists.get(j).get("nums").toString());
                                kucun=kuncuns.toString();
                                lists.get(j).put("nums","0");
                                kunCunService.editNum(lists.get(j));
                            }
                        }
                    }
                }else{

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


    public static boolean isnumber(String str) {
        Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]+)?$");
        return pattern.matcher(str).matches();
    }

    public static void main(String[] args) {
        BigDecimal[] aa = new BigDecimal(5.5).divideAndRemainder(new BigDecimal(2));
        System.out.println(aa[0].toString()+">>>>"+aa[1].toString());
    }

    public void editNumsli(PageData pd) throws Exception {
        daoSupport.update("OrderKunCunMapper.editNumsli",pd);
    }

    public List<PageData> findListli(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("OrderKunCunMapper.findListli",pd);
    }

    public BigDecimal findOrderKucunTsByIdAndTime(PageData pd) throws Exception {
        return (BigDecimal) daoSupport.findForObject("OrderKunCunMapper.findOrderKucunTsByIdAndTime",pd);
    }

    public BigDecimal findOrderKucunTsLiByIdAndTime(PageData pd) throws Exception {
        return (BigDecimal) daoSupport.findForObject("OrderKunCunMapper.findOrderKucunTsLiByIdAndTime",pd);
    }
}
