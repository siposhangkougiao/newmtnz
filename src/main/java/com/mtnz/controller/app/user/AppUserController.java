package com.mtnz.controller.app.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtnz.controller.app.statistical.model.StLogin;
import com.mtnz.controller.app.store.model.StoreUser;
import com.mtnz.controller.app.user.model.LoginSalt;
import com.mtnz.controller.base.BaseController;
import com.mtnz.entity.Page;
import com.mtnz.service.system.adminrelation.AdminRelationService;
import com.mtnz.service.system.customer.CustomerService;
import com.mtnz.service.system.integral.IntegralService;
import com.mtnz.service.system.order_info.OrderInfoService;
import com.mtnz.service.system.statistical.StatisticalService;
import com.mtnz.service.system.store.MyStoreService;
import com.mtnz.service.system.store.StoreService;
import com.mtnz.service.system.sys_app_user.SysAppUserService;
import com.mtnz.service.system.user.LoginSaltService;
import com.mtnz.service.system.yzm.YzmService;
import com.mtnz.sql.system.store.StoreUserMapper;
import com.mtnz.util.*;
import com.sun.org.apache.bcel.internal.generic.IFNONNULL;
import com.sun.org.apache.bcel.internal.generic.IFNULL;
import org.apache.commons.collections.map.HashedMap;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


/*
    Created by xxj on 2018\3\21 0021.
 */
@Controller
@RequestMapping(value = "app/user", produces = "text/html;charset=UTF-8")
public class AppUserController extends BaseController {


    @Resource(name = "sysAppUserService")
    private SysAppUserService sysAppUserService;    //用户
    @Resource(name = "storeService")
    private StoreService storeService;
    @Resource(name = "yzmService")
    private YzmService yzmService;
    @Resource(name = "adminRelationService")
    private AdminRelationService adminRelationService;
    @Resource(name = "customerService")
    private CustomerService customerService;
    @Resource(name = "integralService")
    private IntegralService integralService;
    @Resource(name = "orderInfoService")
    private OrderInfoService orderInfoService;
    @Resource(name = "myStoreService")
    private MyStoreService myStoreService;
    @Resource
    private LoginSaltService loginSaltService;
    @Resource
    private StoreUserMapper storeUserMapper;

    /**
     * @param uid      用户ID
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "Judge", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String Judge(String uid, String password) {
        logBefore(logger, "判断是否自己登陆");
        PageData pd = this.getPageData();
        try {
            PageData pd_a = sysAppUserService.findById(pd);
            String pad = MD5.md5(MD5.md5(password) + pd_a.getString("salt"));
            if (pd_a.getString("password").equals(pad)) {
                pd.clear();
                pd.put("code", "1");
                pd.put("message", "正确返回数据!");
            } else {
                pd.clear();
                pd.put("code", "2");
                pd.put("message", "密码错误");
            }
        } catch (Exception e) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    @RequestMapping(value = "findMyStoreUser", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findMyStoreUser() {
        logBefore(logger, "查询本店2级员工");
        PageData pd = this.getPageData();
        try {
            List<PageData> pd_a = sysAppUserService.findUserByStore(pd);
            pd.clear();
            pd.put("code", "1");
            pd.put("message", "正确返回数据!");
            pd.put("data", pd_a);
        } catch (Exception e) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 停用启用员工
     * @return
     */
    @RequestMapping(value = "update", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String update() {
        logBefore(logger, "修改用户信息");
        PageData pd = this.getPageData();
        System.out.println(">>>>接收到的参数："+ com.alibaba.fastjson.JSONObject.toJSONString(pd));
        try {
            if(pd.get("code")!=null){
                PageData pd2 = new PageData();
                pd2.put("code", pd.get("code").toString());
                pd2.put("phone", pd.get("phone").toString());
                pd2.put("now_time", new Date());
                PageData codepage = myStoreService.findyzmBycode(pd2);
                if(codepage==null){
                    return getMessage("-101","验证码失效，请重新发送！");
                }else {
                    if(!codepage.get("code").toString().equals(pd.get("code").toString())){
                        return getMessage("-102","验证码错误！");
                    }
                    myStoreService.editCode(pd2);
                }
            }
            PageData user = sysAppUserService.findById(pd);
            if(pd.get("password")!=null){
                pd.put("password",MD5.md5(MD5.md5(pd.get("password").toString()) + user.getString("salt")));
            }
            sysAppUserService.update(pd);
            pd.clear();
            pd.put("code", "1");
            pd.put("message", "正确返回数据!");
        } catch (Exception e) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * @param store_id 店iD
     * @param name     名称
     * @param phone    电话
     * @param province 省
     * @param city     市
     * @param county   县
     * @param street   街道
     * @param address  详细地址
     * @return
     */
    @RequestMapping(value = "editStoreName", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editStoreName(String store_id, String name, String address, String phone, String province,
                                String city, String county, String street) {
        logBefore(logger, "修改店铺名称");
        PageData pd = this.getPageData();
        if (store_id == null || store_id.length() == 0 || name == null || name.length() == 0) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "缺少参数");
        } else {
            try {
                storeService.updateName(pd);
                pd.clear();
                pd.put("code", "1");
                pd.put("message", "正确返回数据!");
                pd.put("name", name);
                if (address != null) {
                    pd.put("address", address);
                } else {
                    pd.put("address", "");
                }
                if (address != null) {
                    pd.put("phone", phone);
                } else {
                    pd.put("phone", "");
                }
            } catch (Exception e) {
                pd.clear();
                pd.put("code", "2");
                pd.put("message", "程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @Resource
    private StatisticalService statisticalService;

    /**
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "login", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String login(String username, String password,Integer type) {

        logBefore(logger, "用户登录");
        PageData pd = this.getPageData();
        Map<String, Object> map = new HashMap();
        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "缺少参数");
        } else {
            try {
                if(type==null){
                    pd.put("type",0);// 0表示是pc登录的  1表示是app登录的
                }
                System.out.println("登录账号密码："+pd.toString());
                PageData pd_u = sysAppUserService.findUserName(pd);   //根据用户名查询用户是否存在
                if (pd_u != null) {
                    if ("1".equals(pd_u.get("diedstatus").toString())) {
                        pd.clear();
                        pd.put("code", "2");
                        pd.put("message", "此用已被弃用！！！");
                        ObjectMapper mapper = new ObjectMapper();
                        String str = "";
                        try {
                            str = mapper.writeValueAsString(pd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return str;
                    }
                    String pad = MD5.md5(MD5.md5(password) + pd_u.getString("salt"));
                    pd.put("password", pad);
                    pd_u = sysAppUserService.login(pd);
                    if (pd_u != null) {
                        map.put("username", pd_u.getString("username"));
                        map.put("sname", pd_u.getString("sname"));
                        map.put("address", pd_u.getString("address"));
                        map.put("saddress", pd_u.getString("saddress"));
                        map.put("uid", pd_u.get("uid"));
                        map.put("store_id", pd_u.get("store_id"));
                        map.put("status", pd_u.getString("status"));
                        map.put("province", pd_u.getString("province"));
                        map.put("city", pd_u.getString("city"));
                        map.put("district", pd_u.getString("district"));
                        map.put("phone", pd_u.getString("phone"));
                        map.put("name", pd_u.getString("name"));
                        map.put("identity", pd_u.get("identity").toString());
                        map.put("nickName", pd_u.get("nickName").toString());
                        map.put("header", pd_u.get("header").toString());
                        map.put("signature", pd_u.get("signature").toString());

                        pd_u.put("login_date", DateUtil.getTime());
                        PageData userpage = new PageData();
                        userpage.put("user_id",pd_u.get("uid"));
                        PageData user = integralService.findIntegralSetup(userpage);
                        if(user!=null){
                            map.put("integral", user.get("number"));
                        }else {
                            map.put("integral", new BigDecimal(0));
                        }
                        sysAppUserService.editLoginDate(pd_u);
                        //查询店铺是否审核通过
                        PageData pageData = myStoreService.selectResult(pd_u);
                        map.put("is_pass",pageData.get("is_pass"));
                        /*if(type ==null||type!=1){
                            PageData pdx = new PageData();
                            pdx.put("phoneTow",username);
                            PageData pageData = storeService.findStoreOneByUId(pdx);
                            map.put("store_id", pageData.get("store_id"));
                            map.put("saddress", pageData.getString("address"));
                            map.put("sname", pageData.getString("name"));
                            map.put("phone", pageData.getString("phone"));
                        }*/
                        pd.clear();
                        pd.put("code", "1");
                        pd.put("message", "正确返回数据!");
                        pd.put("data", map);
                        //给前端返回token
                        String salt = "Nsakj";
                        String token = Md5Util.md5(pd_u.get("store_id").toString(),salt);
                        pd.put("token",token);
                        System.out.println(">>>>>>返回的token："+token);
                        /*Long tokenId = Long.valueOf(pd_u.get("store_id").toString());
                        LoginSalt loginSalt = loginSaltService.select(pd_u.get("store_id").toString());
                        if(loginSalt==null){
                            String salt = GetStrings.getRandomNickname(4);
                            loginSaltService.insert(tokenId,salt);
                            String token = Md5Util.md5(tokenId.toString(),salt);
                            pd.put("token",token);
                            System.out.println(">>>>>>返回的token："+token);
                        }else {
                            String token = Md5Util.md5(tokenId.toString(),loginSalt.getSalt());
                            pd.put("token",token);
                            System.out.println(">>>>>>返回的token："+token);
                        }*/
                        //loginSaltService.delete(tokenId);

                        //记录登录次数
                        statisticalService.insertLogin(Long.valueOf(pd_u.get("uid").toString()));
                    } else {
                        pd.clear();
                        pd.put("code", "2");
                        pd.put("message", "密码不正确");
                    }
                } else {
                    pd.clear();
                    pd.put("code", "2");
                    pd.put("message", "用户不存在!");
                }
            } catch (Exception e) {
                pd.clear();
                pd.put("code", "2");
                pd.put("message", "程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "login/admin", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String loginadmin(String username, String password,Integer type) {

        logBefore(logger, "用户登录");
        PageData pd = this.getPageData();
        Map<String, Object> map = new HashMap();
        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "缺少参数");
        } else {
            try {
                if(type==null){
                    pd.put("type",0);// 0表示是pc登录的  1表示是app登录的
                }
                System.out.println("登录账号密码："+pd.toString());
                PageData pd_u = sysAppUserService.findUserName(pd);   //根据用户名查询用户是否存在
                if (pd_u != null) {
                    if ("1".equals(pd_u.get("diedstatus").toString())) {
                        pd.clear();
                        pd.put("code", "2");
                        pd.put("message", "此用已被弃用！！！");
                        ObjectMapper mapper = new ObjectMapper();
                        String str = "";
                        try {
                            str = mapper.writeValueAsString(pd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return str;
                    }
                    /*String pad = MD5.md5(MD5.md5(password) + pd_u.getString("salt"));
                    pd.put("password", pad);*/
                    pd_u = sysAppUserService.loginadmin(pd);
                    if (pd_u != null) {
                        map.put("username", pd_u.getString("username"));
                        map.put("sname", pd_u.getString("sname"));
                        map.put("address", pd_u.getString("address"));
                        map.put("saddress", pd_u.getString("saddress"));
                        map.put("uid", pd_u.get("uid"));
                        map.put("store_id", pd_u.get("store_id"));
                        map.put("status", pd_u.getString("status"));
                        map.put("province", pd_u.getString("province"));
                        map.put("city", pd_u.getString("city"));
                        map.put("district", pd_u.getString("district"));
                        map.put("phone", pd_u.getString("phone"));
                        map.put("name", pd_u.getString("name"));
                        map.put("identity", pd_u.get("identity").toString());
                        map.put("nickName", pd_u.get("nickName").toString());
                        map.put("header", pd_u.get("header").toString());
                        map.put("signature", pd_u.get("signature").toString());
                        pd_u.put("login_date", DateUtil.getTime());

                        PageData userpage = new PageData();
                        userpage.put("user_id",pd_u.get("uid"));
                        PageData user = integralService.findIntegralSetup(userpage);
                        if(user!=null){
                            map.put("integral", user.get("number"));
                        }else {
                            map.put("integral", new BigDecimal(0));
                        }
                        sysAppUserService.editLoginDate(pd_u);
                        //查询店铺是否审核通过
                        PageData pageData = myStoreService.selectResult(pd_u);
                        map.put("is_pass",pageData.get("is_pass"));
                        /*if(type ==null||type!=1){
                            PageData pdx = new PageData();
                            pdx.put("phoneTow",username);
                            PageData pageData = storeService.findStoreOneByUId(pdx);
                            map.put("store_id", pageData.get("store_id"));
                            map.put("saddress", pageData.getString("address"));
                            map.put("sname", pageData.getString("name"));
                            map.put("phone", pageData.getString("phone"));
                        }*/
                        pd.clear();
                        pd.put("code", "1");
                        pd.put("message", "正确返回数据!");
                        pd.put("data", map);
                        //给前端返回token
                        String salt = "Nsakj";
                        String token = Md5Util.md5(pd_u.get("store_id").toString(),salt);
                        pd.put("token",token);
                        System.out.println(">>>>>>返回的token："+token);
                        /*Long tokenId = Long.valueOf(pd_u.get("store_id").toString());
                        LoginSalt loginSalt = loginSaltService.select(pd_u.get("store_id").toString());
                        if(loginSalt==null){
                            String salt = GetStrings.getRandomNickname(4);
                            loginSaltService.insert(tokenId,salt);
                            String token = Md5Util.md5(tokenId.toString(),salt);
                            pd.put("token",token);
                            System.out.println(">>>>>>返回的token："+token);
                        }else {
                            String token = Md5Util.md5(tokenId.toString(),loginSalt.getSalt());
                            pd.put("token",token);
                            System.out.println(">>>>>>返回的token："+token);
                        }*/
                        //loginSaltService.delete(tokenId);

                        //记录登录次数
                        statisticalService.insertLogin(Long.valueOf(pd_u.get("uid").toString()));
                    } else {
                        pd.clear();
                        pd.put("code", "2");
                        pd.put("message", "密码不正确");
                    }
                } else {
                    pd.clear();
                    pd.put("code", "2");
                    pd.put("message", "用户不存在!");
                }
            } catch (Exception e) {
                pd.clear();
                pd.put("code", "2");
                pd.put("message", "程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 添加员工账号
     * @return
     */
    @RequestMapping(value = "newRegister", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String newRegister() {
        PageData pd = null;
        try {
            pd = this.getPageData();
            PageData pd2 = new PageData();
            pd2.put("code", pd.get("code").toString());
            pd2.put("phone", pd.get("phone").toString());
            pd2.put("now_time", new Date());
            PageData codepage = myStoreService.findyzmBycode(pd2);
            if(codepage==null){
                return getMessage("-101","验证码失效，请重新发送");
            }else {
                myStoreService.editCode(pd2);
            }
            PageData dtoUser = sysAppUserService.findUserName(pd);
            if (dtoUser != null && dtoUser.size() > 0) {
                pd.clear();
                pd.put("code", "2");
                pd.put("message", "用户账号已存在请选择其他账号！！！");
                ObjectMapper mapper = new ObjectMapper();
                String str = "";
                try {
                    str = mapper.writeValueAsString(pd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return str;
            }
            pd.put("salt", CharacterUtils.getRandomString(4));
            pd.put("password", MD5.md5(MD5.md5(pd.get("password").toString()) + pd.getString("salt")));
            pd.put("openid", "");
            pd.put("unionid", "");
            pd.put("person_id", "");
            pd.put("status", "0");
            pd.put("date", DateUtil.getTime());
            pd.put("login_date", DateUtil.getTime());
            sysAppUserService.save(pd);

            StoreUser storeUser = new StoreUser();
            storeUser.setUserId((Long) pd.get("uid"));
            storeUser.setStoreId((Long) pd.get("store_id"));
            storeUser.setStatus(0);
            storeUser.setIsmr(1);
            storeUserMapper.insert(storeUser);
            pd.clear();
            pd.put("code", "1");
            pd.put("msg", "操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            pd.clear();
            pd.put("code", "2");
            pd.put("msg", "程序出错！");
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    @RequestMapping(value = "register", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String register(String username, String password) {
        logBefore(logger, "注册");
        PageData pd = this.getPageData();
        if (username == null || username.length() == 0 || password == null || password.length() == 0) {
            pd.put("code", "2");
            pd.put("message", "缺少参数");
        } else {
            try {
                PageData pd_u = sysAppUserService.findUserName(pd);
                if (pd_u == null) {
                    PageData pd_s = new PageData();
                    pd_s.put("name", "");
                    pd_s.put("number", "200");
                    pd_s.put("address", "");
                    pd_s.put("qr_code", "");
                    pd_s.put("phone", "");
                    storeService.save(pd_s);    //先添加店
                    pd.put("username", username);
                    pd.put("salt", CharacterUtils.getRandomString(4));
                    pd.put("password", MD5.md5(MD5.md5(password) + pd.getString("salt")));
                    pd.put("name", "");
                    pd.put("openid", "");
                    pd.put("unionid", "");
                    pd.put("status", "0");
                    pd.put("store_id", pd_s.get("store_id"));
                    pd.put("date", DateUtil.getTime());
                    pd.put("login_date", "");
                    sysAppUserService.save(pd);
                    pd.clear();
                    pd.put("code", "1");
                    pd.put("message", "正确返回数据!");
                } else {
                    pd.clear();
                    pd.put("code", "1");
                    pd.put("message", "用户名已存在");
                }
            } catch (Exception e) {
                pd.clear();
                pd.put("code", "2");
                pd.put("message", "程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * @param password  旧密码
     * @param xpassword 新密码
     * @param uid       id
     * @return
     */
    @RequestMapping(value = "editPassowrd", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editPassowrd(String password, String xpassword, String uid) {
        logBefore(logger, "忘记密码");
        PageData pd = this.getPageData();
        if (password == null || password.length() == 0 || xpassword == null || xpassword.length() == 0 || uid == null || uid.length() == 0) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "正确返回数据!");
        } else {
            try {
                PageData pd_u = sysAppUserService.findById(pd);
                pd_u.put("password", MD5.md5(MD5.md5(password) + pd_u.getString("salt")));
                PageData pd_l = sysAppUserService.login(pd_u);
                if (pd_l != null) {
                    pd_l.put("password", MD5.md5(MD5.md5(xpassword) + pd_u.getString("salt")));
                    sysAppUserService.editPassword(pd_l);
                    pd.clear();
                    pd.put("code", "1");
                    pd.put("message", "正确返回数据!");
                } else {
                    pd.clear();
                    pd.put("code", "2");
                    pd.put("message", "原密码不正确");
                }
            } catch (Exception e) {
                pd.clear();
                pd.put("code", "2");
                pd.put("message", "程序出错,请联系管理员!");
                e.printStackTrace();
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * @param tel  错误的加密
     * @param tels 错误的加密
     * @param tls  正确的加密
     * @param lop  错误的加密
     * @param poi  手机号
     * @return
     */
    @RequestMapping(value = "findeSendS", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findeSendS(String tel, String tels, String tls, String lop, String poi) {
        logBefore(logger, "返回接口");
        PageData pd = new PageData();
        pd = this.getPageData();
        String message = "正确返回数据!";
        String verification = "verification";
        String verieication = "verieication";
        String vericication = "vericication";
        Map<String, Object> map = new HashedMap();
        String YH = "0";
        try {
            String md5s = MD5
                    .md5(MD5.md5("asdas-asdaskjslfklsasdf-dklfgkdjsgjdfljgldf-asdjaksldjaslkdas-dsfsfasd") + poi);
            if (tls.equals(md5s)) {
                map.put("verieication", verieication);
                map.put("verification", verification);
                map.put("vericication", vericication);
            } else {
                message = "验证不通过!";
            }
            pd.put("username", poi);
            PageData pd_u = sysAppUserService.findUserName(pd);
            if (pd_u != null) {
                YH = "1";
            }
            pd.clear();
            pd.put("code", "1");
            pd.put("message", message);
            pd.put("object", map);
            pd.put("YH", YH);
        } catch (Exception e) {
            e.printStackTrace();
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * @param username 用户名
     * @param YZM      验证码
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "ForgetPassowrd", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String ForgetPassowrd(String username, String YZM, String password) {
        logBefore(logger, "忘记密码密码");
        PageData pd = new PageData();
        String code = "1";
        String message = "";
        try {
            pd = this.getPageData();
            PageData pd1 = sysAppUserService.findUserName(pd);
            if (pd1 != null) {
                PageData pd_y = yzmService.findByPhone(pd1);
                if (pd_y != null) {
                    SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    long TIME = sdfTime.parse(DateUtil.getTime()).getTime();
                    long ENDTIME = sdfTime.parse(pd_y.getString("ENDTIME")).getTime();
                    if (TIME < ENDTIME) {
                        pd.put("uid", pd1.get("uid").toString());
                        pd.put("password", MD5.md5(MD5.md5(password) + pd1.getString("salt")));
                        sysAppUserService.editPassword(pd);
                        message = "成功";
                    } else {
                        code = "2";
                        message = "验证码过期";
                    }
                } else {
                    code = "2";
                    message = "没有验证码";
                }
            } else {
                code = "2";
                message = "没有该用户";
            }
            pd.clear();
            pd.put("code", code);
            pd.put("message", message);
        } catch (Exception e) {
            e.printStackTrace();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
        }
        org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
        String str = null;
        try {
            str = mapper.writeValueAsString(pd);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * @param USERNAME 手机号
     * @param tel      正确加密
     * @param tels     错误加密
     * @param tls      错误加密
     * @param lop      错误加密
     * @return
     */
    @RequestMapping(value = "/verification", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public synchronized String verification(String USERNAME, String tel, String tels, String tls, String lop) {
        logBefore(logger, "发送验证码");
        PageData pd = new PageData();
        pd = this.getPageData();
        String STATUS = "0";
        String MESSAGE = "正确返回数据!";
        Map<String, Object> map = new HashMap();
        String md5s = MD5
                .md5(MD5.md5("asdas-asdaskjslfklsasdf-sfsdfks=asdjaks=sadas-asdjaksldjaslkdas-dsfsfasd") + USERNAME);
        try {
            if (md5s.equals(tel)) {
                pd.put("YUE", DateUtil.getDay());
                HttpServletRequest request = this.getRequest();
                String ip = "";
                if (request.getHeader("x-forwarded-for") == null) {
                    ip = request.getRemoteAddr();
                } else {
                    ip = request.getHeader("x-forwarded-for");
                }
                pd.put("IP", ip);
                List<PageData> list_ip = yzmService.findByPhoneIp(pd);
                if (list_ip.size() < 5) {
                    List<PageData> list_yue = yzmService.findByPhoneYue(pd);
                    if (list_yue.size() < 5) {
                        String yzm = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
                        System.out.println("验证码为：" + yzm);

                        PageData pd2 = new PageData();
                        pd2.put("YZM", yzm);
                        pd2.put("PHONE", USERNAME);
                        pd2.put("TIME", DateUtil.getTime());
                        Calendar nowTime = Calendar.getInstance();
                        Date nowDate = (Date) nowTime.getTime(); // 得到当前时间
                        Calendar afterTime = Calendar.getInstance();
                        afterTime.add(Calendar.MINUTE, 5); // 当前分钟+5
                        Date afterDate = (Date) afterTime.getTime();
                        pd2.put("ENDTIME", afterDate);
                        pd2.put("YUE", DateUtil.getDay());
                        pd2.put("IP", ip);
                        yzmService.save(pd2);
                        SmsBao sms = new SmsBao();
                        String context = "验证码是【" + yzm + "】,有效时间:5分钟";
                        String result = sms.sendSMS(USERNAME, context);
                        if ("0".equals(result.split(",")[0])) {
                            map.put("YZM", yzm);
                        } else {
                            map.put("YZM", "-1");
                        }
                    } else {
                        STATUS = "2"; // 手机号今天上限
                        MESSAGE = "手机号今天上限!";
                    }
                } else {
                    STATUS = "1"; // ip地址今天上限
                    MESSAGE = "ip地址今天上限!";
                }
            } else {
                STATUS = "5";
                MESSAGE = "验证不通过!";
            }
            pd.clear();
            pd.put("object", map);
            pd.put("status", STATUS);
            pd.put("code", "1");
            pd.put("message", MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * @param uid 店铺ID
     * @return
     */
    @RequestMapping(value = "findShort", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findShort(Long uid) {
        logBefore(logger, "查询短信数");
        PageData pd = this.getPageData();
        try {
            //PageData pd_s = storeService.findById(pd);
            StoreUser storeUser = new StoreUser();
            storeUser.setUserId(uid);
            List<StoreUser> storeUsers =storeService.select(storeUser);
            List<Long> idlist = new ArrayList<>();
            for (int i = 0; i < storeUsers.size(); i++) {
                idlist.add(storeUsers.get(i).getStoreId());
            }
            Integer count =0;
            if (idlist !=null && idlist.size()!=0){
                count = storeService.selectSumNumber(idlist);
            }

            pd.clear();
            pd.put("code", "1");
            pd.put("message", "正确返回数据!");
            pd.put("number", count);
        } catch (Exception e) {
            e.printStackTrace();
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * @param uid     用户ID
     * @param openid
     * @param unionid
     * @return
     */
    @RequestMapping(value = "editOpenId", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String editOpenId(String uid, String openid, String unionid) {
        logBefore(logger, "添加openid");
        PageData pd = this.getPageData();
        String code = "1";
        String message = "正确返回数据!";
        String openids = "";
        try {
            PageData pd_a = sysAppUserService.findById(pd);
            if (pd != null) {
                if (pd_a.getString("openid").equals("")) {
                    String str = openid.substring(0, 3);
                    if (str.equals("o6_") && openid.length() == 28) {
                        pd_a.put("openid", openid);
                    } else {
                        URL url = null;
                        url = new URL("https://www.meitiannongzi.com/under/app/findXOpenId?unionid=" + unionid);
                        BufferedReader in = null;
                        String inputLine = "";
                        in = new BufferedReader(new InputStreamReader(url.openStream()));
                        inputLine = new String(in.readLine());
                        System.out.println("返回值====================>" + inputLine);
                        JSONObject json = new JSONObject(inputLine);
                        String codes = json.getString("code");
                        if (codes.equals("1")) {
                            String openidss = json.getString("openid");
                            pd_a.put("openid", openidss);
                        } else {
                            pd_a.put("openid", "");
                        }
                    }
                    pd_a.put("unionid", unionid);
                    sysAppUserService.editOpenId(pd_a);
                } else {
                    code = "2";
                    message = "用户已绑定";
                }
            } else {
                code = "2";
                message = "用户不存在!";
            }
            pd.clear();
            pd.put("code", code);
            pd.put("message", message);
            pd.put("openid", openids);
        } catch (Exception e) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @RequestMapping(value = "Edition")
    @ResponseBody
    public String Edition(String version) {
        logBefore(logger, "更新版本");
        PageData pd = new PageData();
        Map<String, Object> map = new HashedMap();
        String code = "200";
        try {
            if (!version.equals("2")) {
                String url = "http://m.nongjike.cn/NJK/static/jsp/app-release.apk";
                String msg = "增加商品分析.";
                map.put("url", url);
                map.put("message", msg);
                code = "400";
            } else {
                code = "200";
            }
            pd.clear();
            pd.put("code", code);
            pd.put("STATUS", "1");
            if (!code.equals("200")) {
                pd.put("data", map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            pd.clear();
            pd.put("code", "200");
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @RequestMapping(value = "saveBusiness", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveBusiness(String store_id, String business_img) {
        logBefore(logger, "添加二维码");
        PageData pd = this.getPageData();
        try {
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
            boolean flag = ImageAnd64Binary.generateImage(business_img, filePath);
            business_img = Const.SERVERPATH + Const.FILEPATHIMG + DateUtil.getDays() + "/" + ffile;
            pd.put("business_img", business_img);
            storeService.editBusiness(pd);
            pd.clear();
            pd.put("code", "1");
            pd.put("message", "正确返回数据!");
        } catch (Exception e) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @RequestMapping(value = "saveQrCode", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String saveQrCode(String store_id, String qr_code) {
        logBefore(logger, "添加二维码");
        PageData pd = this.getPageData();
        try {
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
            boolean flag = ImageAnd64Binary.generateImage(qr_code, filePath);
            qr_code = Const.SERVERPATH + Const.FILEPATHIMG + DateUtil.getDays() + "/" + ffile;
            System.out.println(">>>>>>>"+qr_code);
            pd.put("qr_code", qr_code);
            storeService.editQrCode(pd);
            pd.clear();
            pd.put("code", "1");
            pd.put("message", "正确返回数据!");
            pd.put("qr_code",qr_code);
        } catch (Exception e) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    @RequestMapping(value = "findAdminRelation", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String findAdminRelation(String uid, String name) {
        logBefore(logger, "查询关联");
        PageData pd = this.getPageData();
        try {
            List<PageData> list = adminRelationService.findList(pd);
            pd.clear();
            pd.put("code", "1");
            pd.put("message", "正确返回数据!");
            pd.put("data", list);
        } catch (Exception e) {
            pd.clear();
            pd.put("code", "2");
            pd.put("message", "程序出错,请联系管理员!");
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    @RequestMapping(value = "findAaaBbbb", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void findAaaBbbb() {
        try {
            PageData pd = new PageData();
            List<PageData> list = sysAppUserService.findQuanBu(pd);
            for (int i = 0; i < list.size(); i++) {
                storeService.editPhoneTow(list.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
