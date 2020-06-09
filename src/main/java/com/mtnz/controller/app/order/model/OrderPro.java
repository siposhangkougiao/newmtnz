package com.mtnz.controller.app.order.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "order_pro")
public class OrderPro {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_pro_id")
  private Long orderProId;

  @Column(name = "order_pro_id")
  private Long orderInfoId;

  @Column(name = "product_name")
  private String productName;

  @Column(name = "product_price")
  private String productPrice;

  @Column(name = "purchase_price")
  private String purchasePrice;

  @Column(name = "num")
  private Integer num;

  @Column(name = "total")
  private String total;

  @Column(name = "orde_by")
  private Integer ordeBy;

  @Column(name = "norms1")
  private String norms1;

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

  @Column(name = "product_id")
  private Integer productId;

  @Column(name = "order_kuncun")
  private Integer orderKuncun;

  @Column(name = "all_number")
  private BigDecimal allNumber;

  @Column(name = "now_number")
  private BigDecimal nowNumber;

  @Column(name = "status")
  private String status;

  @Column(name = "date")
  private Date date;

  @Column(name = "revokes")
  private String revokes;

  @Column(name = "product_sale")
  private BigDecimal productSale;

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

  public Long getOrderProId() {
    return orderProId;
  }

  public void setOrderProId(Long orderProId) {
    this.orderProId = orderProId;
  }

  public Long getOrderInfoId() {
    return orderInfoId;
  }

  public void setOrderInfoId(Long orderInfoId) {
    this.orderInfoId = orderInfoId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(String productPrice) {
    this.productPrice = productPrice;
  }

  public String getPurchasePrice() {
    return purchasePrice;
  }

  public void setPurchasePrice(String purchasePrice) {
    this.purchasePrice = purchasePrice;
  }

  public Integer getNum() {
    return num;
  }

  public void setNum(Integer num) {
    this.num = num;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public Integer getOrdeBy() {
    return ordeBy;
  }

  public void setOrdeBy(Integer ordeBy) {
    this.ordeBy = ordeBy;
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

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public Integer getOrderKuncun() {
    return orderKuncun;
  }

  public void setOrderKuncun(Integer orderKuncun) {
    this.orderKuncun = orderKuncun;
  }

  public BigDecimal getAllNumber() {
    return allNumber;
  }

  public void setAllNumber(BigDecimal allNumber) {
    this.allNumber = allNumber;
  }

  public BigDecimal getNowNumber() {
    return nowNumber;
  }

  public void setNowNumber(BigDecimal nowNumber) {
    this.nowNumber = nowNumber;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getRevokes() {
    return revokes;
  }

  public void setRevokes(String revokes) {
    this.revokes = revokes;
  }

  public BigDecimal getProductSale() {
    return productSale;
  }

  public void setProductSale(BigDecimal productSale) {
    this.productSale = productSale;
  }
}
