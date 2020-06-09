package com.mtnz.sql.system.product;


import com.mtnz.controller.app.product.model.Product;
import com.mtnz.controller.base.MyMapper;

import java.util.Map;


public interface ProductNewMapper extends MyMapper<Product> {

    Integer selectOrderCount(Long productId);

    Map<String ,Object> stockTotal(Long storeId);

}