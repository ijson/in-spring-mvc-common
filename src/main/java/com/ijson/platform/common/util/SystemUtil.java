package com.ijson.platform.common.util;

import com.ijson.config.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * description:  系统工具类
 *
 * @author cuiyongxu 创建时间：Oct 8, 2015
 */
public class SystemUtil {

    private static SystemUtil instance;

    private Map<String, String> constant = new HashMap<String, String>();//存放系统配置参数

    /**
     * description:  启用单例模式
     *
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public synchronized static SystemUtil getInstance() {
        if (null == instance) {
            instance = new SystemUtil();
        }
        return instance;
    }

    /**
     * description:  加载配置文件
     *
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    private void initConfig() {
        ConfigFactory.getConfig("in-config", config -> {
            constant = config.getAll();
        });
    }

    /**
     * description:  获取所有配置信息
     *
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Map<String, String> getConstant() {
        initConfig();
        return constant;
    }

    /**
     * description:  根据指定的KEY获取相应配置信息
     *
     * @param key 指定的KEY
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String getConstant(String key) {
        initConfig();
        return constant.get(key);
    }
}
