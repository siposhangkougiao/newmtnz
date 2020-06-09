package com.mtnz.service.system.sys_app_user;


import com.alibaba.fastjson.JSONObject;
import com.mtnz.controller.app.user.model.SysAppUser;
import com.mtnz.controller.app.user.model.ValidationYzm;
import com.mtnz.controller.base.ServiceException;
import com.mtnz.dao.DaoSupport;
import com.mtnz.entity.Page;
import com.mtnz.service.system.integral.IntegralService;
import com.mtnz.service.system.store.MyStoreService;
import com.mtnz.sql.system.user.SysAppUserNewMapper;
import com.mtnz.sql.system.user.ValidationYzmMapper;
import com.mtnz.util.*;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mtnz.controller.app.store.MyAppStoreController.isMobileNO;

/*
    Created by xxj on 2018\3\21 0021.
 */

@Service("sysAppUserService")
public class SysAppUserService {
    @Resource(name="daoSupport")
    private DaoSupport daoSupport;

    @Resource
    SysAppUserNewMapper sysAppUserNewMapper;

    @Resource
    ValidationYzmMapper validationYzmMapper;

    @Resource(name = "integralService")
    private IntegralService integralService;

    @Resource(name = "myStoreService")
    private MyStoreService myStoreService;

    public void save(PageData pd) throws Exception {
        daoSupport.save("SysAppUserMapper.save",pd);
    }

    public PageData findById(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("SysAppUserMapper.findById",pd);
    }

    public List<PageData> datalistPage(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("SysAppUserMapper.datalistPage",page);
    }

    public PageData login(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("SysAppUserMapper.login",pd);
    }

    public PageData loginadmin(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("SysAppUserMapper.loginadmin",pd);
    }

    public void editPassword(PageData pd) throws Exception {
        daoSupport.update("SysAppUserMapper.editPassword",pd);
    }
    public PageData findUserName(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("SysAppUserMapper.findUserName",pd);
    }

    public PageData findBySId(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("SysAppUserMapper.findBySId",pd);
    }

    public void update(PageData pd) throws Exception {
        daoSupport.update("SysAppUserMapper.update",pd);
    }

    public void delete(PageData pd) throws Exception {
        daoSupport.delete("SysAppUserMapper.delete",pd);
    }

    public List<String> findUserList(PageData pd) throws Exception {
        return (List<String>) daoSupport.findForList("SysAppUserMapper.findUserList",pd);
    }

    public void editOpenId(PageData pd) throws Exception {
        daoSupport.update("SysAppUserMapper.editOpenId",pd);
    }


    public List<PageData> findQuanBu(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("SysAppUserMapper.findQuanBu",pd);
    }

    public List<PageData> findUserByStore(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("SysAppUserMapper.findUserByStore",pd);
    }
    public void editLoginDate(PageData pd) throws Exception {
        daoSupport.update("SysAppUserMapper.editLoginDate",pd);
    }

    public List<PageData> findcostor(PageData pd) throws Exception {

        return (List<PageData>) daoSupport.findForList("SysAppUserMapper.findcostor",pd);
    }

    public List<PageData> findall() throws Exception {
        PageData pd = new PageData();
        return (List<PageData>) daoSupport.findForList("SysAppUserMapper.findall",pd);
    }

    public void editStoreId(PageData pd) throws Exception {
        daoSupport.update("SysAppUserMapper.editStoreId",pd);
    }

    public void saveLevel(PageData pd) throws Exception {
        daoSupport.save("ShopperSaleMapper.saveLevel",pd);
    }


    public List<PageData> findallUser() throws Exception {
        PageData pd = new PageData();
        return (List<PageData>) daoSupport.findForList("SysAppUserMapper.findallUser",pd);
    }

    /**
     * 获取登录验证码
     * @param validationYzm
     * @Param type 1 app登录  2PC登录
     * @return
     */
    public ValidationYzm getcode(ValidationYzm validationYzm) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String phone = validationYzm.getPhone();
        if(validationYzm.getType()!=null&&validationYzm.getType()==1){
            phone = RASTest.getKeyDES(phone.trim());
        }
        String[] str = phone.split(",");
        if(str.length !=2 ||str[0]==null||str[1]==null){
            throw new ServiceException(-105,"非法请求",null);
        }
        long aa = new Date().getTime();
        long bb = Long.valueOf(str[1]);
        if(aa-bb>10000){
            throw new ServiceException(-106,"非法请求",null);
        }
        System.out.println(">>>>>"+ JSONObject.toJSONString(str));
        if(!isMobileNO(str[0])){
            throw new ServiceException(-101,"手机号码格式不正确",null);
        }
        /*pd.put("username",str[0]);
        PageData pd_u=sysAppUserService.findUserName(pd);
        if(pd_u!=null){
            return getMessage("-101","用户已存在，不能重复注册！");
        }*/
        SysAppUser sysbean = new SysAppUser();
        sysbean.setUsername(str[0]);
        sysbean.setIsDelete(0);
        SysAppUser sysAppUser = sysAppUserNewMapper.selectOne(sysbean);
        if(sysAppUser==null){
            throw new ServiceException(-103,"用户不存在",null);
        }
        String yzm = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        System.out.println("验证码为：" + yzm);
        /*PageData pd2 = new PageData();
        pd2.put("code", yzm);
        pd2.put("phone", str[0]);
        pd2.put("start_time", DateUtil.getTime());*/
        Example example = new Example(ValidationYzm.class);
        example.and().andEqualTo("phone",str[0]);
        example.and().andEqualTo("isUse",0);
        example.and().andEqualTo("useType",1);
        example.and().andLessThanOrEqualTo("endTime",new Date());
        example.and().andGreaterThanOrEqualTo("startTime",new Date());
        if(validationYzmMapper.selectCountByExample(example)>0){
            throw new ServiceException(-107,"验证码已发送，请稍后",null);
        }
        ValidationYzm yzmbean = new ValidationYzm();
        yzmbean.setCode(yzm);
        yzmbean.setPhone(str[0]);
        yzmbean.setStartTime(new Date());
        Calendar afterTime = Calendar.getInstance();
        afterTime.add(Calendar.MINUTE, 5); // 当前分钟+5
        Date afterDate =afterTime.getTime();
        yzmbean.setEndTime(afterDate);
        yzmbean.setUseType(1);
        System.out.println(">>>>>>>>>>>>>"+sdf.format(afterDate));
        validationYzmMapper.insertSelective(yzmbean);
        SmsBao sms = new SmsBao();
        String context = "验证码是【" + yzm + "】有效时间:5分钟";
        String result = sms.sendSMS(str[0], context);
        ValidationYzm validationYzm1 = new ValidationYzm();
        if ("0".equals(result.split(",")[0])) {
            validationYzm1.setCode(yzm);
        } else {
            validationYzm1.setCode("-1");
        }
        return validationYzm1;
    }

    /**
     * 短信验证码登录
     * @param validationYzm
     */
    public Map newlogin(ValidationYzm validationYzm) throws Exception {
        SysAppUser seBean = new SysAppUser();
        seBean.setUsername(validationYzm.getPhone());
        seBean.setIsDelete(0);
        SysAppUser sysBean = sysAppUserNewMapper.selectOne(seBean);
        if(sysBean==null){
            throw new ServiceException(-102,"用户不存在",null);
        }
        Example example = new Example(ValidationYzm.class);
        example.and().andEqualTo("phone",validationYzm.getPhone());
        example.and().andEqualTo("isUse",0);
        example.and().andEqualTo("useType",1);
        example.and().andLessThanOrEqualTo("startTime",new Date());
        example.and().andGreaterThanOrEqualTo("endTime",new Date());
        example.and().andEqualTo("code",validationYzm.getCode());
        List<ValidationYzm> list = validationYzmMapper.selectByExample(example);
        if(list.size()<1){
            throw new ServiceException(-103,"验证码已过期",null);
        }
        ValidationYzm Bean = list.get(list.size()-1);
        if(!Bean.getCode().equals(validationYzm.getCode())){
            throw new ServiceException(-103,"验证码不正确",null);
        }
        Map map = new HashMap();
        map.put("username", sysBean.getUsername());
        map.put("sname", sysBean.getName());
        map.put("address", sysBean.getAddress());
        map.put("saddress", sysBean.getAddress());
        map.put("uid",sysBean.getUid());
        map.put("store_id", sysBean.getStoreId());
        map.put("status", sysBean.getStatus());
        map.put("province", sysBean.getProvince());
        map.put("city", sysBean.getCity());
        map.put("district", sysBean.getDistrict());
        map.put("phone", sysBean.getPhone());
        map.put("name", sysBean.getName());
        map.put("identity", sysBean.getIdentity());
        //查询积分
        PageData userpage = new PageData();
        userpage.put("user_id",sysBean.getUid());
        PageData user = integralService.findIntegralSetup(userpage);
        if(user!=null){
            map.put("integral", user.get("number"));
        }else {
            map.put("integral", new BigDecimal(0));
        }
        //查询店铺是否审核通过
        PageData pd_u = new PageData();
        pd_u.put("store_id",sysBean.getStoreId());
        PageData pageData = myStoreService.selectResult(pd_u);
        map.put("is_pass",pageData.get("is_pass"));
        //给前端返回token
        String salt = "Nsakj";
        String token = Md5Util.md5(pd_u.get("store_id").toString(),salt);
        map.put("token",token);
        System.out.println(">>>>>>返回的token："+token);
        ValidationYzm upbean = new ValidationYzm();
        upbean.setId(Bean.getId());
        upbean.setIsUse(1);
        validationYzmMapper.updateByPrimaryKeySelective(upbean);
        return map;
    }

    public void updateUserInfo(SysAppUser sysAppUser){
        SysAppUser appUser = sysAppUserNewMapper.selectByPrimaryKey(sysAppUser);
        if(appUser==null){
            throw new ServiceException(-102,"用户不存在",null);
        }
        Example example = new Example(SysAppUser.class);
        example.and().andEqualTo("uid",sysAppUser.getUid());
        sysAppUserNewMapper.updateByExampleSelective(sysAppUser,example);
    }

}
