package com.mtnz.controller.app.product.model;


import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "kuncun")
public class KuCun {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "kuncun_id")
  private Long kuncunId;

  @Column(name = "product_id")
  private Long productId;

  @Column(name = "store_id")
  private Long storeId;

  @Column(name = "store_id")
  private Long supplierId;

  @Column(name = "customer_id")
  private Long customerId;

  @Column(name = "status")
  private String status;

  @Column(name = "num")
  private BigDecimal num;

  @Column(name = "nums")
  private BigDecimal nums;

  @Column(name = "likucun")
  private BigDecimal likucun;

  @Column(name = "all_number")
  private BigDecimal allNumber;

  @Column(name = "now_number")
  private BigDecimal nowNumber;

  @Column(name = "date")
  private String date;

  @Column(name = "total")
  private String total;

  @Column(name = "order_info_id")
  private Long orderInfoId;

  @Column(name = "product_price")
  private BigDecimal productPrice;

  @Column(name = "product_price")
  private BigDecimal purchase_price;

  @Column(name = "money")
  private BigDecimal money;

  @Column(name = "jia")
  private String jia;

  @Column(name = "revokes")
  private String revokes;

  @Column(name = "id")
  private String id;

  @Column(name = "purchaseWay")
  private Integer purchaseWay;

  public Integer getPurchaseWay() {
    return purchaseWay;
  }

  public void setPurchaseWay(Integer purchaseWay) {
    this.purchaseWay = purchaseWay;
  }

  public Long getKuncunId() {
    return kuncunId;
  }

  public void setKuncunId(Long kuncunId) {
    this.kuncunId = kuncunId;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Long getStoreId() {
    return storeId;
  }

  public void setStoreId(Long storeId) {
    this.storeId = storeId;
  }

  public Long getSupplierId() {
    return supplierId;
  }

  public void setSupplierId(Long supplierId) {
    this.supplierId = supplierId;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public BigDecimal getNum() {
    return num;
  }

  public void setNum(BigDecimal num) {
    this.num = num;
  }

  public BigDecimal getNums() {
    return nums;
  }

  public void setNums(BigDecimal nums) {
    this.nums = nums;
  }

  public BigDecimal getLikucun() {
    return likucun;
  }

  public void setLikucun(BigDecimal likucun) {
    this.likucun = likucun;
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

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public Long getOrderInfoId() {
    return orderInfoId;
  }

  public void setOrderInfoId(Long orderInfoId) {
    this.orderInfoId = orderInfoId;
  }

  public BigDecimal getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(BigDecimal productPrice) {
    this.productPrice = productPrice;
  }

  public BigDecimal getPurchase_price() {
    return purchase_price;
  }

  public void setPurchase_price(BigDecimal purchase_price) {
    this.purchase_price = purchase_price;
  }

  public BigDecimal getMoney() {
    return money;
  }

  public void setMoney(BigDecimal money) {
    this.money = money;
  }

  public String getJia() {
    return jia;
  }

  public void setJia(String jia) {
    this.jia = jia;
  }

  public String getRevokes() {
    return revokes;
  }

  public void setRevokes(String revokes) {
    this.revokes = revokes;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
