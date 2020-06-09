package com.mtnz.controller.app.product.model;


import com.mtnz.controller.app.supplier.model.Supplier;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "product")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  private Long productId;

  @Column(name = "store_id")
  private Long storeId;

  @Column(name = "product_name")
  private String productName;

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

  @Column(name = "threePurchase")
  private String threePurchase;

  @Column(name = "product_price")
  private String productPrice;

  @Column(name = "purchase_price")
  private String purchasePrice;

  @Column(name = "production_enterprise")
  private String productionEnterprise;

  @Column(name = "product_img")
  private String productImg;

  @Column(name = "date")
  private String date;

  @Column(name = "status")
  private Integer status;

  @Column(name = "kucun")
  private BigDecimal kucun;

  @Column(name = "likucun")
  private BigDecimal likucun;

  @Column(name = "type")
  private String type;

  @Column(name = "bar_code")
  private String barCode;

  @Column(name = "bar_code_number")
  private String barCodeNumber;

  @Column(name = "supplier_id")
  private Long supplierId;

  @Column(name = "number")
  private String number;

  @Column(name = "url")
  private String url;

  @Column(name = "number_tow")
  private String numberTow;

  @Column(name = "type2")
  private String type2;

  @Column(name = "lose")
  private Integer lose;
  @Column(name = "level1_price")
  private BigDecimal level1Price;
  @Column(name = "level2_price")
  private BigDecimal level2Price;
  @Column(name = "level3_price")
  private BigDecimal level3Price;
  @Column(name = "isThreeSales")
  private Integer isThreeSales;

  @Transient
  private String name;

  @Transient
  private Integer count;

  @Transient
  private Integer pageNumber=1;

  @Transient
  private Integer pageSize=10;

  @Transient
  List<ProductImg> imgs = new ArrayList<>();

  @Transient
  Supplier supplier;

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }

  public List<ProductImg> getImgs() {
    return imgs;
  }

  public void setImgs(List<ProductImg> imgs) {
    this.imgs = imgs;
  }

  public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
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

  public String getProductionEnterprise() {
    return productionEnterprise;
  }

  public void setProductionEnterprise(String productionEnterprise) {
    this.productionEnterprise = productionEnterprise;
  }

  public String getProductImg() {
    return productImg;
  }

  public void setProductImg(String productImg) {
    this.productImg = productImg;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public Integer getStatus(int i) {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public BigDecimal getKucun() {
    return kucun;
  }

  public void setKucun(BigDecimal kucun) {
    this.kucun = kucun;
  }

  public BigDecimal getLikucun() {
    return likucun;
  }

  public void setLikucun(BigDecimal likucun) {
    this.likucun = likucun;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getBarCode() {
    return barCode;
  }

  public void setBarCode(String barCode) {
    this.barCode = barCode;
  }

  public String getBarCodeNumber() {
    return barCodeNumber;
  }

  public void setBarCodeNumber(String barCodeNumber) {
    this.barCodeNumber = barCodeNumber;
  }

  public Long getSupplierId() {
    return supplierId;
  }

  public void setSupplierId(Long supplierId) {
    this.supplierId = supplierId;
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

  public String getNumberTow() {
    return numberTow;
  }

  public void setNumberTow(String numberTow) {
    this.numberTow = numberTow;
  }

  public String getType2() {
    return type2;
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

  public Integer getLose() {
    return lose;
  }

  public void setLose(Integer lose) {
    this.lose = lose;
  }

  public BigDecimal getLevel1Price() {
    return level1Price;
  }

  public void setLevel1Price(BigDecimal level1Price) {
    this.level1Price = level1Price;
  }

  public BigDecimal getLevel2Price() {
    return level2Price;
  }

  public void setLevel2Price(BigDecimal level2Price) {
    this.level2Price = level2Price;
  }

  public BigDecimal getLevel3Price() {
    return level3Price;
  }

  public void setLevel3Price(BigDecimal level3Price) {
    this.level3Price = level3Price;
  }

  public Integer getIsThreeSales() {
    return isThreeSales;
  }

  public void setIsThreeSales(Integer isThreeSales) {
    this.isThreeSales = isThreeSales;
  }

  public void setType2(String type2) {
    this.type2 = type2;
  }
}
