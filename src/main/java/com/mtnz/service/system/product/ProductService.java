package com.mtnz.service.system.product;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mtnz.controller.app.product.model.ProductImg;
import com.mtnz.controller.app.supplier.model.Supplier;
import com.mtnz.entity.Page;
import com.mtnz.controller.app.product.model.Product;
import com.mtnz.dao.DaoSupport;

import com.mtnz.service.system.supplier.SupplierService;
import com.mtnz.service.system.supplier.SupplieresService;
import com.mtnz.sql.system.product.ProductImgMapper;
import com.mtnz.sql.system.product.ProductNewMapper;
import com.mtnz.util.PageData;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/*
    Created by xxj on 2018\3\21 0021.
 */

@Service("productService")
public class ProductService {

    @Resource(name = "daoSupport")
    private DaoSupport daoSupport;




    public void save(PageData pd) throws Exception {
        daoSupport.save("ProductMapper.save",pd);
    }

    public PageData findById(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("ProductMapper.findById",pd);
    }

    public List<PageData> findProductxx(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findProduct",pd);
    }

    public PageData findProductCount(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("ProductMapper.findProductCount",pd);
    }

    public void updateProduct(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.updateProduct",pd);
    }

    public void delete(PageData pd) throws Exception {
        daoSupport.delete("ProductMapper.delete",pd);
    }

    public List<PageData> findlikeList(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findlikeList",pd);
    }

    public void editNum(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.editNum",pd);
    }

    public void editNumJia(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.editNumJia",pd);
    }

    public void editNums(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.editNums",pd);
    }

    public List<PageData> findProductProfit(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findProductProfit",pd);
    }

/*    public List<PageData> findProductProfitList(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findProductProfitList",pd);
    }*/

    public PageData findBarCodeProduct(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("ProductMapper.findBarCodeProduct",pd);
    }

    public List<PageData> findBarCodeProducts(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findBarCodeProducts",pd);
    }

    public PageData findKuCun(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("ProductMapper.findKuCun",pd);
    }

    public List<PageData> findListType(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findListType",pd);
    }

    public void editSupplierId(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.editSupplierId",pd);
    }

    public List<PageData> findSupplierList(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findSupplierList",pd);
    }

    public PageData findCount(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("ProductMapper.findCount", pd);
    }

    public List<PageData> findNotSupplierList(Page page) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.SupplierdatalistPage",page);
    }

    public List<PageData> findLikeSupplierProduct(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findLikeSupplierProduct",pd);
    }


    public void editStatus(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.editStatus",pd);
    }

    public PageData findByNumber(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("ProductMapper.findByNumber",pd);
    }

    public List<PageData> findByNumbers(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findByNumbers",pd);
    }


    public List<PageData> findProductFenXi(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findProductFenXi",pd);
    }

    public void editJianNums(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.editJianNums",pd);
    }

    public void editJiaNums(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.editJiaNums",pd);
    }


    public List<PageData> findSupplierFeiXi(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findSupplierFeiXi",pd);
    }

    public List<PageData> findReportAnalysis(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findReportAnalysis",pd);
    }

    public List<PageData> findReportAnalysisYue(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findReportAnalysisYue",pd);
    }

    public List<PageData> findReportAnalysisXiaoShi(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findReportAnalysisXiaoShi",pd);
    }

    public PageData findSumProductMoney(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("ProductMapper.findSumProductMoney",pd);
    }

    public List<PageData> findProductFeiXis(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.findProductFeiXis",pd);
    }


    public List<PageData> findProductPrice(PageData pc) throws Exception {

        return (List<PageData>) daoSupport.findForList("ProductMapper.findProductPrice",pc);
    }

    public void editNumli(PageData pd) throws Exception {

        daoSupport.update("ProductMapper.editNumli",pd);
    }

    public void editJiaNumsli(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.editJiaNumsli",pd);
    }

    public void editJianNumsli(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.editJianNumsli",pd);
    }

    public void editNumslikucun(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.editNumslikucun",pd);
    }

    public List<PageData> selectProductLevel(PageData pd) throws Exception {
        return (List<PageData>) daoSupport.findForList("ProductMapper.selectProductLevel",pd);
    }

    public PageData selectLevel(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("ProductMapper.selectLevel",pd);
    }

    public PageData selectSaleLevel(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("ProductMapper.selectSaleLevel",pd);
    }

    public PageData findSaleLevel(PageData pd) throws Exception {
        return (PageData) daoSupport.findForObject("ProductMapper.findSaleLevel",pd);
    }

    public List<PageData> findproductall() throws Exception {
        PageData pd =new PageData();
        return (List<PageData>) daoSupport.findForList("ProductMapper.findproductall",pd);
    }

    public void editProductall(PageData pd) throws Exception {
        daoSupport.update("ProductMapper.editProductall",pd);
    }

    public void editProductallxx(List<PageData> xx) throws Exception {
        Map map = new HashedMap();
        map.put("list",xx);
        daoSupport.update("ProductMapper.editProductallxx",map);
    }

    @Resource
    ProductNewMapper productNewMapper;

    @Resource
    ProductImgMapper productImgMapper;

    @Resource
    SupplieresService supplieresService;

    public PageInfo selectList(Product product) {
        product.setPageSize(5);
        Product bean = new Product();

        bean.setStoreId(product.getStoreId());
        Example example = new Example(Product.class);
        example.and().andEqualTo("storeId",product.getStoreId());
        example.and().andEqualTo("status",0);
        example.orderBy("productId").desc();
        PageHelper.startPage(product.getPageNumber(),product.getPageSize());
        //List<Product> list = productNewMapper.select(bean);
        System.out.println("===============>"+example);
        List<Product> list = productNewMapper.selectByExample(example);

        /*Integer total = list.size();
        for (int i = 0; i < list.size(); i++) {
            Integer number = productNewMapper.selectOrderCount(list.get(i).getProductId());
            if(number==null){
                number=0;
            }
            list.get(i).setCount(number);
        }*/
        /*list.sort(Comparator.comparing(Product::getCount).reversed());
        //计算当前需要显示的数据下标起始值
        int startIndex = (product.getPageNumber() - 1) * product.getPageSize();
        int endIndex = Math.min(startIndex + product.getPageSize(),list.size());
        list = list.subList(startIndex,endIndex);*/

        for (int i = 0; i < list.size(); i++) {
            ProductImg productImg = new ProductImg();
            productImg.setProductId(list.get(i).getProductId());
            List<ProductImg> imgs = productImgMapper.select(productImg);
            list.get(i).setImgs(imgs);

            /*//计算库存
            BigDecimal a = list.get(i).getLikucun();
            BigDecimal b = new BigDecimal(1);
            try {
                b = new BigDecimal(list.get(i).getNorms1());
            }catch (Exception e){
            }
            BigDecimal c = null;
            try {
                c = a.divide(b,4);
            } catch (Exception e) {
                c= new BigDecimal(0);
            }
            BigDecimal kus = list.get(i).getKucun().add(c);
            list.get(i).setKucun(kus);*/

            //查询供货商
            Supplier supplierbean = new Supplier();
            supplierbean.setSupplierId(list.get(i).getSupplierId());
            Supplier supplier = supplieresService.selectSupplier(supplierbean);
            list.get(i).setSupplier(supplier);
        }
        //创建Page类
        /*com.github.pagehelper.Page page = new com.github.pagehelper.Page(product.getPageNumber(), product.getPageSize());
        page.setTotal(total);
        page.addAll(list);
        PageInfo pageInfo = new PageInfo<>(page);*/
        PageInfo pageInfo = new PageInfo(list);

        return pageInfo;
    }

    public List<PageData> selectproductList(PageData pd) throws Exception {

        return (List<PageData>) daoSupport.findForList("ProductMapper.selectproductList",pd);
    }

    public Map<String ,Object> stockTotal(Long storeId) throws Exception {
        return productNewMapper.stockTotal(storeId);
    }

}
