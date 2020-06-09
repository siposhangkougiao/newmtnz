package com.mtnz.service.system.permissions;

import java.util.List;
import java.util.Map;

public interface StorePermissionsService {

    public List<Map<String,Object>> findPermission(Long userId, Long StoreId);

    public boolean updatePermission(Long userId,Long storeId,String permissionIds);

    public boolean updatePermissionAll(String userId,Long storeId,String permissionIds);


}
