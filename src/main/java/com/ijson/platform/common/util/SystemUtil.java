package com.ijson.platform.common.util;

import com.google.common.collect.Maps;

import com.ijson.config.ConfigFactory;

import java.util.Map;

/**
 * 系统工具类
 *
 * @author cuiyongxu 创建时间：Oct 8, 2015
 */
public class SystemUtil {


    private SystemUtil(){
    }

    private static SystemUtil instance;

    private Map<String, String> constant = Maps.newHashMap();//存放系统配置参数

    /**
     * 启用单例模式
     *
     * @return instance
     * @author cuiyongxu
     */
    public static synchronized SystemUtil getInstance() {
        if (null == instance) {
            synchronized (SystemUtil.class){
                if(instance==null){
                    instance=new SystemUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 加载配置文件
     *
     * @author cuiyongxu
     */
    private void initConfig() {
        ConfigFactory.getConfig("in-common-config", config -> {
            constant = config.getAll();
        });
    }

    /**
     * 获取所有配置信息
     *
     * @return map
     * @author cuiyongxu
     */
    public Map<String, String> getConstant() {
        initConfig();
        return constant;
    }

    /**
     * 根据指定的KEY获取相应配置信息
     *
     * @param key 指定的KEY
     * @return value
     * @author cuiyongxu
     */
    String getConstant(String key) {
        initConfig();
        return constant.get(key);
    }
}
