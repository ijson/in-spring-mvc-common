package com.ijson.platform.cache.handler.ehcache;

import com.google.common.collect.Lists;
import com.ijson.platform.cache.handler.CacheHandler;
import com.ijson.platform.common.util.Validator;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.List;


/**
 * description:  ehcache缓存实现类
 *
 * @author ijson.com 创建时间：Jan 24, 2015
 */
@Slf4j
@Component
public class EhcacheHandler implements CacheHandler {

    private static final String EHCACHE_XML = "/ehcache.xml";

    private URL url;

    private CacheManager manager;


    //读取ehcache.xml映射文件

    private volatile EhcacheConfigurer cacheConfig;//做spring注入时可以注入
    private String cacheName = "ijson-cache";//做spring注入时需要注入

    @PostConstruct
    public void init() {
        url = getClass().getResource(EHCACHE_XML);
        manager = CacheManager.create(url);
    }

    @Override
    public synchronized CacheHandler setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }


    @Override
    public boolean createCacheObject(String key, Object object) {
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, object);
        cache.put(element);
        return true;
    }

    @Override
    public boolean checkCacheObject(String key) {
        boolean flag = false;
//        if (Validator.isNotNull(key)) {
//            Element element = getCache().get(key);
//            if (element != null) {
//                flag = true;
//            }
//        }
        return flag;
    }

    @Override
    public Object getCacheObjectByKey(String key) {
//        if (Validator.isNotNull(key)) {
//            Element element = getCache().get(key);
//            if (element == null) {
//                return null;
//            } else {
//                return element.getObjectValue();
//            }
//        } else {
        return null;
//        }
    }

    @Override
    public Object getCacheCloneByKey(String key) {
        Cache cache = manager.getCache(cacheName);

        log.info("{},{}", cacheName, key);
        return cache.get(key);
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
        return null;
    }

    @Override
    public boolean removeCacheObject(String key) {
        boolean flag = false;
//        if (Validator.isNotNull(key)) {
//            getCache().remove(key);
//            flag = true;
//        }
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
//        getCache().removeAll();
    }

    @Override
    public String cacheType() {
        return "ehcache";
    }


}
