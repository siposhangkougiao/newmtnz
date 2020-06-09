package com.mtnz.service.system.permissions.impl;

import com.mtnz.controller.app.permissions.model.StorePermissions;
import com.mtnz.service.system.permissions.StorePermissionsService;
import com.mtnz.sql.system.permissions.StorePermissionsMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StorePermissionsServiceImpl implements StorePermissionsService {

    @Autowired
    private StorePermissionsMapper storePermissionsMapper;

    @Override
    public List<Map<String,Object>> findPermission(Long userId, Long StoreId) {
        return storePermissionsMapper.findPermission(userId,StoreId);
    }

    @Override
    public boolean updatePermission(Long userId,Long storeId,String permissionIds) {

        List<Map<String,Object>> list = storePermissionsMapper.findPermission(userId,storeId);
        if (list!=null && list.size() != 0){
            storePermissionsMapper.deletePermission(userId,storeId);
        }
        String[] permIds = permissionIds.split(",");
        for (String permId : permIds) {
            StorePermissions storePermissions = new StorePermissions();
            storePermissions.setUserId(userId);
            storePermissions.setStoreId(storeId);
            storePermissions.setPermissionId(Long.valueOf(permId));
            storePermissions.setPermType(1);
            storePermissionsMapper.insert(storePermissions);
        }
        return true;
    }

    @Override
    public boolean updatePermissionAll(String userId, Long storeId, String permissionIds) {
        if (StringUtils.isNotBlank(userId)){
            String[] userIds = userId.split(",");
            for (String id : userIds) {
                List<Map<String,Object>> list = storePermissionsMapper.findPermission(Long.valueOf(id),storeId);
                if (list!=null && list.size() != 0){
                    storePermissionsMapper.deletePermission(Long.valueOf(id),storeId);
                }
                String[] permIds = permissionIds.split(",");
                for (String permId : permIds) {
                    StorePermissions storePermissions = new StorePermissions();
                    storePermissions.setUserId(Long.valueOf(id));
                    storePermissions.setStoreId(storeId);
                    storePermissions.setPermissionId(Long.valueOf(permId));
                    storePermissions.setPermType(1);
                    storePermissionsMapper.insert(storePermissions);
                }
            }
            return true;
        }
        return false;
    }
}
