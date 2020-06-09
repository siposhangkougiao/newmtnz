package com.mtnz.controller.app.permissions;

import com.mtnz.controller.app.permissions.model.Permissions;
import com.mtnz.controller.base.BaseController;
import com.mtnz.controller.base.Result;
import com.mtnz.controller.base.ServiceException;
import com.mtnz.sql.system.permissions.PermissionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/permission")
public class AppPermissionsController extends BaseController {

    @Autowired
    private PermissionsMapper permissionsMapper;

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public Result findList(){
        Result result = new Result(0,"成功");

        try {
            List<Permissions> list = permissionsMapper.selectAll();
            if (list!= null && list.size()!=0) {
                result.setData(list);
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



}
