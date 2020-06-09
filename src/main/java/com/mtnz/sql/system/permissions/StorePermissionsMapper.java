package com.mtnz.sql.system.permissions;

import com.mtnz.controller.app.permissions.model.Permissions;
import com.mtnz.controller.app.permissions.model.StorePermissions;
import com.mtnz.controller.base.MyMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;
import java.util.Map;

public interface StorePermissionsMapper extends MyMapper<StorePermissions> {

    public List<Map<String,Object>> findPermission(@Param("userId") Long userId, @Param("storeId") Long storeId);

    @Delete("delete from store_permissions where userId=#{userId} and storeId=#{storeId}")
    public boolean deletePermission(@Param("userId") Long userId, @Param("storeId") Long storeId);

}
