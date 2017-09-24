package com.ijson.platform.generator.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:  vo方法参数模型
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
@Setter
@Getter
public class ParamsVo<E>  {

    private String key;
    /**
     * 为插件提供的参数值
     */
    private String methodKey;
    /**
     * 当前操作用户ID
     */
    private String userId;
    /**
     * 信息ID,多为主键ID
     */
    private String infoId;
    /**
     * 待操作的单对象,需要对当前对象增删改查等
     */
    private E obj;
    /**
     * 待操作批量对象,如果存在需要对对象批量操作,则可以将对象存至此List中
     */
    private List<E> objs = new ArrayList<E>();
    /**
     * 执行方法所需参数,在不同方法中需要的参数不同,可以存至map中,便于其他地方使用
     */
    private Map<String, Object> params = new HashMap<String, Object>();
    /**
     * 当前操作用户Ename
     */
    private String userEname;

    public Object getParams(String key) {
        return params.get(key);
    }

    public void setParams(String key, Object value) {
        params.put(key, value);
    }

}
