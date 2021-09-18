package com.ijson.platform.cache.handler.memcached;

import com.ijson.platform.cache.handler.CacheHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * desc:
 * author: cuiyongxu
 * create_time: 2021/9/18-4:36 PM
 **/
@Component
public class MemcachedHandler implements CacheHandler {
    @Override
    public void setCacheName(String cacheName) {

    }

    @Override
    public boolean createCacheObject(String key, Object object) {
        return false;
    }

    @Override
    public boolean checkCacheObject(String key) {
        return false;
    }

    @Override
    public Object getCacheObjectByKey(String key) {
        return null;
    }

    @Override
    public Object getCacheCloneByKey(String key) {
        return null;
    }

    @Override
    public List<Object> getObjects(List<String> keys) {
        return null;
    }

    @Override
    public List<Object> getObjects(List<String> keys, String prefix) {
        return null;
    }

    @Override
    public List<String> getObjects(List<String> keys, String prefix, List<Object> objs) {
        return null;
    }

    @Override
    public List getAllKeys() {
        return null;
    }

    @Override
    public boolean removeCacheObject(String key) {
        return false;
    }

    @Override
    public boolean updateCacheObject(String key, Object value) {
        return false;
    }

    @Override
    public void shutdownCache() {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public String cacheType() {
        return "memcached";
    }
}
