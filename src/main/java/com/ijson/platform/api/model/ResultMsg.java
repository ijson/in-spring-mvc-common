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

    public ResultMsg() {

    }

    public ResultMsg(String isok, String msg) {
        this.isok = isok;
        this.msg = msg;
    }

}
