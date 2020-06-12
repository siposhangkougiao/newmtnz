package com.mtnz.controller.app.order.model;

import java.math.BigDecimal;
import java.util.Date;

public class OrderGift {

    private Long id;

    private Long order_info_id;

    private Date great_time;

    private Long product_id;

    private BigDecimal num;

    private BigDecimal number;

    private Integer is_back;

    private BigDecimal purchase_price;

    private BigDecimal total_purchase_price;

    private BigDecimal product_price;

    private Integer isThreeSales;

    public BigDecimal getProduct_price() {
        return product_price;
    }

    public void setProduct_price(BigDecimal product_price) {
        this.product_price = product_price;
    }

    public Integer getIsThreeSales() {
        return isThreeSales;
    }

    public void setIsThreeSales(Integer isThreeSales) {
        this.isThreeSales = isThreeSales;
    }

    public BigDecimal getTotal_purchase_price() {
        return total_purchase_price;
    }

    public void setTotal_purchase_price(BigDecimal total_purchase_price) {
        this.total_purchase_price = total_purchase_price;
    }

    public BigDecimal getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(BigDecimal purchase_price) {
        this.purchase_price = purchase_price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrder_info_id() {
        return order_info_id;
    }

    public void setOrder_info_id(Long order_info_id) {
        this.order_info_id = order_info_id;
    }

    public Date getGreat_time() {
        return great_time;
    }

    public void setGreat_time(Date great_time) {
        this.great_time = great_time;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public Integer getIs_back() {
        return is_back;
    }

    public void setIs_back(Integer is_back) {
        this.is_back = is_back;
    }
}
