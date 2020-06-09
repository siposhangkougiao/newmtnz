package com.mtnz.controller.base;

import java.io.Serializable;

/**
* 返回结果给调用者
*
* @author Administrator
*
*/
public class Result<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 315103965831826836L;

    public Result() {
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(String msg,T data) {
        this.msg = msg;
        this.data = data;
    }


    /**
     * 编码:0-成功,1-失败
     */
    private int code;
    /**
     * 异常信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
