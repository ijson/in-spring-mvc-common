package com.ijson.platform.database.model;

import com.google.common.collect.Maps;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * description:   方法参数模型
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
@Getter
@Setter
public class MethodParam <T>{

    /**
     * 待操作对象
     */
    private Object vaule;

    /**
     * 执行KEY,只要集中在manager
     */
    private String key;

    /**
     * 要缓存的对象ID值
     */
    private String cacheId = "";

    /**
     * 主键id
     */
    private String infoId = "";

    /**
     * 参数列表
     */
    private Map<String, Object> params = Maps.newHashMap();

    /**
     * 自定义sql
     */
    private String sqlStr = "";

    private boolean isDelete = false;//是否执行物理删除

    /**
     * 以下参数为分页查询时使用
     */
    private int PageIndex = 1;//当前页
    private int pageSize = 20;//每页行数
    private long pageCount = 0;//总记录数
    private String orderby = "";//排序条件

    private String spanceName;//空间名（当启用ibatis是此参数不能为空）

    public MethodParam() {
    }

    /**
     * Creates a new instance of MethodParam.
     *
     * @param key        属性字段
     * @param cacheId    缓存Id
     * @param sqlStr     sql
     * @param spanceName 实体名  package+class,例如:com.ijson.entity.User
     */
    public MethodParam(String key, String cacheId, String sqlStr, String spanceName) {
        this.cacheId = cacheId;
        this.key = key;
        this.spanceName = spanceName;
        this.sqlStr = sqlStr;
    }

    public void setParams(String key, Object value) {
        this.params.put(key, value);
    }
}
