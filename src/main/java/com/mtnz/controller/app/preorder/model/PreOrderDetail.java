package com.mtnz.controller.app.preorder.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "pre_order_detail")
public class PreOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private Long order_id;

    @Column(name = "product_id")
    private Long product_id;

    @Column(name = "product_name")
    private String product_name;

    @Column(name = "product_price")
    private BigDecimal product_price;

    @Column(name = "purchase_price")
    private BigDecimal purchase_price;

    @Column(name = "num")
    private Integer num;

    @Column(name = "number")
    private BigDecimal number;

    @Column(name = "norms1")
    private BigDecimal norms1;

    @Column(name = "norms2")
    private String norms2;

    @Column(name = "norms3")
    private String norms3;

    @Column(name = "norms4")
    private String norms4;

    @Column(name = "norms5")
    private String norms5;

    @Column(name = "isThreeSales")
    private String isThreeSales;

    @Column(name = "great_time")
    private Date great_time;

    @Column(name = "is_pass")
    private Integer is_pass;

    @Column(name = "isli")
    private Integer isli;

    @Column(name = "store_id")
    private Long store_id;

    public Integer getIsli() {
        return isli;
    }

    public void setIsli(Integer isli) {
        this.isli = isli;
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

    public String getIsThreeSales() {
        return isThreeSales;
    }

    public void setIsThreeSales(String isThreeSales) {
        this.isThreeSales = isThreeSales;
    }

    public Long getId() {
        return id;
    }

    public Long getStore_id() {
        return store_id;
    }

    public void setStore_id(Long store_id) {
        this.store_id = store_id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public BigDecimal getProduct_price() {
        return product_price;
    }

    public void setProduct_price(BigDecimal product_price) {
        this.product_price = product_price;
    }

    public BigDecimal getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(BigDecimal purchase_price) {
        this.purchase_price = purchase_price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getNorms1() {
        return norms1;
    }

    public void setNorms1(BigDecimal norms1) {
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

    public Date getGreat_time() {
        return great_time;
    }

    public void setGreat_time(Date great_time) {
        this.great_time = great_time;
    }

    public Integer getIs_pass() {
        return is_pass;
    }

    public void setIs_pass(Integer is_pass) {
        this.is_pass = is_pass;
    }
}
