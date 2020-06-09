package com.mtnz.controller.app.store;

/*
    Created by xxj on 2018\4\18 0018.  
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtnz.controller.app.store.model.StoreLose;
import com.mtnz.controller.base.BaseController;
import com.mtnz.entity.Page;
import com.mtnz.service.system.already.AlreadyService;
import com.mtnz.service.system.customer.CustomerService;
import com.mtnz.service.system.person.PersonService;
import com.mtnz.service.system.recharge.RechargeService;
import com.mtnz.service.system.shorts.ShortService;
import com.mtnz.service.system.store.StoreService;
import com.mtnz.service.system.sys_app_user.SysAppUserService;
import com.mtnz.service.system.user.LoginSaltService;
import com.mtnz.service.system.user.UserService;
import com.mtnz.util.*;
import com.mtnz.util.utils.GetWxOrderno;
import com.mtnz.util.utils.RequestHandler;
import com.mtnz.util.utils.TenpayUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "app/store",produces = "text/html;charset=UTF-8")
public class AppStoreController extends BaseController{

    @Resource(name = "storeService")
    private StoreService storeService;
    @Resource(name = "sysAppUserService")
    private SysAppUserService sysAppUserService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "shortService")
    private ShortService shortService;
    @Resource(name = "personService")
    private PersonService personService;
    @Resource(name = "rechargeService")
    private RechargeService rechargeService;
    @Resource(name = "alreadyService")
    private AlreadyService alreadyService;
    @Resource(name = "customerService")
    public CustomerService customerService;
    @Resource
    private LoginSaltService loginSaltService;

    /**
     *
     * @param status 修改status
     * @param short_id 短信Id
     * @param message 内容
     * @param reason 不通过原因
     * @return
     */
    @RequestMapping(value = "editShort",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editShort(String status,String short_id,String message,String reason){
        logBefore(logger,"修改短息");
        PageData pd=this.getPageData();
        try{
            shortService.edit(pd);
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
     *
     * @param message 内容
     * @param USER_ID
     * @return
     */
    @RequestMapping(value = "LaoMaoshoreMessage",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String LaoMaoshoreMessage(String message,String USER_ID){
        logBefore(logger,"老猫发送短信");
        PageData pd=this.getPageData();
        try{
            pd.put("date",DateUtil.getTime());
            pd.put("status","2");
            shortService.save(pd);
            pd.put("status","0");
            List<String> list=sysAppUserService.findUserList(pd);
            String result=list.toString().trim().replaceAll("\\[","").replaceAll("]","");
            SmsBao smsBao=new SmsBao();
            smsBao.sendSMS(result.toString(),message);
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
     *
     * @param status 0是普通1是全部
     * @param USER_ID 负责人ID
     * @param message 内容
     * @param short_id 短信ID
     * @return
     */
    @RequestMapping(value = "shoreMessage",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findFaSongDuanXin(String status,String USER_ID,String message,String short_id){
        logBefore(logger,"发送短信");
        PageData pd=this.getPageData();
        try{
            List<String> list=sysAppUserService.findUserList(pd);
            String result=list.toString().trim().replaceAll("\\[","").replaceAll("]","");
            SmsBao smsBao=new SmsBao();
            smsBao.sendSMS(result.toString(),message);
            pd.put("status","1");
            shortService.edit(pd);
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
     *
     * @param status (0普通1老猫)
     * @param USER_ID 用户ID
     * @param pageNum 页码
     * @return
     */
    @RequestMapping(value = "findShort",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public synchronized String findShort(String status,String USER_ID,String pageNum){
        logBefore(logger,"查询短信");
        PageData pd=this.getPageData();
        Page page=new Page();
        try{
            if (pageNum == null || pageNum.length() == 0) {
                pageNum = "1";
            }
            page.setPd(pd);
            page.setShowCount(10);
            page.setCurrentPage(Integer.parseInt(pageNum));
            List<PageData> list=shortService.datalistPage(page);
            Map<String, Object> map = new HashedMap();
            if (page.getCurrentPage() == Integer.parseInt(pageNum)) {
                map.put("data",list);
            }else{
                map.put("data",new ArrayList());
            }
            pd.clear();
            pd.put("object",map);
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("pageTotal",String.valueOf(page.getTotalPage()));
            pd.put("totalResult",String.valueOf(page.getTotalResult()));
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
     * @param USERNAME 用户ID
     * @param PASSWORD 旧密码
     * @param XPASSWORD 新密码
     * @return
     */
    @RequestMapping(value = "editUserPassword",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editUserPassword(String USERNAME,String PASSWORD,String XPASSWORD){
        logBefore(logger,"修改用户密码");
        PageData pd=this.getPageData();
        String code="1";
        String message="正确返回数据!";
        try{
                pd.put("PASSWORD",new SimpleHash("SHA-1", USERNAME, PASSWORD).toString());
                PageData pd_uu=userService.getUserByNameAndPwd(pd);
                if(pd_uu!=null){
                    pd_uu.put("PASSWORD",new SimpleHash("SHA-1", USERNAME, XPASSWORD).toString());
                    userService.editPassword(pd_uu);
                }else{
                    code="2";
                    message="密码错误!";
                }
            pd.clear();
            pd.put("code",code);
            pd.put("message",message);
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
     * @param USER_ID 用户ID
     * @return
     */
    @RequestMapping(value = "deleteAppUser",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editAppUser(String USER_ID){
        logBefore(logger,"修改用户");
        PageData pd=this.getPageData();
        try{
            userService.delete(pd);
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
     *
     * @param USERNAME 用户名
     * @param PASSWORD 秘密
     * @param NAME 姓名
     * @param province 区
     * @param PHONE 手机号
     * @param USER_ID 用户ID
     * @return
     */
    @RequestMapping(value = "editAppUser",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editAppUser(String USERNAME,String PASSWORD,String NAME,String province,String PHONE,String USER_ID){
        logBefore(logger,"修改用户");
        PageData pd=this.getPageData();
        try{
            pd.put("PASSWORD", new SimpleHash("SHA-1", USERNAME, "123").toString()); // 默认密码123
            userService.edit(pd);
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
     *
     * @param USERNAME 用户名称
     * @param PASSWORD 密码
     * @param NAME 姓名
     * @param province 区域
     * @param PHONE 电话
     * @return
     */
    @RequestMapping(value = "saveAppUser",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveAppUser(String USERNAME,String PASSWORD,String NAME,String province,String PHONE){
        logBefore(logger,"添加队员");
        PageData pd=this.getPageData();
        try{
            PageData pd_a=userService.finAppByUId(pd);
            if(pd_a!=null){

                pd.clear();
                pd.put("code","2");
                pd.put("message","队员已存在!");
            }else{
                pd.put("USER_ID", this.get32UUID()); // ID
                pd.put("PASSWORD", new SimpleHash("SHA-1", USERNAME, "123").toString()); // 默认密码123
                pd.put("RIGHTS","");
                pd.put("ROLE_ID","");
                pd.put("LAST_LOGIN","");
                pd.put("IP","");
                pd.put("STATUS","");
                pd.put("BZ","");
                pd.put("SKIN","");
                pd.put("EMAIL","");
                pd.put("NUMBER",USERNAME);
                pd.put("PHONE",PHONE);
                userService.saveU(pd);
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }
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

    @RequestMapping(value = "findAppUserList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findAppUserList(String pageNum){
        logBefore(logger,"查询队员列表");
        Page page=new Page();
        PageData pd=this.getPageData();
        try{
            if (pageNum == null || pageNum.length() == 0) {
                pageNum = "1";
            }
            page.setPd(pd);
            page.setShowCount(10);
            page.setCurrentPage(Integer.parseInt(pageNum));
            List<PageData> list = userService.datalistPage(page);
            Map<String, Object> map = new HashedMap();
            if (page.getCurrentPage() == Integer.parseInt(pageNum)) {
                map.put("data",list);
            }else{
                map.put("data",new ArrayList());
            }
            pd.clear();
            pd.put("object",map);
            pd.put("code","1");
            pd.put("message","正确返回数据!");
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

    /**
     *
     * @param message 内容
     * @param USER_ID id
     * @return
     */
    @RequestMapping(value = "saveShort",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveShort(String message,String USER_ID){
        logBefore(logger,"添加群发短信");
        PageData pd=this.getPageData();
        try{
            pd.put("date",DateUtil.getTime());
            pd.put("status","0");
            pd.put("qr_code","");
            shortService.save(pd);
            pd.clear();
            pd.put("code","1");
            pd.put("message",message);
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
     * @param USERNAME 用户名
     * @param PASSWORD 密码
     * @return
     */
    @RequestMapping(value = "loagin",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String loagin(String USERNAME,String PASSWORD){
        logBefore(logger,"后台登陆");
        PageData pd=new PageData();
        String code="1";
        String message="正确返回数据!";
        try{
            pd.put("USERNAME", USERNAME);
            String passwd = new SimpleHash("SHA-1", USERNAME, PASSWORD).toString(); // 密码加密
            pd.put("PASSWORD", passwd);
            PageData pd1 = userService.getUserByNameAndPwd(pd);
            String USER_ID="";
            String STATUS="0";
            String NAME="";
            String province="";
            if (pd1 != null) {
                pd1.put("LAST_LOGIN", DateUtil.getTime().toString());
                userService.updateLastLogin(pd1);
                USER_ID=pd1.getString("USER_ID");
                if(!pd1.getString("RIGHTS").equals("")){
                    STATUS="1";
                }
                NAME=pd1.getString("NAME");
                province=pd1.getString("province");
            }else{
                code="2";
                message="账号或密码错误";
            }

            pd.clear();

            //给前端返回token
            String salt = "Nsakj";
            String token = Md5Util.md5("0",salt);
            pd.put("token",token);
            System.out.println(">>>>>>返回的token："+token);
            pd.put("code",code);
            pd.put("message",message);
            pd.put("USER_ID",USER_ID);
            pd.put("STATUS",STATUS); //0普通1老猫
            pd.put("NAME",NAME);
            pd.put("province",province);
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

    @RequestMapping(value = "deleteUser",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String deleteUser(String uid){
        logBefore(logger,"删除用户");
        PageData pd=this.getPageData();
        try{
            sysAppUserService.delete(pd);
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
     *
     * @param uid 用户iD
     * @param name 名称
     * @param province 省
     * @param city 市
     * @param district 区
     * @param address 地址
     * @param person_id 区域负责人ID
     * @param phone 手机号
     * @param storename 店名
     * @param store_id 店的ID
     * @param corp 种植作物
     * @return
     */
    @RequestMapping(value = "editUser",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editUser(String uid,String name,String province,String city,String district,
                           String address,String person_id,String phone,String storename,String store_id,String corp){
        logBefore(logger,"修改用户");
        PageData pd=this.getPageData();
        try{
            PageData pd_s=new PageData();
            pd_s.put("name",storename);
            pd_s.put("store_id",store_id);
            storeService.updateName(pd_s);
            sysAppUserService.update(pd);
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
     *
     * @param pageNum 页码
     * @param province 省
     * @param city 市
     * @param person_id 区域负责人ID
     * @param USER_ID 用户ID
     * @param status 0普通用户1老猫
     * @return
     */
    @RequestMapping(value = "findUserList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findUserList(String pageNum,String province,String city,String naem,String person_id,String USER_ID,String status){
        logBefore(logger,"查询用户");
        Page page=new Page();
        PageData pd=this.getPageData();
        try{
            if (null != province && !"".equals(province)) {
                province = province.trim();
                pd.put("province", province);
            }
            if (null != city && !"".equals(city)) {
                city = city.trim();
                pd.put("STATUS", city);
            }
            if (pageNum == null || pageNum.length() == 0) {
                pageNum = "1";
            }
            page.setPd(pd);
            page.setShowCount(10);
            page.setCurrentPage(Integer.parseInt(pageNum));
            List<PageData> list=sysAppUserService.datalistPage(page);
            Map<String, Object> map = new HashedMap();
            if (page.getCurrentPage() == Integer.parseInt(pageNum)) {
                map.put("data",list);
            }else{
                map.put("data",new ArrayList());
            }
            pd.clear();
            pd.put("object",map);
            pd.put("code","1");
            pd.put("message","正确返回数据!");
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


    /**
     *
     * @param username 申请账号
     * @param password 密码
     * @param name 名称
     * @param province 省
     * @param city 市
     * @param district 县
     * @param person_id 负责人ID
     * @param phone 电话
     * @param storename 店铺名称
     * @return
     */
    @RequestMapping(value = "saveUser",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveUser(String username,String password,String name,
                           String province,String city,String district,String address,String person_id,String phone,String storename){
        logBefore(logger,"添加用户");
        PageData pd=this.getPageData();
        try{
            PageData pd_u=sysAppUserService.findUserName(pd);
            if(pd_u!=null){
                pd.clear();
                pd.put("code","2");
                pd.put("message","用户名已存在");
            }else{
                PageData pd_s=new PageData();
                pd_s.put("name",storename+"的店");
                pd_s.put("number","200");
                pd_s.put("address","");
                pd_s.put("qr_code","");
                pd_s.put("phone","");
                pd_s.put("province",province);
                pd_s.put("city",city);
                pd_s.put("county",district);
                pd_s.put("street","");
                pd_s.put("business_img","");
                pd_s.put("phoneTow",phone);
                storeService.save(pd_s);    //先添加店
                pd.put("username",username);
                pd.put("salt", CharacterUtils.getRandomString(4));
                pd.put("password", MD5.md5(MD5.md5("123456") + pd.getString("salt")));
                pd.put("openid","");
                pd.put("unionid","");
                pd.put("status","0");
                pd.put("store_id",pd_s.get("store_id"));
                pd.put("date", DateUtil.getTime());
                pd.put("login_date", "");
                pd.put("identity", 1);
                pd.put("diedstatus", 0);
                sysAppUserService.save(pd);
                SmsBao smsBao=new SmsBao();
                smsBao.sendSMS(phone,"您的账号已创建，账号:"+username+" 初始密码:"+GetStrings.getRandomNickname(10));
                PageData pd_customer=new PageData();
                pd_customer.put("name","零散客户");
                pd_customer.put("phone","");
                pd_customer.put("address","");
                pd_customer.put("crop","");
                pd_customer.put("area","");
                pd_customer.put("input_date", DateUtil.getTime());
                pd_customer.put("billing_date","");
                pd_customer.put("owe","0");
                pd_customer.put("status","3");
                pd_customer.put("store_id",pd_s.get("store_id"));
                pd_customer.put("uid",pd.get("uid"));
                pd_customer.put("province","");
                pd_customer.put("city","");
                pd_customer.put("county","");
                pd_customer.put("street","");
                pd_customer.put("img","");
                pd_customer.put("identity","");
                pd_customer.put("remarks","");
                customerService.save(pd_customer);
                PageData pageData = new PageData();
                pageData.put("user_id",pd.get("uid"));
                pageData.put("store_id",pd_s.get("store_id"));
                pageData.put("status",0);
                pageData.put("ismr",1);
                storeService.saveStoreUser(pageData);
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
            }
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
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

    @RequestMapping(value = "findPersonList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findPersonList(String province){
        logBefore(logger,"查询负责人列表");
        PageData pd=this.getPageData();
        try{
            //List<PageData> list=personService.findList(pd);
            List<PageData> lists=userService.findList(pd);
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("data",lists);
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
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
     * 查询店铺列表
     * @param userId 用户id
     * @return
     */
    @RequestMapping(value = "findstorList",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findstorList(String userId){
        logBefore(logger,"查询店铺列表");
        PageData pd=this.getPageData();
        try{
            //查询关联表
            List<PageData> list=storeService.findstorListById(pd);
            for (int i = 0; i < list.size(); i++) {
                StoreLose storeLose = new StoreLose();
                storeLose.setStoreId(Long.valueOf(list.get(i).get("store_id").toString()));
                StoreLose selectLose = storeService.selectLose(storeLose);
                if(selectLose!=null){
                    list.get(i).put("lose",selectLose.getStatus());
                    list.get(i).put("automatic",selectLose.getAutomatic());
                }else {
                    list.get(i).put("lose",1);
                    list.get(i).put("automatic",1);
                }
            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
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
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 添加店铺
     * @param storename  店铺名称
     * @param province  省
     * @param city  市
     * @param county 区
     * @param address  详细地址
     * @param phone  电话
     * @param qr_code  二维码地址
     * @param user_id  用户id
     * @return
     */
    @RequestMapping(value = "saveOtherStore",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveOtherStore(String storename,String province,String city,String county,String address,
                                 String phone,String qr_code,String user_id){
        logBefore(logger,"查询店铺列表");
        PageData pd=this.getPageData();
        try{
            PageData pd_s=new PageData();
            pd_s.put("name",storename);
            pd_s.put("number","0");
            pd_s.put("address",address);
            pd_s.put("qr_code",qr_code);
            pd_s.put("phone",phone);
            pd_s.put("province",province);
            pd_s.put("city",city);
            pd_s.put("county",county);
            pd_s.put("street","");
            pd_s.put("business_img","");
            pd_s.put("phoneTow",phone);
            storeService.save(pd_s);    //先添加店

            PageData pageData = new PageData();
            pageData.put("user_id",user_id);
            pageData.put("store_id",pd_s.get("store_id"));
            pageData.put("status",0);
            pageData.put("ismr",0);
            storeService.saveStoreUser(pageData);

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
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 修改店铺信息
     * @param storename  店铺名称
     * @param province 省
     * @param city 市
     * @param county 区
     * @param address 详细地址
     * @param phone 电话
     * @param qr_code 二维码
     * @param store_id  店铺id
     * @return
     */
    @RequestMapping(value = "editStoreDetail",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editStoreDetail(String storename,String province,String city,String county,String address,
                                 String phone,String qr_code,Long store_id){
        logBefore(logger,"修改店铺信息");
        PageData pd=this.getPageData();
        try{
            PageData pd_s=new PageData();
            pd_s.put("name",storename);
            pd_s.put("address",address);
            pd_s.put("qr_code",qr_code);
            pd_s.put("province",province);
            pd_s.put("city",city);
            pd_s.put("county",county);
            pd_s.put("phoneTow",phone);
            pd_s.put("store_id",store_id);

            storeService.editStoreDetail(pd_s);
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
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 切换店铺
     * @param user_id 用户id
     * @param store_id 店铺id
     * @return
     */
    @RequestMapping(value = "editcheckStore",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editcheckStore(Long user_id,Long store_id,Integer type){
        logBefore(logger,"查询店铺列表");
        PageData pd=this.getPageData();
        try{
            System.out.println(">>>>>>"+pd.toString());
            pd.put("ismr",0);
            //全部修改未0
            storeService.editCheckStore(pd);

            //修改默认的店铺
            pd.put("ismr",1);
            storeService.editCheckStoreById(pd);

            sysAppUserService.editStoreId(pd);

            //给前端返回token
            /*Long tokenId = Long.valueOf(store_id);
            loginSaltService.delete(tokenId);
            String salt = GetStrings.getRandomNickname(4);
            loginSaltService.insert(tokenId,salt);

            String token = Md5Util.md5(tokenId.toString(),salt);
            System.out.println(">>>>>>返回的token："+token);*/
            String salt = "Nsakj";
            String token = Md5Util.md5(store_id.toString(),salt);
            pd.put("token",token);
            System.out.println(">>>>>>返回的token："+token);
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("token",token);
        }catch (Exception e){
            pd.clear();
            pd.put("code","2");
            pd.put("message","程序出错,请联系管理员!");
            e.printStackTrace();
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
     * 停用  启用店铺
     * @param status 0启用  1通用
     * @param store_id  店铺id
     * @return
     */
    @RequestMapping(value = "editStatus",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editStatus(Integer status,Long store_id){
        logBefore(logger,"查询店铺列表");
        PageData pd=this.getPageData();
        try{

            storeService.editStatus(pd);
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
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 刷表程序
     * @return
     */
    @RequestMapping(value = "shuabiao",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findstorList(){
        logBefore(logger,"刷表程序");
        PageData pd=this.getPageData();
        try{
            //查询关联表
            List<PageData> list=sysAppUserService.findall();
            for (int i = 0; i < list.size(); i++) {
                PageData pageData = new PageData();
                pageData.put("user_id",list.get(i).get("uid"));
                pageData.put("store_id",list.get(i).get("store_id"));
                pageData.put("status",0);
                pageData.put("ismr",1);
                storeService.saveStoreUser(pageData);
                StoreLose storeLose = new StoreLose();
                storeLose.setStoreId(Long.valueOf(list.get(i).get("store_id").toString()));
                StoreLose selectLose = storeService.selectLose(storeLose);
                if(selectLose!=null){
                    list.get(i).put("lose",selectLose.getStatus());
                }else {
                    list.get(i).put("lose",1);
                }
            }
            pd.clear();
            pd.put("code","1");
            pd.put("message","正确返回数据!");
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
            str=mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }



   /* @RequestMapping(value = "/wxPay_snatch", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String wxPay_snatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logBefore(logger, "微信支付");
        Map<String,Object> result=new HashMap<String,Object>();
        String partner = "1482116722";
        String appid = "wx3e8ef0db80a0580f";
        String appsecret = "278fc286264c9d8a96571752f88c3eb7";
        String partnerkey = "5BCB0B1B9455219FF9628FA9DED938A2";
        Date now = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String s = String.valueOf((int) ((Math.random() * 9 + 1) * 100));
        String no = s + sdf1.format(now).substring(2); // 订单号
        String ORDER_NUMBER = no;
        String MONEY = request.getParameter("money");
        String code = "1";
        String flag = "count,"+request.getParameter("uid")+","+request.getParameter("store_id");

        // 获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
        String currTime = TenpayUtil.getCurrTime();
        // 8位日期
        String strTime = currTime.substring(8, currTime.length());
        // 四位随机数
        String strRandom = TenpayUtil.buildRandom(4) + "";
        // 10位序列号,可以自行调整。
        String strReq = strTime + strRandom;

        // 商户号
        String mch_id = partner;
        // 随机数
        String nonce_str = strReq;
        // 商品描述根据情况修改
        String body = "喜开单";
        // 商户订单号
        String out_trade_no = ORDER_NUMBER;
        // 总金额以分为单位，不带小数点
        // Integer total_fee1=Integer.valueOf(MONEY)*100;
        String total_fee = MONEY;
        // 订单生成的机器 IP
        String spbill_create_ip = request.getRemoteAddr();
        // 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
        String notify_url = "111";
        String trade_type = "NATIVE";

        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("attach", flag);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", total_fee);
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);

        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(appid, appsecret, partnerkey);

        String sign = reqHandler.createSign(packageParams);
        String xml = "<xml>" +
                "<appid>" + appid + "</appid>" +
                "<attach>" + flag + "</attach>" +
                "<mch_id>" + mch_id+ "</mch_id>" +
                "<nonce_str>" + nonce_str + "</nonce_str>" +
                "<sign>" + sign + "</sign>" +
                // "<body>"+body+"</body>"+
                "<body><![CDATA[" + body + "]]></body>" +
                "<out_trade_no>" + out_trade_no + "</out_trade_no>"
                + "<total_fee>" + total_fee + "</total_fee>"
                + "<spbill_create_ip>" + spbill_create_ip+ "</spbill_create_ip>" +
                "<notify_url>" + notify_url + "</notify_url>" +
                "<trade_type>" + trade_type+ "</trade_type>" + "</xml>";
        System.out.println(xml);
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(createOrderURL);
            post.addHeader("Content-Type", "text/xml; charset=UTF-8");
            StringEntity xmlEntity = new StringEntity(xml, ContentType.TEXT_XML);
            post.setEntity(xmlEntity);
            HttpResponse httpResponse = httpClient.execute(post);
            String responseXML = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            logger.info("response xml content : " + responseXML);
            // parse CODE_URL CONTENT
            Map<String, String> resultMap = (Map<String, String>) XMLUtil.xmlToMap(responseXML);
            logger.info("response code_url : " + resultMap.get("code_url"));
            String codeurl = resultMap.get("code_url");
            if(codeurl != null && !"".equals(codeurl)) {
                result.put("codeurl", codeurl);
            }
            post.releaseConnection();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return result.toString();
    }*/

    @RequestMapping(value = "/wxPay_snatch", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String wxPay_snatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logBefore(logger, "微信支付");
        String partner = "1482116722";
        String appid = "wx3e8ef0db80a0580f";
        String appsecret = "278fc286264c9d8a96571752f88c3eb7";
        String partnerkey = "7de65fda4cca605c55c260d937a18121";
        Date now = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String s = String.valueOf((int) ((Math.random() * 9 + 1) * 100));
        String no = s + sdf1.format(now).substring(2); // 订单号
        String ORDER_NUMBER = no;
        String money = request.getParameter("money");
        String code = "1";
        String flag = request.getParameter("count")+","+request.getParameter("uid")+","+request.getParameter("store_id");
        logBefore(logger,"================微信扫码支付数值"+flag);

        // 获取openId后调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder
        String currTime = TenpayUtil.getCurrTime();
        // 8位日期
        String strTime = currTime.substring(8, currTime.length());
        // 四位随机数
        String strRandom = TenpayUtil.buildRandom(4) + "";
        // 10位序列号,可以自行调整。
        String strReq = strTime + strRandom;

        // 商户号
        String mch_id = partner;
        // 随机数
        String nonce_str = strReq;
        // 商品描述根据情况修改
        String body = "喜开单";
        // 商户订单号
        String out_trade_no = ORDER_NUMBER;
        // 总金额以分为单位，不带小数点
        Double moneys=Double.parseDouble(money)*100;

        String total_fee = DateUtil.doubleTrans(moneys);
        // 订单生成的机器 IP
        String spbill_create_ip = request.getRemoteAddr();
        // 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
        String notify_url = "http://115.28.210.33:9090/manage/app/store/wxPayNotify";
        String trade_type = "NATIVE";

        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", appid);
        packageParams.put("attach", flag);
        packageParams.put("mch_id", mch_id);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", total_fee);
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", trade_type);

        RequestHandler reqHandler = new RequestHandler(request, response);
        reqHandler.init(appid, appsecret, partnerkey);

        String sign = reqHandler.createSign(packageParams);
        String xml = "<xml>" +
                "<appid>" + appid + "</appid>" +
                "<attach>" + flag + "</attach>" +
                "<mch_id>" + mch_id+ "</mch_id>" +
                "<nonce_str>" + nonce_str + "</nonce_str>" +
                "<sign>" + sign + "</sign>" +
                // "<body>"+body+"</body>"+
                "<body><![CDATA[" + body + "]]></body>" +
                "<out_trade_no>" + out_trade_no + "</out_trade_no>"
                + "<total_fee>" + total_fee + "</total_fee>"
                + "<spbill_create_ip>" + spbill_create_ip+ "</spbill_create_ip>" +
                "<notify_url>" + notify_url + "</notify_url>" +
                "<trade_type>" + trade_type+ "</trade_type>" + "</xml>";
        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String responseXML = "";
        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
        try {

            responseXML = new GetWxOrderno().getcodeUrl(createOrderURL, xml);
            System.out.println("XML================="+responseXML);
            if (responseXML.equals("")) {
                //resultMap= (Map<String, String>) XMLUtil.xmlToMap(responseXML);
                // request.setAttribute("ErrorMsg", "统一支付接口获取预支付订单出错");
                System.out.println("ErrorMsg  统一支付接口获取预支付订单出错");
                // response.sendRedirect("error.jsp");
                code = "2";
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //ObjectMapper mapper = new ObjectMapper();
        finalpackage.put("code_url",responseXML);
        finalpackage.put("code",code);
        ObjectMapper mapper=new ObjectMapper();
        String str = null;
        try {
            str = mapper.writeValueAsString(finalpackage);
            System.out.println("str================="+responseXML);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        logger.error("返回参数："+str);
        return str;
    }


    /*private String generateQrcode(String codeurl) {
        String  encoderImgId = this.get32UUID() + ".png"; // encoderImgId此处二维码的图片名
        String filePath = PathUtil.getClasspath() + Const.FILEPATHTWODIMENSIONCODE + encoderImgId; // 存放路径
				System.out.println("二维码的路径"+filePath);
				TwoDimensionCode.encoderQRCode(codeurl, filePath, "png"); // 执行生成二维码
        return filePath;
    }*/

    /**
     *
     * @param money 钱数
     * @param customer_id 客户ID
     * @param store_id 店ID
     * @return
     */
    @RequestMapping(value = "saveAlready",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveAlready(String money,String customer_id,String store_id){
        logBefore(logger,"还款");
        PageData pd=this.getPageData();
        if(money==null||money.length()==0||customer_id==null||customer_id.length()==0||
                store_id==null||store_id.length()==0){
            pd.clear();
            pd.put("code","2");
            pd.put("message","正确返回数据!");
        }else{
            try{
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String s = String.valueOf((int) ((Math.random() * 9 + 1) * 100));
                Date now = new Date();
                String no = s + sdf1.format(now).substring(2); // 订单号
                String date=DateUtil.getTime();
                pd.put("date", date);
                pd.put("out_trade_no",no);
                pd.put("status","0");
                alreadyService.save(pd);
                pd.clear();
                pd.put("code","1");
                pd.put("message","正确返回数据!");
                pd.put("date",date);
                pd.put("already_id",pd.get("already_id").toString());
                pd.put("out_trade_no",no);
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

    @RequestMapping(value = "findAlreadyId",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findAlreadyId(String already_id){
        logBefore(logger,"查询充值记录");
        PageData pd=this.getPageData();
        try{
            PageData pd_a=alreadyService.findById(pd);
            pd.put("code","1");
            pd.put("message","正确返回数据!");
            pd.put("status",pd_a.getString("status"));
         }catch (Exception e){
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


    @RequestMapping(value = "/wxPayNotify", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String wxPayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logBefore(logger, "微信回调");
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        System.out.println(sb);
        Map<String, String> map = GetWxOrderno.doXMLParse(sb.toString());
        String attach = map.get("attach");
        String result_code = map.get("result_code");
        String orderNo = map.get("out_trade_no");
        String total_fee = map.get("total_fee");
        //String total_amount = request.getParameter("total_amount");
        String xml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                + "<return_msg><![CDATA[签名失败]]></return_msg>" + "</xml>";
        logBefore(logger,"================微信扫码支付回调数值"+attach);
        if ("SUCCESS".equals(result_code)) {
            String str[]=attach.split(",");
            PageData od=new PageData();
            od.put("store_id",str[2]);
            od.put("count",str[0]);
            od.put("uid",str[2]);
            od.put("money",Double.parseDouble(total_fee)/100);
            od.put("date", DateUtil.getTime());
            od.put("out_trade_no",orderNo);
            od.put("status","1");
            rechargeService.save(od);
            storeService.updateJiaNumber(od);
            System.out.println("~~~~~~~~~~~~~~~~付款成功~~~~~~~~~");
            xml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>"
                    + "</xml>";
        }
        System.out.println(xml);
        return xml;
    }

}
