package com.ijson.platform.api.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ijson.platform.api.entity.BaseEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * description:  vo方法参数模型
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
@Data
public class ExtPv<E> extends BaseEntity {

    /**
     * 插件类型
     */
    private String pluginKey;
    /**
     * 操作人
     */
    private AuthContext context;
    /**
     * 主键ID
     */
    private Object primaryId;
    /**
     * 待操作的单对象,需要对当前对象增删改查等
     */
    private E obj;
    /**
     * 待操作批量对象,如果存在需要对对象批量操作,则可以将对象存至此List中
     */
    private List<E> objs = Lists.newArrayList();
    /**
     * 执行方法所需参数,在不同方法中需要的参数不同,可以存至map中,便于其他地方使用
     */
    private Map<String, Object> params = Maps.newHashMap();


    public Object getParams(String key) {
        return params.get(key);
    }

    public void setParams(String key, Object value) {
        params.put(key, value);
    }


    public static <E> ExtPv<E> id(Object infoId) {
        ExtPv<E> vo = new ExtPv();
        vo.setPrimaryId(infoId);
        return vo;
    }

}
