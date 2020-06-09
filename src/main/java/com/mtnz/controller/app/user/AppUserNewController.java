package com.mtnz.controller.app.user;

import com.alibaba.fastjson.JSONObject;
import com.mtnz.controller.app.store.model.StoreLose;
import com.mtnz.controller.app.user.model.SysAppUser;
import com.mtnz.controller.app.user.model.ValidationYzm;
import com.mtnz.controller.base.BaseController;
import com.mtnz.controller.base.Result;
import com.mtnz.controller.base.ServiceException;
import com.mtnz.service.system.store.StoreService;
import com.mtnz.service.system.sys_app_user.SysAppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;


@RestController
@RequestMapping(value = "app/appusernew")
public class AppUserNewController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(AppUserNewController.class);

    @Resource
    private SysAppUserService sysAppUserService;

    /**
     * 获取登录验证码
     * @param validationYzm
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/code")
    @Produces(MediaType.APPLICATION_JSON)
    public Result getcode(@RequestBody ValidationYzm validationYzm){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(validationYzm));
        Result result = new Result(0,"成功");
        try {
            ValidationYzm validationYzm1 = sysAppUserService.getcode(validationYzm);
            result.setData(validationYzm1);
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
     * 短信验证码登录
     * @param validationYzm
     * @return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Result login(@RequestBody ValidationYzm validationYzm){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(validationYzm));
        Result result = new Result(0,"成功");
        try {
            Map map = sysAppUserService.newlogin(validationYzm);
            result.setData(map);
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

    @RequestMapping(method = RequestMethod.POST,value = "/updateUserInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Result updateUserInfo(@RequestBody SysAppUser sysAppUser){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(sysAppUser));
        Result result = new Result(0,"成功");
        try {
            sysAppUserService.updateUserInfo(sysAppUser);
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


}
