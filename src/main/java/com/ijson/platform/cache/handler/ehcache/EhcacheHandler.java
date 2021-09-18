package com.ijson.platform.cache.handler.ehcache;

import com.google.common.collect.Lists;
import com.ijson.platform.cache.handler.CacheHandler;
import com.ijson.platform.common.exception.CacheException;
import com.ijson.platform.common.util.Validator;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * description:  ehcache缓存实现类
 *
 * @author ijson.com 创建时间：Jan 24, 2015
 */
@Component
public class EhcacheHandler implements CacheHandler {

    //读取ehcache.xml映射文件
    private Cache cache;

    private volatile EhcacheConfigurer cacheConfig;//做spring注入时可以注入
    private String cacheName = "ijson-cache";//做spring注入时需要注入

    @PostConstruct
    public void init() {
        cacheConfig = new EhcacheConfigurer();
    }

    @Override
    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
        cache = cacheConfig.getCache(this.cacheName);
    }


    private Cache getCache() {
        if (null == cache) {
            cache = cacheConfig.getCache(this.cacheName);
        }

        if (cache == null) {
            throw new CacheException(-1, "缓存初始化失败,请检查ehcache.xml是否已配置:" + cacheName);
        }
        return cache;
    }

    @Override
    public boolean createCacheObject(String key, Object object) {
        boolean flag = false;
        if (Validator.isNotNull(key) && object != null) {
            getCache().remove(key);
            Element element = new Element(key, object);
            getCache().put(element);
            flag = true;
        }
        return flag;
    }

    @Override
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

    @Override
    public Object getCacheObjectByKey(String key) {
        if (Validator.isNotNull(key)) {
            Element element = getCache().get(key);
            if (element == null) {
                return null;
            } else {
                return element.getObjectValue();
            }
        } else {
            return null;
        }
    }

    @Override
    public Object getCacheCloneByKey(String key) {
        if (Validator.isNotNull(key)) {
            Element element = getCache().get(key);
            if (element == null) {
                return null;
            } else {
                return Validator.clone(element.getObjectValue());
            }
        } else {
            return null;
        }
    }

    @Override
    public List<Object> getObjects(List<String> keys) {
        if (Validator.isEmpty(keys)) {
            return null;
        }
        List<Object> list = Lists.newArrayList();
        for (String key : keys) {
            Object object = getCacheCloneByKey(key);
            if (!Validator.isEmpty(object)) {
                list.add(object);
            }
        }
        return list;
    }

    @Override
    public List<Object> getObjects(List<String> keys, String prefix) {
        if (Validator.isEmpty(keys)) {
            return null;
        }
        List<Object> list = Lists.newArrayList();
        for (String key1 : keys) {
            String key = prefix + key1;
            Object object = getCacheCloneByKey(key);
            if (!Validator.isEmpty(object)) {
                list.add(object);
            }
        }
        return list;
    }

    @Override
    public List<String> getObjects(List<String> keys, String prefix, List<Object> objs) {
        if (Validator.isEmpty(keys)) {
            return null;
        }
        List<String> list = Lists.newArrayList();
        for (String key1 : keys) {
            String key = prefix + key1;
            Object object = getCacheCloneByKey(key);
            if (!Validator.isEmpty(object)) {
                objs.add(object);
            } else {
                list.add(key1);
            }
        }
        return list;
    }

    @Override
    public List getAllKeys() {
        return getCache().getKeys();
    }

    @Override
    public boolean removeCacheObject(String key) {
        boolean flag = false;
        if (Validator.isNotNull(key)) {
            getCache().remove(key);
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean updateCacheObject(String key, Object value) {
        return createCacheObject(key, value);
    }

    @Override
    public void shutdownCache() {
        cacheConfig.shutdownCache();
    }

    @Override
    public void removeAll() {
        getCache().removeAll();
    }

    @Override
    public String cacheType() {
        return "ehcache";
    }


}
