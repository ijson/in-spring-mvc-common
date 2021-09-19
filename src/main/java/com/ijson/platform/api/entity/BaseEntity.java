package com.ijson.platform.api.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * description:  实体模型父类,所有的实体类需要集成此类,封装Serializable
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
@Data
public class BaseEntity implements Serializable {


    protected String id;
    private String shortUrl;
    private Boolean deleted = false;
    private Boolean enable = true;
    private String createdBy;
    private long createTime = System.currentTimeMillis();
    private String lastModifiedBy;
    private long lastModifiedTime = System.currentTimeMillis();

    public Boolean getDeleted() {
        return deleted != null && deleted;
    }

    public Boolean getEnable() {
        return enable != null && enable;
    }
}
