package com.mtnz.controller.app.product.model;


import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public class ProductVo {

    /**
     * {
     *     "supplier_id":"0",
     *     "kucun":"55",
     *     "threePurchase":"35.0",
     *     "status":"2",
     *     "production_enterprise":"",
     *     "product_price":"25",
     *     "product_name":"脱光光",
     *     "store_id":"469",
     *     "product_img":"",
     *     "number":"",
     *     "type":"肥料",
     *     "number_tow":"",
     *     "url":"",
     *     "isThreeSales":"1",
     *     "purchase_price":"25",
     *     "bar_code_number":"",
     *     "IStatus":"2",
     *     "norms5":"包",
     *     "type2":"钙镁肥",
     *     "norms4":"35.0",
     *     "norms3":"瓶",
     *     "norms2":"kg",
     *     "norms1":"25.0"
     * }
     *
     * */


    /*@NotBlank
    private String store_id;
    @NotBlank
    private String product_name;
    @NotBlank
    private String product_price;
    @NotBlank
    private String norms1;

    private String norms2;
    private String norms3;
    private String norms4;
    private String norms5;
    private String threePurchase="0";
    @NotBlank
    private String purchase_price="0"; 10
    private String production_enterprise="";
    private String product_img;
    private String status;
    private String IStatus;
    @NotBlank
    private String kucun="0";
    private String type="其他";
    private String bar_code_number;
    private String supplier_id="0";
    private String number="";
    private String url="";
    private String number_tow="";
    private String type2="";
    private String level1_price;
    private String level2_price;
    private String level3_price;
    private String isThreeSales; 26

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getNorms1() {
        return norms1;
    }

    public void setNorms1(String norms1) {
        this.norms1 = norms1;
    }

    public String getNorms2() {
        return norms2;
    }

    public void setNorms2(String norms2) {
        this.norms2 = norms2;
    }

    public String getNorms3() {
        return norms3;
    }

    public void setNorms3(String norms3) {
        this.norms3 = norms3;
    }

    public String getNorms4() {
        return norms4;
    }

    public void setNorms4(String norms4) {
        this.norms4 = norms4;
    }

    public String getNorms5() {
        return norms5;
    }

    public void setNorms5(String norms5) {
        this.norms5 = norms5;
    }

    public String getThreePurchase() {
        return threePurchase;
    }

    public void setThreePurchase(String threePurchase) {
        this.threePurchase = threePurchase;
    }

    public String getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        this.purchase_price = purchase_price;
    }

    public String getProduction_enterprise() {
        return production_enterprise;
    }

    public void setProduction_enterprise(String production_enterprise) {
        this.production_enterprise = production_enterprise;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKucun() {
        return kucun;
    }

    public void setKucun(String kucun) {
        this.kucun = kucun;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBar_code_number() {
        return bar_code_number;
    }

    public void setBar_code_number(String bar_code_number) {
        this.bar_code_number = bar_code_number;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNumber_tow() {
        return number_tow;
    }

    public void setNumber_tow(String number_tow) {
        this.number_tow = number_tow;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getLevel1_price() {
        return level1_price;
    }

    public void setLevel1_price(String level1_price) {
        this.level1_price = level1_price;
    }

    public String getLevel2_price() {
        return level2_price;
    }

    public void setLevel2_price(String level2_price) {
        this.level2_price = level2_price;
    }

    public String getLevel3_price() {
        return level3_price;
    }

    public void setLevel3_price(String level3_price) {
        this.level3_price = level3_price;
    }

    public String getIsThreeSales() {
        return isThreeSales;
    }

    public void setIsThreeSales(String isThreeSales) {
        this.isThreeSales = isThreeSales;
    }*/

    private String product_id;
    private String supplier_id;
    @NotBlank
    private String kucun;
    private String threePurchase;

    private String production_enterprise;
    private String product_price;
    private String product_name;
    @NotBlank
    private String store_id;
    private String product_img;
    private String number;
    private String type;
    private String number_tow;
    private String url;
    private String isThreeSales;
    private String purchase_price;
    private String bar_code_number;
    private String status;
    private String norms5;
    private String type2;
    private String norms4;
    private String norms3;
    private String norms2;
    @NotBlank
    private String norms1;
    private String level1_price;

    public String getLevel1_price() {
        return level1_price;
    }

    public void setLevel1_price(String level1_price) {
        this.level1_price = level1_price;
    }

    public String getLevel2_price() {
        return level2_price;
    }

    public void setLevel2_price(String level2_price) {
        this.level2_price = level2_price;
    }

    public String getLevel3_price() {
        return level3_price;
    }

    public void setLevel3_price(String level3_price) {
        this.level3_price = level3_price;
    }

    private String level2_price;
    private String level3_price;
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getKucun() {
        return kucun;
    }

    public void setKucun(String kucun) {
        this.kucun = kucun;
    }

    public String getThreePurchase() {
        return threePurchase;
    }

    public void setThreePurchase(String threePurchase) {
        this.threePurchase = threePurchase;
    }



    public String getProduction_enterprise() {
        return production_enterprise;
    }

    public void setProduction_enterprise(String production_enterprise) {
        this.production_enterprise = production_enterprise;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber_tow() {
        return number_tow;
    }

    public void setNumber_tow(String number_tow) {
        this.number_tow = number_tow;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsThreeSales() {
        return isThreeSales;
    }

    public void setIsThreeSales(String isThreeSales) {
        this.isThreeSales = isThreeSales;
    }

    public String getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        this.purchase_price = purchase_price;
    }

    public String getBar_code_number() {
        return bar_code_number;
    }

    public void setBar_code_number(String bar_code_number) {
        this.bar_code_number = bar_code_number;
    }



    public String getNorms5() {
        return norms5;
    }

    public void setNorms5(String norms5) {
        this.norms5 = norms5;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getNorms4() {
        return norms4;
    }

    public void setNorms4(String norms4) {
        this.norms4 = norms4;
    }

    public String getNorms3() {
        return norms3;
    }

    public void setNorms3(String norms3) {
        this.norms3 = norms3;
    }

    public String getNorms2() {
        return norms2;
    }

    public void setNorms2(String norms2) {
        this.norms2 = norms2;
    }

    public String getNorms1() {
        return norms1;
    }

    public void setNorms1(String norms1) {
        this.norms1 = norms1;
    }
















}
