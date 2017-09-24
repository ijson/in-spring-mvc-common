package com.ijson.platform.cache.impl;

import com.google.common.collect.Maps;
import com.ijson.config.ConfigFactory;
import com.ijson.platform.cache.CacheManager;
import com.ijson.platform.cache.manager.ehcache.impl.EhcacheManagerImpl;
import com.ijson.platform.common.util.Validator;

import java.util.Map;


/**
 * description:  创建缓存实例工厂
 *
 * @author heppy1.com 创建时间：Jan 24, 2015
 */
public class LoadCacheFactory {

    private static LoadCacheFactory instance;
    private static Map<String, CacheManager> ehcaches = Maps.newHashMap();
    private Map<String, String> constant = Maps.newHashMap(); //存放系统配置参数

    public static LoadCacheFactory getInstance() {
        if (null == instance)
            instance = new LoadCacheFactory();
        return instance;
    }

    private String initConfig(String key) {
        ConfigFactory.getConfig("in-cache", (config -> {
            constant = config.getAll();
        }));
        return constant.get(key);
    }

    /**
     * description:  获取缓存实例
     *
     * @param cacheName 缓存空间名称
     * @author cuiyongxu
     * @return cacheManager
     */
    public CacheManager getCacheManager(String cacheName) {
        return getEhcacheManager(cacheName);
    }


    /**
     * description: 获取ehcache缓存实例
     *
     * @param cacheName 缓存空间名称
     * @author cuiyongxu
     * @return cacheManager
     */
    private CacheManager getEhcacheManager(String cacheName) {
        if (Validator.isNull(cacheName))
            cacheName = Validator.getDefaultStr(initConfig("cache_default_name"), "ijsonCache");
        if (null == ehcaches.get(cacheName)) {
            CacheManager cacheManager = new EhcacheManagerImpl();
            cacheManager.setCacheName(cacheName);
            ehcaches.put(cacheName, cacheManager);
        }
        return ehcaches.get(cacheName);
    }

}
