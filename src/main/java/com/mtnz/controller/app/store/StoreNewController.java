package com.mtnz.controller.app.store;

import com.alibaba.fastjson.JSONObject;
import com.mtnz.controller.app.preorder.model.PreOrder;
import com.mtnz.controller.app.preorder.model.SelectListBean;
import com.mtnz.controller.app.store.model.StoreLose;
import com.mtnz.controller.base.BaseController;
import com.mtnz.controller.base.Result;
import com.mtnz.controller.base.ServiceException;
import com.mtnz.service.system.preorder.PreOrderService;
import com.mtnz.service.system.store.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@RestController
@RequestMapping(value = "app/storenew")
public class StoreNewController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(StoreNewController.class);

    @Resource
    StoreService storeService;

    /**
     * 设置店铺负库存
     * @param storeLose
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    public Result save(@RequestBody StoreLose storeLose){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(storeLose));
        Result result = new Result(0,"成功");
        try {
            storeService.insert(storeLose);
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
     * 设置店铺自动出库
     * @param storeLose
     * @return
     */
    @RequestMapping(value = "/saveAuto",method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    public Result saveAuto(@RequestBody StoreLose storeLose){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(storeLose));
        Result result = new Result(0,"成功");
        try {
            storeService.insert(storeLose);
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
     * 查询店铺负库存设置
     * @param storeLose
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    public Result select(StoreLose storeLose){
        logger.error("接收到的参数：{}",JSONObject.toJSONString(storeLose));
        Result result = new Result(0,"成功");
        try {
            StoreLose storeLoses = storeService.selectLose(storeLose);
            result.setData(storeLoses);
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
