package com.mtnz.controller.app.permissions.model;

import org.apache.poi.ss.formula.functions.T;

import javax.persistence.*;
import java.util.List;

@Table(name = "store_permissions")
public class StorePermissions {

    @Id
    @Column(name = "id")
    private Long id;
    /** 用户id*/
    @Column(name = "userId")
    private Long userId;
    /** '店铺id'*/
    @Column(name = "storeId")
    private Long storeId;
    /** 权限id*/
    @Column(name = "permissionId")
    private Long permissionId;
    /** 权限类型:0无权限1有权限*/
    @Column(name = "permType")
    private Integer permType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getPermType() {
        return permType;
    }

    public void setPermType(Integer permType) {
        this.permType = permType;
    }

    @Override
    public String toString() {
        return "StorePermissions{" +
                "id=" + id +
                ", userId=" + userId +
                ", storeId=" + storeId +
                ", permissionId=" + permissionId +
                ", permType=" + permType +
                '}';
    }
}
