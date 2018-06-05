package com.ijson.platform.cache.manager.ehcache.impl;

import com.google.common.collect.Lists;

import com.ijson.platform.cache.CacheManager;
import com.ijson.platform.cache.manager.ehcache.EhcacheConfigurer;
import com.ijson.platform.common.util.Validator;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import java.util.List;

import lombok.extern.slf4j.Slf4j;


/**
 * description:  ehcache缓存实现类
 *
 * @author heppy1.com 创建时间：Jan 24, 2015
 */
@Slf4j
public class EhcacheManagerImpl<T> implements CacheManager<T> {


    //读取ehcache.xml映射文件
    private Cache cache;
    private EhcacheConfigurer cacheConfig;//做spring注入时可以注入
    private String cacheName;//做spring注入时需要注入

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
        cache = getCacheConfig().getCache(this.cacheName);
    }


    public EhcacheManagerImpl(String cacheName) {
        this.cacheName = cacheName;
        cache = getCacheConfig().getCache(this.cacheName);
    }

    private Cache getCache() {
        if (null == cache) {
            cache = getCacheConfig().getCache(this.cacheName);
        }

        return cache;
    }

    public boolean createCacheObject(String key, T object) {
        boolean flag = false;
        if (Validator.isNotNull(key) && object != null) {
            Cache cache = getCache();
            if (cache == null) {
                cache = getDefaultCache();
            }
            cache.remove(key);
            Element element = new Element(key, object);
            cache.put(element);
            flag = true;
        }
        return flag;
    }

    private Cache getDefaultCache() {
        return cache = getCacheConfig().getCache("ijsonCache");
    }

    public boolean checkCacheObject(String key) {
        boolean flag = false;
        if (Validator.isNotNull(key)) {
            Element element = getCache().get(key);
            if (element != null) {
                flag = true;
            }
        }
        return flag;
    }

    public T getCacheObjectByKey(String key) {
        if (Validator.isNotNull(key)) {
            Element element = getCache().get(key);
            if (element == null) {
                return null;
            } else {
                return (T) element.getObjectValue();
            }
        } else {
            return null;
        }
    }

    public T getCacheCloneByKey(String key) {
        if (Validator.isNotNull(key)) {
            Element element = getCache().get(key);
            if (element == null) {
                return null;
            } else {
                return (T) Validator.clone(element.getObjectValue());
            }
        } else
            return null;
    }

    public List<T> getObjects(List<String> keys) {
        if (Validator.isEmpty(keys))
            return null;
        List<T> list = Lists.newArrayList();
        for (String key : keys) {
            T object = getCacheCloneByKey(key);
            if (!Validator.isEmpty(object)) {
                list.add(object);
            }
        }
        return list;
    }

    public List<T> getObjects(List<String> keys, String prefix) {
        if (Validator.isEmpty(keys))
            return null;
        List<T> list = Lists.newArrayList();
        for (String key1 : keys) {
            String key = prefix + key1;
            T object = getCacheCloneByKey(key);
            if (!Validator.isEmpty(object)) {
                list.add(object);
            }
        }
        return list;
    }

    public List<String> getObjects(List<String> keys, String prefix, List<T> objs) {
        if (Validator.isEmpty(keys))
            return null;
        List<String> list = Lists.newArrayList();
        for (String key1 : keys) {
            String key = prefix + key1;
            T object = getCacheCloneByKey(key);
            if (!Validator.isEmpty(object)) {
                objs.add(object);
            } else {
                list.add(key1);
            }
        }
        return list;
    }

    public List getAllKeys() {
        return getCache().getKeys();
    }

    public boolean removeCacheObject(String key) {
        boolean flag = false;
        if (Validator.isNotNull(key)) {
            getCache().remove(key);
            flag = true;
        }
        return flag;
    }

    public boolean updateCacheObject(String key, T value) {
        return createCacheObject(key, value);
    }

    public void shutdownCache() {
        cacheConfig.shutdownCache();
    }

    public void removeAll() {
        getCache().removeAll();
    }

    private EhcacheConfigurer getCacheConfig() {
        if (null == cacheConfig) {
            cacheConfig = new EhcacheConfigurer();
        }
        return cacheConfig;
    }

    public void setCacheConfig(EhcacheConfigurer cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

}
