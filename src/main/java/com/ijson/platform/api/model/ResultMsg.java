package com.ijson.platform.api.model;

import lombok.Getter;
import lombok.Setter;

/**
 * description:  JSON返回对象封装
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
@Setter
@Getter
public class ResultMsg extends BaseEntity {

    /**
     * 需要返回的标示,例如:T或者F
     */
    private String isok;
    /**
     * 返回给前台的数据,Object类型,业务系统自处理
     */
    private Object msg;

    private Boolean flag;

    public ResultMsg() {

    }

    public ResultMsg(String isok, Object msg) {
        this.isok = isok;
        this.msg = msg;
    }

    public ResultMsg setError(Object msg){
        this.isok = "F";
        this.msg = msg;
        return this;
    }

    public ResultMsg setError(Object msg,Boolean flag){
        this.isok = "F";
        this.msg = msg;
        this.flag = flag;
        return this;
    }

    public ResultMsg setOK(Object msg,Boolean flag){
        this.isok = "T";
        this.msg = msg;
        this.flag = flag;
        return this;
    }

    public ResultMsg setOK(Object msg){
        this.isok = "T";
        this.msg = msg;
        return this;
    }

}
