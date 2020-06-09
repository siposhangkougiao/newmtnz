package com.mtnz.sql.system.store;


import com.mtnz.controller.app.store.model.StoreUser;
import com.mtnz.controller.base.MyMapper;

import java.util.Map;

public interface StoreUserMapper extends MyMapper<StoreUser> {

    public Map<String,Object> findStoreIsPass(Long useId);

}