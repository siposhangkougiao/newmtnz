package com.mtnz.controller.base;

import tk.mybatis.mapper.common.special.InsertListMapper;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.ExampleMapper;

public interface MyMapper<T> extends BaseMapper<T>, InsertListMapper<T>, ExampleMapper<T>, ConditionMapper<T> {

}
