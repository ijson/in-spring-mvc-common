package com.ijson.platform.cache.impl;

import com.google.common.collect.Maps;

import com.ijson.config.ConfigFactory;
import com.ijson.platform.cache.CacheManager;
import com.ijson.platform.cache.manager.ehcache.impl.EhcacheManagerImpl;
import com.ijson.platform.common.util.Validator;

import org.apache.commons.collections.MapUtils;

import java.util.Map;


/**
 * description:  创建缓存实例工厂
 *
 * @author heppy1.com 创建时间：Jan 24, 2015
 */
public class LoadCacheFactory<T> {

    public static LoadCacheFactory instance = new LoadCacheFactory();
    private static Map<String, CacheManager> ehcaches = Maps.newHashMap();
    private Map<String, String> constant = Maps.newHashMap(); //存放系统配置参数

    private void initConfig() {
        if (MapUtils.isEmpty(constant)) {
            ConfigFactory.getConfig("in-cache", (config -> {
                constant = config.getAll();
            }));
            GetCacheHandlerType.constant.putAll(constant);
        }
    }

    /**
     * description:  获取缓存实例
     *
     * @return cacheManager
     * @author cuiyongxu
     */
    public CacheManager<T> getCacheManager(String cacheName) {
        initConfig();
        String cacheType = constant.get("cache_type");
        CacheHandler<T> cacheHandler = createCacheHandlerType(cacheType);
        return cacheHandler.getFactory(cacheName);
    }


    interface CacheHandler<T> {
        CacheManager<T> getFactory(String cacheName);
    }

    private GetCacheHandlerType createCacheHandlerType(String type) {
        return GetCacheHandlerType.valueOf(type);
    }


    enum GetCacheHandlerType implements CacheHandler {
        ehcache {
            @Override
            public CacheManager getFactory(String cacheName) {
                if (Validator.isEmpty(cacheName)) {
                    cacheName = Validator.getDefaultStr(constant.get("cache_default_name"), "ijsonCache");
                }
                if (null == ehcaches.get(cacheName)) {
                    ehcaches.put(cacheName, new EhcacheManagerImpl(cacheName));
                }
                return ehcaches.get(cacheName);
            }
        };

        public static Map<String, String> constant = Maps.newHashMap(); //存放系统配置参数
    }


}
