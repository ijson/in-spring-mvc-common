package com.ijson.platform.api;


import com.ijson.platform.api.entity.BaseEntity;

/**
 * description:  springmvc的基类，公共方法<br>所有的控制层.全部继承此类
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
public abstract class BaseAction<T extends BaseEntity> {
    /**
     * 由子类自定义前缀
     *
     * @return prefix
     */
    protected abstract String getPrefix();

    /**
     * 全局变量,前缀+save,前缀:/user/ 输出:/user/save
     */
    protected final String ADD = getPrefix() + "save";
    /**
     * 全局变量,前缀+save,前缀:/user/ 输出:/user/save
     */
    protected final String EDIT = getPrefix() + "save";
    /**
     * 全局变量,前缀+view,前缀:/user/ 输出:/user/view
     */
    protected final String VIEW = getPrefix() + "view";
    /**
     * 全局变量,前缀+list,前缀:/user/ 输出:/user/list
     */
    protected final String LIST = getPrefix() + "list";


}
