package com.mtnz.controller.app.agency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtnz.controller.app.store.model.StoreLose;
import com.mtnz.controller.base.BaseController;
import com.mtnz.service.system.agency.AgencyService;
import com.mtnz.service.system.banner.BannerService;
import com.mtnz.service.system.store.StoreService;
import com.mtnz.service.system.sys_app_user.SysAppUserService;
import com.mtnz.util.DateUtil;
import com.mtnz.util.PageData;
import com.mtnz.util.WPush;
import org.apache.poi.util.SystemOutLogger;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.xml.rpc.ServiceException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/*
    Created by xxj on 20  18\3  \23 0023.
 */
@Controller
@RequestMapping(value = "app/agency",produces = "text/html;charset=UTF-8")
public class AppAgencyController extends BaseController{

    @Resource(name = "agencyService")
    private AgencyService agencyService;
    @Resource(name="bannerService")
    private BannerService bannerService;
    @Resource(name = "sysAppUserService")
    private SysAppUserService sysAppUserService;
    @Resource
    private StoreService storeService;


    /**
     *
     * @param agency_id ID
     * @param agency 事项
     * @param month 月
     * @param hour 小时
     * @param name 名字
     * @return
     */
    @RequestMapping(value = "updateAgency",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String updateAgency(String agency_id,String agency,String month,String hour,String name){
        logBefore(logger,"修改事件详情");
        PageData pd=this.getPageData();
        if(agency_id==null||agency_id.length()==0||agency==null||agency.length()==0||
                month==null||month.length()==0||hour==null||hour.length()==0||name==null||name.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try{
                agencyService.edit(pd);
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }catch (Exception e){
                pd.clear();
                pd.put("ode","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * @param agency_id 事件ID
     * @param status 1删除  2完成
     * @return
     */
    @RequestMapping(value = "deleteAgency",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String deleteAgency(String agency_id,String status){
        logBefore(logger,"删除事件详情");
        PageData pd=this.getPageData();
        if(agency_id==null||agency_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try{
                if("1".equals(status)){
                    agencyService.delete(pd);
                }else{
                    agencyService.editStatus(pd);
                }
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }catch(Exception e){
                pd.clear();
                pd.put("code","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * @param uid   id
     * @param agency    事项
     * @param month  那一天
     * @param hour 小时
     * @param name 姓名
     * @param
     * @return
     */
    @RequestMapping(value = "saveAgency",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveAgency(String uid,String agency,String month,String hour,String name,String customer_id){
        logBefore(logger,"添加代办事项");
        PageData pd=this.getPageData();
        if(uid==null||uid.length()==0||agency==null||agency.length()==0||
                month==null||month.length()==0||hour==null||hour.length()==0||name==null||name.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try{
                PageData pd_c=sysAppUserService.findById(pd);
                pd.put("openid",pd_c.getString("openid"));
                pd.put("date",DateUtil.getTime());
                pd.put("fstatus","0");
                if(customer_id==null){
                    //查询用户id
                    PageData pds = new PageData();
                    pds.put("name",name);
                    List<PageData> us = sysAppUserService.findcostor(pds);
                    if(us.size()<1){
                        throw new ServiceException("用户未找到");
                    }
                }else{
                    pd.put("customer_id",customer_id);
                }
                agencyService.save(pd);
                PageData pd_a=agencyService.findById(pd);
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("data",pd_a);
            }catch (Exception e){
                pd.clear();
                pd.put("ode","2");
                pd.put("message","程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @RequestMapping(value = "findAgencyId",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findAgencyId(String agency_id){
        logBefore(logger,"查询事件详情");
        PageData pd=this.getPageData();
        if(agency_id==null||agency_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try{
                PageData pd_a=agencyService.findById(pd);
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("data",pd_a);
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
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * @param uid 用户ID
     * @param month 时间模版(2018-03-04)
     * @return
     */
    @RequestMapping(value = "findMonthList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findMonthList(String uid,String month){
        logBefore(logger,"根据时间查询代办事项");
        PageData pd=this.getPageData();
        if(uid==null||uid.length()==0||month==null||month.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try{
                List<PageData> list=agencyService.findUserList(pd);
                pd.put("month",month);
                pd.put("months",DateUtil.getSpecifiedDayBefore(pd.getString("month"),1));
                List<PageData> Yesterday=agencyService.findBeforeList(pd);
                pd.put("month",DateUtil.getSpecifiedDayBefore(pd.getString("month"),1));
                pd.put("months",DateUtil.getSpecifiedDayBefore(pd.getString("month"),1));
                List<PageData> before=agencyService.findBeforeList(pd);
                pd.clear();
                pd.put("code","1");
                pd.put("list",list);
                pd.put("message","正确返回数据!");
                pd.put("Yesterday",Yesterday);
                pd.put("before",before);
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
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * @param uid 用户ID
     * @return
     */
    @RequestMapping(value = "findAgency",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findAgency(String uid){
        logBefore(logger,"查询代办事项");
        PageData pd=this.getPageData();
        if(uid==null||uid.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数!");
        }else{
            try{
                pd.put("month",DateUtil.getDay());
                pd.put("months",DateUtil.getSpecifiedDayBefore(pd.getString("month"),1));
                List<PageData> list=agencyService.findUserList(pd);
                List<PageData> month_list=agencyService.findMonthList(pd);
                List<PageData> Yesterday=agencyService.findBeforeList(pd);
                pd.put("month",DateUtil.getSpecifiedDayBefore(pd.getString("month"),1));
                pd.put("months",DateUtil.getSpecifiedDayBefore(pd.getString("month"),1));
                List<PageData> before=agencyService.findBeforeList(pd);
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("month_list",month_list);
                pd.put("list",list);
                pd.put("Yesterday",Yesterday);
                pd.put("before",before);
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
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     *
     * @param uid 用户id
     * @return
     */
    @RequestMapping(value = "findAgencyCount",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String  findAgencyCount(String uid,Long store_id){
        logBefore(logger,"查询代办事项和首页版图");
        System.out.println(">>>>>>>uid:"+uid + "store_id:" + store_id);
        PageData pd=this.getPageData();
        if(uid==null||uid.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","缺少参数");
        }else{
            try{
                if(store_id!=null){
                    pd.put("store_id",store_id);
                }
                pd.put("month",DateUtil.getDay());
                PageData pd_c=agencyService.findCount(pd);
                List<PageData> list=bannerService.findList(uid, pd);
                System.out.println(pd);
                PageData pd_s=sysAppUserService.findBySId(pd);

                StoreLose storeLose = new StoreLose();
                storeLose.setStoreId(Long.valueOf(store_id));
                StoreLose selectLose = storeService.selectLose(storeLose);
                pd.clear();
                if(selectLose!=null){
                    pd.put("lose",selectLose);
                }else {
                    storeLose.setStatus(1);
                    storeLose.setAutomatic(1);
                    pd.put("lose",storeLose);
                }
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("count",pd_c.get("count").toString());
                pd.put("data",list);
                if (pd_s != null) {
                    pd.put("name", pd_s.getString("sname"));
                    pd.put("address", pd_s.getString("saddress"));
                    pd.put("province", pd_s.getString("sprovince"));
                    pd.put("city", pd_s.getString("scity"));
                    pd.put("district", pd_s.getString("district"));
                    pd.put("county", pd_s.getString("scounty"));
                    pd.put("street", pd_s.getString("street"));
                    pd.put("phone", pd_s.getString("sphone"));
                    pd.put("openid", pd_s.getString("openid"));

                    if (pd_s.getString("qr_code") == null) {
                        pd.put("qr_code", "");
                    } else {
                        pd.put("qr_code", pd_s.getString("qr_code"));
                    }

                    pd.put("business_img", pd_s.getString("business_img"));
                }
            }catch (Exception e) {
                pd.clear();
                pd.put("code", "2");
                pd.put("message", "程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper=new ObjectMapper();
        String str="";
        try {
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }



 /*   @RequestMapping(value = "findAgency",produces = "text/html;charset=UTF-8")
    @ResponseBody
     public String findAgency(String uid){
        logBefore(logger,);
    }*/


    int count=1;
    /**
     * 查询代办事项发送提醒
     */
    public void execute(){
        logBefore(logger,"查询代办事项发送提醒");
        PageData pd=new PageData();
        try{
            String hour =DateUtil.dealZeroToNine();
            pd.put("hour",hour);
            pd.put("month",DateUtil.getDay());
            List<PageData> list=agencyService.findList(pd);
            URL url = null;
            url = new URL("https://www.meitiannongzi.com/under/app/SendRemind");
            BufferedReader in = null;
            String inputLine = "";
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            inputLine = new String(in.readLine());
            System.out.println("返回值====================>" + inputLine);
            JSONObject json = new JSONObject(inputLine);
            String token = json.getString("token");
            for(int i=0,len=list.size();i<len;i++){
                if(!"".equals(list.get(i).getString("openid"))){
                    String success=WPush.wpush(list.get(i).getString("openid"),token);
                    if(success!="success"){
                        //怕进入死循环加个条件
                        if(count<5){
                            count++;
                            execute();
                        }else{
                            agencyService.editfStatus(list.get(i));
                            count=1;
                        }
                    }else{
                        agencyService.editfStatus(list.get(i));
                        count=1;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

  /*  public static void main(String[] args) {
        AppAgencyController aa=new AppAgencyController();
        aa.execute();
    }*/



}
