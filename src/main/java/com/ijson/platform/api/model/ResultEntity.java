package com.ijson.platform.api.model;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * description:  内容传输对象,给插件提供
 *
 * @author cuiyongxu 创建时间：2016年1月4日
 */
@Data
public class ResultEntity extends BaseEntity {

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, Object entity) {
        this.result = result;
        this.entity = entity;
        this.message = message;
    }

    public ResultEntity(String result, String message) {
        this.result = result;
        this.message = message;
    }

    /**
     * serialVersionUID:TODO(用一句话描述这个变量表示什么).
     */
    private static final long serialVersionUID = 1L;
    private String result;
    private Object entity;
    private String message;
    private Map<String, Object> params = Maps.newHashMap();

}
