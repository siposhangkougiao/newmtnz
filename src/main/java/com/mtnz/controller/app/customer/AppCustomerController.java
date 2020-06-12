package com.mtnz.controller.app.customer;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mtnz.controller.app.customer.model.Customer;
import com.mtnz.controller.base.BaseController;
import com.mtnz.entity.Page;
import com.mtnz.service.system.agency.AgencyService;
import com.mtnz.service.system.balance.BalanceService;
import com.mtnz.service.system.customer.CustomerNewService;
import com.mtnz.service.system.customer.CustomerService;
import com.mtnz.service.system.integral.IntegralService;
import com.mtnz.service.system.order_info.OrderInfoService;
import com.mtnz.service.system.shortletter.ShortLetterService;
import com.mtnz.service.system.store.StoreService;
import com.mtnz.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/*
    Created by xxj on 2018\3\21 0021.  
 */
@Controller
@RequestMapping(value = "app/customer",produces = "text/html;charset=UTF-8")
public class AppCustomerController extends BaseController{
    @Resource(name = "customerService")
    private CustomerService customerService;    //客户
    @Resource(name = "orderInfoService")
    private OrderInfoService orderInfoService;  //开单
    @Resource(name = "storeService")
    private StoreService storeService;
    @Resource(name = "shortLetterService")
    private ShortLetterService shortLetterService;
    @Resource(name = "agencyService")
    private AgencyService agencyService;
    @Resource(name = "integralService")
    private IntegralService integralService;
    @Resource(name = "balanceService")
    private BalanceService balanceService;
    @Resource
    private CustomerNewService customerNewService;


    /**
     *
     * @param store_id 店ID
     * @param pageNum 页码
     * @return
     */
    @RequestMapping(value = "findShortLetter",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findShortLetter(String store_id,String pageNum){
        logBefore(logger,"查询短信记录");
        PageData pd=this.getPageData();
        Page page=new Page();
        if(store_id==null&&store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数哦");
        }else{
            try{
                String message="正确返回数据!";
                if (pageNum == null || pageNum.length() == 0) {
                    pageNum = "1";
                }
                page.setPd(pd);
                page.setShowCount(10);
                page.setCurrentPage(Integer.parseInt(pageNum));
                List<PageData> list=shortLetterService.datalistPage(page);
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
     * @param name  名字
     * @param phone 电话
     * @param address 地址
     * @param crop 作物
     * @param area 面积
     * @param  customer_id 用户ID
     * @param img 头像
     * @param IStatus 判断是否修改了图片 0没有(没修改就把图片路径传过来) 1修改了 2只删除了图片 3是网页
     * @param status 0启用 1停用
     * @return
     */
    @RequestMapping(value="editCustomer",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editCustomer(String name,String phone,String address,
                               String crop,String area,String customer_id,String img,String IStatus,String status){
        logBefore(logger,"修改客户");
        PageData pd=this.getPageData();
        ObjectMapper mapper=new ObjectMapper();
        if(name==null||name.length()==0||phone==null||phone.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数哦");
        }else{
            String message="正确返回数据!";
            try{
                /*PageData pdCustomer=customerService.findById(pd);
                if(pdCustomer.getString("status").equals("3")){
                    pd.clear();
                    pd.put("code","2");
                    pd.put("message","零散客户不可修改!");
                    return mapper.writeValueAsString(pd);
                }*/
                List<PageData> listName=customerService.findCustomerName(pd);
                if(listName!=null&&listName.size()!=0){
                    pd.clear();
                    pd.put("code","2");
                    pd.put("message","客户名称已存在!");
                    return mapper.writeValueAsString(pd);
                }
                List<PageData> listPhone=customerService.findCustomerPhone(pd);
                if(listPhone!=null&&listPhone.size()!=0){
                    pd.clear();
                    pd.put("code","2");
                    pd.put("message","客户手机号已存在!");
                    pd.put("customer_id",listPhone.get(0).get("customer_id").toString());
                    return mapper.writeValueAsString(pd);
                }
                if(IStatus!=null){
                    if(IStatus.equals("1")){
                        String ffile1 = this.get32UUID() + ".jpg";
                        String filePath2 = PathUtil.getClasspath() + Const.FILEPATHIMG + "touxiang/" + DateUtil.getDays(); // 文件上传路径
                        File file = new File(filePath2, ffile1);
                        if (!file.exists()) {
                            if (!file.getParentFile().exists()) {
                                file.getParentFile().mkdirs();
                            }
                        }
                        String ffile = this.get32UUID() + ".jpg";
                        String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG +"touxiang/"+DateUtil.getDays()+"/"+ ffile; // 文件上传路径
                        boolean flag = ImageAnd64Binary.generateImage(img, filePath);
                        img = Const.SERVERPATH + Const.FILEPATHIMG+"touxiang/"+DateUtil.getDays()+"/"+ ffile;
                        System.out.println(">>>>"+img);
                    }else if(IStatus.equals("0")){
                        img=img;
                    }else if(IStatus.equals("2")){
                        img="";
                    }
                }else{
                    img="";
                }
                pd.put("IStatus",IStatus);
                pd.put("img",img);
                customerService.updateCustomer(pd);
                pd.clear();
                pd.put("code","1");
                pd.put("message",message);
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!!");
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
     * 查询客户
     * @param store_id  店面ID
     * @param pageNum   页码
     * @param status    排序 0最近下单时间 1最早开单人 2录入时间
     * @param state1 0 不判断  1判断显示停用
     * @param state2 0 不判断  1判断欠款用户
     * @param state3 0 不判断  1判断预付款
     * @param address 地址
     * @return
     */
    @RequestMapping(value = "findCustomer",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findCustomer(String store_id,String pageNum,String status,String state1,
                               String state2,String state3,String address,String phone){
        logBefore(logger,"查询客户");
        PageData pd=this.getPageData();
        /*Page page=new Page();*/
        java.text.DecimalFormat df = new java.text.DecimalFormat("##########.00");
        if(store_id==null||store_id.length()==0||status==null||status.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try {
                System.out.println(">>>>>>>>>>>>接收的参数:"+pd.toString());
                String message="正确返回数据!";
                /*if (pageNum == null || pageNum.length() == 0) {
                    pageNum = "1";
                }*/
                //page.setShowCount(10);
                //page.setCurrentPage(Integer.parseInt(pageNum));
                Integer pageNumber=1;
                Integer pageSize = 10;
                if(pageNum!=null){
                    pageNumber =Integer.valueOf(pageNum);
                }

                PageHelper.startPage(pageNumber,pageSize);
                System.out.println(JSONObject.toJSONString(pd));
                List<PageData> list=customerService.pageinfo(pd);
                PageInfo pageInfo = new PageInfo(list);
                if(state3!=null&&state3.equals("1")){
                    Iterator<PageData> iterator = list.iterator();
                    while(iterator.hasNext()){
                        PageData integer = iterator.next();
                        PageData up = new PageData();
                        up.put("user_id",integer.get("customer_id"));
                        PageData ison = balanceService.findUserbalanceByUserId(up);
                        if(ison==null||new BigDecimal(ison.get("balance").toString()).compareTo(new BigDecimal(0))<1){
                            //list.remove(integer);
                            iterator.remove();
                        }

                    }
                }
                BigDecimal totlepayment = new BigDecimal(0);
                for (int i = 0; i < list.size(); i++) {
                    PageData pageData = list.get(i);
                    Object customer_id = pageData.get("customer_id");
                    PageData pd1 = new PageData();
                    pd1.put("customer_id",customer_id);
                    PageData sumdiscountMoney = orderInfoService.findSumdiscountMoney(pd1);
                    PageData sumMoney = orderInfoService.findSumMoney(pd1);
                    if (sumdiscountMoney!=null && !"".equals(sumdiscountMoney)) {
                        Object discountmoney = sumdiscountMoney.get("discountmoney");
                        pageData.put("discountmoney",discountmoney);
                    }else {
                        pageData.put("discountmoney",String.valueOf(new BigDecimal(0).setScale(2,BigDecimal.ROUND_HALF_UP)));
                    }
                    if (sumMoney!=null && !"".equals(sumMoney)) {
                        Object money = sumMoney.get("money");
                        pageData.put("totlemoney",String.valueOf(new BigDecimal(money.toString()).setScale(2,BigDecimal.ROUND_HALF_UP)));
                    }else {
                        pageData.put("totlemoney",String.valueOf(new BigDecimal(0).setScale(2,BigDecimal.ROUND_HALF_UP)));
                    }
                    pageData.put("owe",String.valueOf(new BigDecimal(pageData.get("owe").toString()).setScale(2,BigDecimal.ROUND_HALF_UP)));
                    //查询用户积分
                    PageData pageuser = integralService.findUserIntegralByUserid(pageData);
                    if(pageuser!=null){
                        list.get(i).put("integral",pageuser.get("remain_integral"));
                    }else {
                        list.get(i).put("integral",new BigDecimal(0));
                    }
                    //查询用户账户余额
                    if(pageData.get("customer_id")==null){
                        return getMessage("-101","客户id不能为空");
                    }
                    PageData ub = new PageData();
                    ub.put("user_id",pageData.get("customer_id").toString());
                    PageData balance = balanceService.findUserbalanceByUserId(ub);
                    if(balance==null){
                        PageData customer = customerService.findById(pageData);
                        PageData pda = new PageData();
                        pda.put("name",customer.get("name"));
                        pda.put("balance",new BigDecimal(0));
                        pda.put("user_id",pageData.get("customer_id"));
                        //balanceService.saveBalance(pda);
                        list.get(i).put("prepayment",new BigDecimal(0));
                    }else {
                        list.get(i).put("prepayment",balance.get("balance"));
                        totlepayment =totlepayment.add(new BigDecimal(balance.get("balance").toString()));
                    }
                }
                /*for (PageData pageData : list) {
                }*/
                if(pageNum.equals("1")){
                    PageData pd_c=customerService.findLingSan(pd);
                    if(null!=pd_c&&pd_c.size()>0){
                        list.add(0,pd_c);
                    }
                }
                PageData pd_c=customerService.findCount(pd);
                String money="0.00";
                PageData pd_m=customerService.findSumOwe(pd);
                if(pd_m!=null){
                    if(!pd_m.get("total_owe").toString().equals("0.0")){
                        money=String.valueOf(new BigDecimal(pd_m.get("total_owe").toString()).setScale(2,BigDecimal.ROUND_HALF_UP));
                    }
                }
                Map<String, Object> map = new HashedMap();
                for (int i = 0; i < list.size(); i++) {
                    if(!list.get(i).containsKey("role")){
                        list.get(i).put("role","0");
                    }
                    if(list.get(i).containsKey("role")&&list.get(i).get("role")!=null){
                        list.get(i).put("role",list.get(i).get("role").toString());
                    }
                }
                /*if (page.getCurrentPage() == Integer.parseInt(pageNum)) {
                    map.put("data", list);
                }else{
                    map.put("message",message);
                    map.put("data",list);
                }*/
                map.put("message",message);
                map.put("data",list);
                pd.clear();
                pd.put("object", map);
                pd.put("message", message);
                pd.put("code", "1");
                pd.put("pageTotal",pageInfo.getPages());
                System.out.println(">>>>"+JSONObject.toJSONString(pageInfo));
                pd.put("count",pd_c.get("count").toString());
                pd.put("money",money);
                pd.put("totlepayment",totlepayment);
            } catch (Exception e) {
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
     * 新增客户
     * @param name  客户名称
     * @param phone 客户电话
     * @param address  客户地址(填选)
     * @param crop  种植作物(填选)
     * @param area  种植面积(填选)
     * @param store_id 表示属于那个用户
     * @param uid  用户id
     * @param province 省
     * @param city 市
     * @param county 区
     * @param street 街道
     * @param img 头像
     * @param identity 身份证
     * @param remarks 备注
     * @return
     */
    @RequestMapping(value = "saveCustomer",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveCustomer(String name,String phone,String address,String crop,String area,
                               String store_id,String uid,String province,String city,
                               String county,String street,String img,String identity,String remarks,Integer level){
        logBefore(logger,"新增客户");
        PageData pd=this.getPageData();
        String status="1";
        ObjectMapper mapper=new ObjectMapper();
        if(name==null||name.length()==0||phone==null||phone.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数哦");
        }else{
            String message="正确返回数据!";
            try{
                List<PageData> listName=customerService.findCustomerName(pd);
                if(listName!=null&&listName.size()!=0){
                    pd.clear();
                    pd.put("code","2");
                    pd.put("message","该客户名已存在,建议名字后面加村.");
                    return mapper.writeValueAsString(pd);
                }
                List<PageData> listPhone=customerService.findCustomerPhone(pd);
                if(listPhone!=null&&listPhone.size()!=0){
                    pd.clear();
                    pd.put("code","2");
                    pd.put("message","客户手机号已存在!");
                    pd.put("customer_id",listPhone.get(0).get("customer_id").toString());
                    return mapper.writeValueAsString(pd);
                }
                if(img == null) {
                    img = "";
                } else if(img.contains("http")){
                    pd.put("img",img);
                } else {
                     if (img != null && !"".equals(img)) {
                         String ffile1 = this.get32UUID() + ".jpg";
                         String filePath2 = PathUtil.getClasspath() + Const.FILEPATHIMG + "touxiang/" + DateUtil.getDays(); // 文件上传路径
                         File file = new File(filePath2, ffile1);
                         if (!file.exists()) {
                             if (!file.getParentFile().exists()) {
                                 file.getParentFile().mkdirs();
                             }
                         }
                         String ffile = this.get32UUID() + ".jpg";
                         String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + "touxiang/" + DateUtil.getDays() + "/" + ffile; // 文件上传路径
                         boolean flag = ImageAnd64Binary.generateImage(img, filePath);
                         img = Const.SERVERPATH + Const.FILEPATHIMG + "touxiang/" + DateUtil.getDays() + "/" + ffile;
                     } else {
                         img = "";
                     }
                 }
                System.out.println(">>>> customer img= "+img);
                pd.put("input_date", DateUtil.getTime());
                pd.put("billing_date","");
                pd.put("owe","0");
                pd.put("status","0");
                if(province==null||province.length()==0){
                    pd.put("province","");
                }
                if(city==null||city.length()==0){
                    pd.put("city","");
                }
                if(county==null||county.length()==0){
                    pd.put("county","");
                }
                if(street==null||street.length()==0){
                    pd.put("street","");
                }
                if(remarks==null||remarks.length()==0){
                    pd.put("remarks","");
                }
                pd.put("img",img);
                if(identity==null||identity.length()==0){
                    pd.put("identity","");
                }
                while (true){
                    Integer number = NumberUtil.getNumber();
                    Customer customer = new Customer();
                    customer.setNumber(number);
                    Customer bean = customerNewService.selectByNumber(customer);
                    if(bean==null){
                        pd.put("number",number);
                        break;
                    }
                }
                customerService.save(pd);
                PageData pda = new PageData();
                pda.put("store_id",store_id);
                pda.put("store_id",phone);
                List<PageData> pageData = customerService.findCustomerPhone(pda);
                pd.clear();
                pd.put("code",status);
                pd.put("message",message);
                if(pageData.size()>0){
                    pd.put("customer",pageData.get(pageData.size()-1).get("customer_id").toString());
                }
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!!");
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
     * 查询客户详情
     * @param customer_id   客户ID
     * @return
     */
    @RequestMapping(value = "findCustomerId",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findCustomerId(String customer_id){
        logBefore(logger,"查询客户详情");
        PageData pd=this.getPageData();
        if(customer_id==null||customer_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                pd.put("return_goods","0");
                PageData pd_c=customerService.findById(pd);
                PageData pd_m=orderInfoService.findSumMoney(pd);
                PageData pd_d=orderInfoService.findSumdiscountMoney(pd);
                if(pd_m!=null){
                    pd_c.put("total",String.valueOf(new BigDecimal(pd_m.get("money").toString()).setScale(2,BigDecimal.ROUND_HALF_UP)));
                }else {
                    pd_c.put("total",String.valueOf(new BigDecimal(0).setScale(2)));
                }
                if(pd_d!=null){
                    pd_c.put("discountmoney",String.valueOf(new BigDecimal(pd_d.get("discountmoney").toString()).setScale(2,BigDecimal.ROUND_HALF_UP)));
                }else {
                    pd_c.put("discountmoney", String.valueOf(new BigDecimal(0).setScale(2)));
                }
                pd_c.put("owe",String.valueOf(new BigDecimal(pd_c.get("owe").toString()).setScale(2,BigDecimal.ROUND_HALF_UP)));
                PageData pdx = new PageData();
                pdx.put("customer_id",customer_id);
                PageData pageData = integralService.findUserIntegralByUserid(pdx);
                if(pageData!=null){
                    pd_c.put("integral",pageData.get("remain_integral"));
                }else {
                    pd_c.put("integral",new BigDecimal(0));
                }
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
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
     *
     * @param data 客户数据源（name：姓名phone 电话）
     * @param store_id 表示属于那个店
     * @return
     */
    @RequestMapping(value = "batchSaveCustomer",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String batchCustomer(String data,String store_id,String uid){
        logBefore(logger,"批量插入");
        PageData pd=this.getPageData();
        if(data==null||data.length()==0||store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
           try{
               Gson gson = new Gson();
               logBefore(logger,"没有处理过参数"+data);
               String myJson=gson.toJson(data);
               logBefore(logger,"处理过的参数"+myJson);
               myJson = DateUtil.delHTMLTag(data);
               myJson = myJson.replace("\r", "");
               myJson = myJson.replace("\n", "");
               myJson = myJson.replace("\\", "");
               logBefore(logger,"再次处理过的参数"+myJson);
               List<PageData> list = gson.fromJson(myJson, new TypeToken<List<PageData>>() {
               }.getType());
               customerService.batchSaveCustomer(list,store_id,DateUtil.getTime(),uid);
               quchong(pd);
               quchongs(pd);
               pd.clear();
               pd.put("code","1");
               pd.put("message","正确返回数据!");
           } catch (Exception e){
               logBefore(logger,"批量插入出错,"+e.toString()+"参数:"+data);
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
     * 模糊查询客户
     * @param store_id  店面ID
     * @param name  客户名称
     * @return
     */
    @RequestMapping(value = "findLikeCustomer",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findLikeCustomer(String name,String pageNum,String store_id){
        logBefore(logger,"模糊查询客户");
        PageData pd=this.getPageData();
        if(name==null||name.length()==0||store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                Map<String,Object> map=new HashMap<String, Object>();
                List<PageData> list=customerService.findLikename(pd);
                String money="0.0";
                PageData pd_m=customerService.findSumOwe(pd);
                if(pd_m!=null){
                    if(!pd_m.get("total_owe").toString().equals("0.0")){
                        money=df.format(pd_m.get("total_owe"));
                    }
                }
                //查询用户账户余额
                for (int i = 0; i < list.size(); i++) {
                    PageData ub = new PageData();
                    PageData pageData = list.get(i);
                    ub.put("user_id",list.get(i).get("customer_id"));
                    ub.put("customer_id",list.get(i).get("customer_id"));
                    if(ub.get("customer_id")==null){
                        return getMessage("-101","客户id不能为空");
                    }
                    PageData balance = balanceService.findUserbalanceByUserId(ub);
                    if(balance==null){
                        PageData customer = customerService.findById(ub);
                        PageData pda = new PageData();
                        pda.put("name",customer.get("name"));
                        pda.put("balance",new BigDecimal(0));
                        pda.put("user_id",ub.get("customer_id"));
                        //balanceService.saveBalance(pda);
                        list.get(i).put("prepayment",new BigDecimal(0));
                    }else {
                        list.get(i).put("prepayment",balance.get("balance"));
                    }

                    //查询用户积分
                    PageData pageuser = integralService.findUserIntegralByUserid(pageData);
                    if(pageuser!=null){
                        list.get(i).put("integral",pageuser.get("remain_integral"));
                    }else {
                        list.get(i).put("integral",new BigDecimal(0));
                    }
                }
                map.put("data",list);
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("object",map);
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
     * 查询全部客户
     * @param store_id  店面ID
     * @return
     */
    @RequestMapping(value = "findCustomerList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findCustomerList(String store_id){
        logBefore(logger,"查询全部客户");
        PageData pd=this.getPageData();
        if(store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                Map<String,Object> map=new HashMap<String, Object>();
                List<PageData> list=customerService.findCustomerList(pd);
                map.put("data",list);
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("object",map);
            }catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","正确返回数据!");
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
     * 查询代收款的客户
     * @param store_id  店面ID
     * @param pageNum   页码
     * @return
     */
    @RequestMapping(value = "findOweCustomer",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findOweCustomer(String store_id,String pageNum){
        logBefore(logger,"查询代收款客户");
        PageData pd=this.getPageData();
        Page page=new Page();
        if(store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try{
                String message="正确返回数据!";
                if (pageNum == null || pageNum.length() == 0) {
                    pageNum = "1";
                }
                page.setPd(pd);
                page.setShowCount(10);
                page.setCurrentPage(Integer.parseInt(pageNum));
                List<PageData> list=customerService.owelistPage(page);
                java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                for(int i=0;i<list.size();i++){
                    list.get(i).put("totlemoney",df.format(list.get(i).get("totlemoney")));
                }
                pd.put("state2","1");
                String money1="0.0";
                PageData pdSum=customerService.findSumOwe(pd);
                if(pdSum!=null){
                    money1=df.format(pdSum.get("total_owe"));
                }
                Map<String, Object> map = new HashedMap();
                if (page.getCurrentPage() == Integer.parseInt(pageNum)) {
                    map.put("data", list);
                }else{
                    map.put("message",message);
                    map.put("data",new ArrayList());
                }
                for(int i=0;i<list.size();i++){
                    list.get(i).put("return_date","0");
                    PageData pd_sum=orderInfoService.findSumOweMoney(list.get(i));
                    String money="0";
                    if(pd_sum!=null){
                        money=pd_sum.get("money").toString();
                    }
                    list.get(i).put("money",money);
                }
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("object",map);
                pd.put("pageTotal",String.valueOf(page.getTotalPage()));
                pd.put("money",money1);
            } catch (Exception e){
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
     * 查询代收款的客户
     * @param store_id  店面ID
     * @param  name 模糊查询
     * @return
     */
    @RequestMapping(value = "findlikeOweCustomer",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findlikeOweCustomer(String store_id,String name){
        logBefore(logger,"模糊查询代收款");
        PageData pd=this.getPageData();
        if(store_id==null||store_id.length()==0||name==null||name.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try{
                java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
                List<PageData> list=customerService.findLikeowelist(pd);
                for(int i=0;i<list.size();i++){
                    PageData pd_sum=orderInfoService.findSumOweMoney(list.get(i));
                    String money="0";
                    if(pd_sum!=null){
                        money=pd_sum.get("money").toString();
                    }
                    list.get(i).put("money",money);
                    list.get(i).put("totlemoney",df.format(list.get(i).get("totlemoney")));
                }
                pd.put("state2","1");
                String money1="0.0";
                PageData pdSum=customerService.findSumOwe(pd);
                if(pdSum!=null){
                    money1=df.format(pdSum.get("total_owe"));
                }
                Map<String, Object> map = new HashedMap();
                map.put("data", list);
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("object",map);
                pd.put("money",money1);
            } catch (Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
                logBefore(logger,"===================群发短息出错===================="+e.toString());
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
     * @param customer_id 客户ID
     * @param status 0停用 1启用
     * @return
     */
    @RequestMapping(value ="deleteCustomer",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String deleteCustomer(String customer_id,String status){
        logBefore(logger,"删除客户");
        PageData pd=this.getPageData();
        if(customer_id==null||customer_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try{
                if(status!=null&&status.equals("1")){
                    pd.put("status","0");
                }else{
                    pd.put("status","1");
                }
                customerService.updateStatus(pd);   //逻辑删除
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }catch (Exception e){
                pd.clear();
                pd.put("cide","2");
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
     * @param data 手机号
     * @param message 内容
     * @param count 数量
     * @param store_id 店铺id
     * @param  customer 客户数据源
     * @return
     */
    @RequestMapping(value = "MassShort",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String MassShort(String data,String message,String count,String store_id,String customer){
        logBefore(logger,"群发短信");
        PageData pd=this.getPageData();
        System.out.println(">>>>>>"+ com.alibaba.fastjson.JSONObject.toJSONString(pd));
        String messages="正确返回数据!";
        String code="1";
        if(message==null||message.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","正确返回数据!");
        }else{
            try {
                PageData pd_c=storeService.findById(pd);
                count = MessageSend.getCount(message).toString();
                if(Integer.valueOf(pd_c.get("number").toString())>Integer.valueOf(count)){
                    SmsBao sms=new SmsBao();
                    String result= sms.sendSMSstord(data,message,pd_c.getString("name"));
                    SendMessageResult messageResult = MessageSend.sendMessage(data,"【"+pd_c.get("name").toString()+"】"+message+",回T退订");
                    System.out.println(">>>>>>返回的数据是"+ JSONObject.toJSONString(messageResult));

                    if(messageResult.getResult().equals(0)){
                        pd_c.put("count",count);
                        storeService.updateNumber(pd);
                        PageData shortletter=new PageData();
                        shortletter.put("message",message);
                        shortletter.put("store_id",store_id);
                        shortletter.put("customer",customer);
                        shortletter.put("date",DateUtil.getTime());
                        shortletter.put("number",count);
                        shortletter.put("phone",data);
                        shortLetterService.save(shortletter);
                    }else {
                        code = "2";
                        messages = "发送失败!";
                    }
                }else{
                    code = "2";
                    messages = "短信不够!";
                }
                PageData pd_cc=storeService.findById(pd);
                pd.clear();
                pd.put("code",code);
                pd.put("message",messages);
                pd.put("number",pd_cc.get("number").toString());
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
        logger.error(str);
        return str;
    }


    //去重方法
    public void quchong(PageData pd){
        try{
            List<PageData> repeat_list=customerService.findRepeatCustomer(pd);  //查询重复客户
            if(repeat_list.size()!=0){
                customerService.batchPpdate(repeat_list);  //删除重复
                List<PageData> list=customerService.findRepeatCustomer(pd);  //查询重复客户
                if(list.size()!=0){     //如果还有就递归此方法
                    quchong(pd);
                }
            }else{
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //去重方法
    public void quchongs(PageData pd){
        try{
            List<PageData> repeat_list=customerService.findRepeatCustomerPhone(pd);  //查询重复客户
            if(repeat_list.size()!=0){
                customerService.batchPpdate(repeat_list);  //删除重复
                List<PageData> list=customerService.findRepeatCustomerPhone(pd);  //查询重复客户
                if(list.size()!=0){     //如果还有就递归此方法
                    quchongs(pd);
                }
            }else{
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 查询未购买或购买过的客户
     * @param store_id 店ID
     * @param status 状态 1一个月  2三个月  3半年  4一年
     * @param pageNum 页码
     * @param cstatus 1 未购买 2以购买
     * @return
     */
    @RequestMapping(value = "findPurchaseList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findPurchaseList(String store_id,String status,String pageNum,String cstatus){
        logBefore(logger,"查询未购买或购买过的客户");
        PageData pd=this.getPageData();
        if(store_id==null||status.length()==0||status==null||status.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","正确返回数据!");
        }else{
            try{
                String message="正确返回数据!";
                Integer SHU1 = Integer.valueOf(pageNum) * 10;
                pd.put("SHU1", SHU1 - 10);
                Map<String,Object> map=DateUtil.ThisMonth();
                if(status.equals("1")){
                    pd.put("date",map.get("firstDay"));
                }else if(status.equals("2")){
                    pd.put("date",map.get("lastDay"));
                }else if(status.equals("3")){
                    pd.put("date",map.get("first"));
                }else if(status.equals("4")){
                    pd.put("date",map.get("nian"));
                }
                List<PageData> list=customerService.findpurchaselist(pd);
                for(int i=0;i<list.size();i++){
                    List<PageData> lists= agencyService.findCustomer(list.get(i));
                    if(lists!=null&&lists.size()!=0){
                        list.get(i).put("status","1");
                    }else{
                        list.get(i).put("status","0");
                    }
                }
                PageData pd_c=customerService.Purchaselistcount(pd);
                Integer pageTotal;
                if (Integer.valueOf(pd_c.get("count").toString()) % 10 == 0) {
                    pageTotal = Integer.valueOf(pd_c.get("count").toString()) / 10;
                } else {
                    pageTotal = Integer.valueOf(pd_c.get("count").toString()) / 10 + 1;
                }
                Map<String, Object> maps = new HashedMap();
                maps.put("data",list);
                pd.clear();
                pd.put("object",maps);
                pd.put("code","1");
                pd.put("message",message);
                pd.put("pageTotal",pageTotal);
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

    @RequestMapping(value = "findCustomers",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findCustomers(String store_id){
        logBefore(logger,"查询店铺的客户数量和总欠款");
        PageData pd=this.getPageData();
        try {
            PageData pd_c=customerService.findCount(pd);
            PageData pd_m=customerService.findSumOwe(pd);
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("count",pd_c.get("count").toString());
            pd.put("money",pd_m.get("total_owe").toString());
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

    @RequestMapping(value = "findQuanBu",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findQuanBu(String store_id){
        logBefore(logger,"查询全部用户");
        PageData pd=this.getPageData();
        try{
            List<PageData> list=customerService.findQuanBuList(pd);
            pd.clear();
            pd.put("data",list);
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

    /*@RequestMapping(value = "ScreenCustome",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ScreenCustome(String status) {
        logBefore(logger, "筛选客户");
        PageData pd=this.getPageData();
        try{

        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
        }
    }*/
}
