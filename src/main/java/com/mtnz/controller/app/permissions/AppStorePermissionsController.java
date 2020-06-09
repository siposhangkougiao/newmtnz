package com.mtnz.controller.app.permissions;

import com.mtnz.controller.app.permissions.model.Permissions;
import com.mtnz.controller.base.BaseController;
import com.mtnz.controller.base.Result;
import com.mtnz.controller.base.ServiceException;
import com.mtnz.service.system.permissions.StorePermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/storePermission")
public class AppStorePermissionsController extends BaseController {

    @Autowired
    private StorePermissionsService storePermissionsService;

    /**
     * 获取权限列表
     * @param userId
     * @param storeId
     * @return
     */
    @RequestMapping(value = "/getUserPermission",method = RequestMethod.POST)
    public Result getUserPermission(Long userId, Long storeId){
        Result result = new Result(0,"成功");

        try {
            List<Map<String,Object>> map = storePermissionsService.findPermission(userId,storeId);
            if (map!= null && map.size()!=0) {
                result.setData(map);
            }
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
     * 设置店员权限
     * @param userId
     * @param storeId
     * @param permissionIds
     * @return
     */
    @RequestMapping(value = "/updatePermission",method = RequestMethod.POST)
    public Result updatePermission(Long userId, Long storeId, String permissionIds){
        Result result = new Result(0,"成功");

        try {
           storePermissionsService.updatePermission(userId,storeId,permissionIds);
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
     * 设置全部店员权限
     * @param userId
     * @param storeId
     * @param permissionIds
     * @return
     */
    @RequestMapping(value = "/updatePermissionAll",method = RequestMethod.POST)
    public Result updatePermissionAll(String userId,Long storeId,String permissionIds){
        Result result = new Result(0,"成功");

        try {
            storePermissionsService.updatePermissionAll(userId,storeId,permissionIds);
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
