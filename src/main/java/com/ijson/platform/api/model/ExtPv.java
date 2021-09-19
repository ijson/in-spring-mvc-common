package com.ijson.platform.api.model;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ijson.platform.api.entity.BaseEntity;
import com.ijson.platform.common.util.ObjectId;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * description:  vo方法参数模型
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
@Data
public class ExtPv<E extends BaseEntity> {

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
    private String primaryId;
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

    public static <E extends BaseEntity> ExtPv<E> of(E obj) {
        ExtPv<E> extPv = new ExtPv<>();
        if (obj != null) {
            String id = obj.getId();
            if (Strings.isNullOrEmpty(id)) {
                id = ObjectId.getId();
                obj.setId(id);
            }
            extPv.setPrimaryId(id);
            extPv.setObj(obj);
        }
        return extPv;
    }


    public static <E extends BaseEntity> ExtPv<E> of(String id) {
        ExtPv<E> extPv = new ExtPv<>();
        extPv.setPrimaryId(id);
        return extPv;
    }

    public static <E extends BaseEntity> ExtPv<E> of(String id, Operator operator, Object value) {
        ExtPv<E> extPv = new ExtPv<>();
        extPv.setPrimaryId(id);
        extPv.param(operator.name(), value);
        return extPv;
    }


    public Object getParams(String key) {
        return params.get(key);
    }

    public Boolean getBoolean(String key) {
        Object o = params.get(key);
        if (!(o instanceof Boolean)) {
            return null;
        }
        return (boolean) o;
    }


    public ExtPv param(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public void setObj(E obj) {
        this.primaryId = ObjectId.getId();
        this.obj = obj;
    }

}
