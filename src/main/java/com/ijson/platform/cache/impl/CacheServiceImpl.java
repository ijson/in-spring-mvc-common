package com.ijson.platform.cache.impl;

import com.ijson.platform.cache.CacheManager;
import com.ijson.platform.cache.CacheService;

import java.util.List;


/**
 * description:  缓存外部接品实现类
 *
 * @author heppy1.com 创建时间：Jan 24, 2015
 */
public class CacheServiceImpl implements CacheService {

    /**
     * description:  获取缓存实例
     *
     * @param cacheName 缓存空间名称
     * @return cacheManager
     * @author cuiyongxu
     */
    public CacheManager getCache(String cacheName) {
        return LoadCacheFactory.getInstance().getCacheManager(cacheName);
    }

    public boolean createCacheObject(String cacheName, String key, Object object) {
        return getCache(cacheName).createCacheObject(key, object);
    }

    public boolean checkCacheObject(String cacheName, String key) {
        return getCache(cacheName).checkCacheObject(key);
    }

    public Object getCacheObjectByKey(String cacheName, String key) {
        return getCache(cacheName).getCacheCloneByKey(key);
    }

    public List<Object> getObjects(String cacheName, List<String> keys, String prefix) {
        return getCache(cacheName).getObjects(keys, prefix);
    }

    public List<String> getObjects(String cacheName, List<String> keys, String prefix, List<Object> objs) {
        return getCache(cacheName).getObjects(keys, prefix, objs);
    }

    public boolean removeCacheObject(String cacheName, String key) {
        return getCache(cacheName).removeCacheObject(key);
    }

}
