package com.mtnz.controller.app.order.model;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "order_info")
public class OrderInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="order_info_id")
  private Long orderInfoId;

  @Column(name="order_number")
  private String orderNumber;

  @Column(name="store_id")
  private Integer storeId;

  @Column(name="customer_id")
  private Integer customerId;

  @Column(name="name")
  private String name;

  @Column(name="phone")
  private String phone;

  @Column(name="address")
  private String address;

  @Column(name="money")
  private String money;

  @Column(name="date")
  private Date date;

  @Column(name="status")
  private String status;

  @Column(name="owe_money")
  private String oweMoney;

  @Column(name="total_money")
  private String totalMoney;

  @Column(name="discount_money")
  private String discountMoney;

  @Column(name="medication_date")
  private String medicationDate;

  @Column(name="remarks")
  private String remarks;

  @Column(name="revokes")
  private String revokes;

  @Column(name="open_bill")
  private String openBill;

  @Column(name="product_sale")
  private BigDecimal productSale;

  @Column(name="total_sale")
  private BigDecimal totalSale;

  @Column(name="outboundStatus")
  private Integer outboundStatus;

  public Integer getOutboundStatus() {
    return outboundStatus;
  }

  public void setOutboundStatus(Integer outboundStatus) {
    this.outboundStatus = outboundStatus;
  }

  public Long getOrderInfoId() {
    return orderInfoId;
  }

  public void setOrderInfoId(Long orderInfoId) {
    this.orderInfoId = orderInfoId;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public Integer getStoreId() {
    return storeId;
  }

  public void setStoreId(Integer storeId) {
    this.storeId = storeId;
  }

  public Integer getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getMoney() {
    return money;
  }

  public void setMoney(String money) {
    this.money = money;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getOweMoney() {
    return oweMoney;
  }

  public void setOweMoney(String oweMoney) {
    this.oweMoney = oweMoney;
  }

  public String getTotalMoney() {
    return totalMoney;
  }

  public void setTotalMoney(String totalMoney) {
    this.totalMoney = totalMoney;
  }

  public String getDiscountMoney() {
    return discountMoney;
  }

  public void setDiscountMoney(String discountMoney) {
    this.discountMoney = discountMoney;
  }

  public String getMedicationDate() {
    return medicationDate;
  }

  public void setMedicationDate(String medicationDate) {
    this.medicationDate = medicationDate;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getRevokes() {
    return revokes;
  }

  public void setRevokes(String revokes) {
    this.revokes = revokes;
  }

  public String getOpenBill() {
    return openBill;
  }

  public void setOpenBill(String openBill) {
    this.openBill = openBill;
  }

  public BigDecimal getProductSale() {
    return productSale;
  }

  public void setProductSale(BigDecimal productSale) {
    this.productSale = productSale;
  }

  public BigDecimal getTotalSale() {
    return totalSale;
  }

  public void setTotalSale(BigDecimal totalSale) {
    this.totalSale = totalSale;
  }
}
