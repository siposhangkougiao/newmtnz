package com.mtnz.service.system.mysql.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.mtnz.controller.app.mysql.model.GetBean;
import com.mtnz.controller.app.mysql.model.KuCun;
import com.mtnz.controller.app.mysql.model.Product;
import com.mtnz.service.system.mysql.KuCunService;
import com.mtnz.sql.system.mysql.KuCunMapper;
import com.mtnz.sql.system.mysql.ProductMapper;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class KuCunServiceImpl implements KuCunService {

    @Resource
    ProductMapper productMapper;

    @Resource
    KuCunMapper kuCunMapper;
    @Override
    public void test() {
        PageHelper.startPage(1,10000);
        List<Product> list = productMapper.selectAll();
        List<Long> relist = new ArrayList<>();
        List<KuCun> updatelist = new ArrayList<>();
        List<KuCun> updateling = new ArrayList<>();
        List<GetBean> getBeans = kuCunMapper.selectgetbean(list);
        Map<Long,Integer> map = new HashedMap();
        for (int i = 0; i < getBeans.size(); i++) {
            map.put(getBeans.get(i).getProductId(),getBeans.get(i).getTotal());
        }
        for (int i = 0; i < list.size(); i++) {
            /*Integer allnumber = kuCunMapper.selectSum(list.get(i).getProductId());*/
            if(map.containsKey(list.get(i).getProductId())&&list.get(i).getKucun()!=map.get(list.get(i).getProductId())){
                KuCun kuCun = new KuCun();
                kuCun.setProductId(list.get(i).getProductId());
                kuCun.setNums(0);
                updateling.add(kuCun);
                /*kuCunMapper.updatekucun(kuCun);*/
                KuCun kubean = kuCunMapper.selectdesc(list.get(i).getProductId());

                if(kubean!=null){
                    System.out.println("查询出来的kucun_id："+JSONObject.toJSONString(kubean));
                    KuCun kuCun1 = new KuCun();
                    kuCun1.setKuncunId(kubean.getKuncunId());
                    kuCun1.setNums(list.get(i).getKucun());
                    updatelist.add(kuCun1);
                    /*int count = kuCunMapper.updateByPrimaryKeySelective(kuCun1);
                    if(count>0){
                        System.out.println(">>>>>>>>>>>>"+"更新了一条数据");
                    }*/
                    relist.add(list.get(i).getProductId());
                }
            }
        }
        kuCunMapper.updateling(updateling);
        kuCunMapper.updatelist(updatelist);
        System.out.println(JSONObject.toJSONString(relist));
    }
}
