package com.mtnz.controller.app.user.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "sys_app_user")
public class SysAppUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "uid")
  private Long uid;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "salt")
  private String salt;

  @Column(name = "name")
  private String name;

  @Column(name = "openid")
  private String openid;

  @Column(name = "unionid")
  private String unionid;

  @Column(name = "status")
  private String status;

  @Column(name = "store_id")
  private Long storeId;

  @Column(name = "date")
  private String date;

  @Column(name = "province")
  private String province;

  @Column(name = "city")
  private String city;

  @Column(name = "district")
  private String district;

  @Column(name = "address")
  private String address;

  @Column(name = "person_id")
  private String personId;

  @Column(name = "phone")
  private String phone;

  @Column(name = "corp")
  private String corp;

  @Column(name = "login_date")
  private String loginDate;

  @Column(name = "identity")
  private Integer identity;

  @Column(name = "diedstatus")
  private Integer diedstatus;

  @Column(name = "is_delete")
  private Integer isDelete;

  @Column(name = "nickName")
  private String nickName;

  @Column(name = "header")
  private String header;

  @Column(name = "signature")
  private String signature;


  public Long getUid() {
    return uid;
  }

  public void setUid(Long uid) {
    this.uid = uid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOpenid() {
    return openid;
  }

  public void setOpenid(String openid) {
    this.openid = openid;
  }

  public String getUnionid() {
    return unionid;
  }

  public void setUnionid(String unionid) {
    this.unionid = unionid;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getStoreId() {
    return storeId;
  }

  public void setStoreId(Long storeId) {
    this.storeId = storeId;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPersonId() {
    return personId;
  }

  public void setPersonId(String personId) {
    this.personId = personId;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getCorp() {
    return corp;
  }

  public void setCorp(String corp) {
    this.corp = corp;
  }

  public String getLoginDate() {
    return loginDate;
  }

  public void setLoginDate(String loginDate) {
    this.loginDate = loginDate;
  }

  public Integer getIdentity() {
    return identity;
  }

  public void setIdentity(Integer identity) {
    this.identity = identity;
  }

  public Integer getDiedstatus() {
    return diedstatus;
  }

  public void setDiedstatus(Integer diedstatus) {
    this.diedstatus = diedstatus;
  }

  public Integer getIsDelete() {
    return isDelete;
  }

  public void setIsDelete(Integer isDelete) {
    this.isDelete = isDelete;
  }
}
