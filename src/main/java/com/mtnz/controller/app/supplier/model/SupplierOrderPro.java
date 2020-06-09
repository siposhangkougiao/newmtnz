package com.mtnz.controller.app.supplier.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "supplier_order_pro")
public class SupplierOrderPro {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "supplier_order_pro_id")
  private Long supplierOrderProId;

  @Column(name = "supplier_order_info_id")
  private Long supplierOrderInfoId;

  @Column(name = "product_name")
  private String productName;

  @Column(name = "product_price")
  private String productPrice;

  @Column(name = "num")
  private BigDecimal num;

  @Column(name = "nums")
  private BigDecimal nums;

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

  @Column(name = "revokes")
  private String revokes;

  @Column(name = "product_id")
  private Long productId;

  /**
   * 1 加数量  其他减数量
   */
  @Transient
  private Integer type;

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

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Long getSupplierOrderProId() {
    return supplierOrderProId;
  }

  public void setSupplierOrderProId(Long supplierOrderProId) {
    this.supplierOrderProId = supplierOrderProId;
  }

  public Long getSupplierOrderInfoId() {
    return supplierOrderInfoId;
  }

  public void setSupplierOrderInfoId(Long supplierOrderInfoId) {
    this.supplierOrderInfoId = supplierOrderInfoId;
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

  public String getRevokes() {
    return revokes;
  }

  public void setRevokes(String revokes) {
    this.revokes = revokes;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }
}
