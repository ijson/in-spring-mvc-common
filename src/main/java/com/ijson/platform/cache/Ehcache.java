package com.ijson.platform.cache;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.platform.common.util.Validator;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * desc:
 * author: cuiyongxu
 * create_time: 2021/9/20-12:14 AM
 **/
public final class Ehcache {

    private static final String EHCACHE_XML = "/ehcache.xml";
    private static final String DEFAULT_CACHE_NAMESPACE = "in_cache";
    private static Ehcache ehCache;
    private URL url;
    private CacheManager manager;
    public static Ehcache ice = getInstance();

    private Ehcache(String EHCACHE_XML) {
        url = getClass().getResource(EHCACHE_XML);
        manager = CacheManager.create(url);
    }


    private static synchronized Ehcache getInstance() {
        if (ehCache == null) {
            ehCache = new Ehcache(EHCACHE_XML);
        }
        return ehCache;
    }


    public boolean createCacheObject(String nameSpace, String key, Object object) {
        boolean flag = false;
        if (!Strings.isNullOrEmpty(key) && object != null) {
            Cache cache = manager.getCache(nameSpace);
            cache.remove(key);
            cache.put(new Element(key, object));
            flag = true;
        }
        return flag;
    }

    public boolean createCacheObject(String key, Object object) {
        return createCacheObject(DEFAULT_CACHE_NAMESPACE, key, object);
    }

    public boolean checkCacheObject(String nameSpace, String key) {
        boolean flag = false;
        if (!Strings.isNullOrEmpty(key)) {
            Cache cache = manager.getCache(nameSpace);
            Element element = cache.get(key);
            if (element != null) {
                flag = true;
            }
        }
        return flag;
    }

    public Object getCacheObjectByKey(String nameSpace, String key) {
        if (!Strings.isNullOrEmpty(key)) {
            Cache cache = manager.getCache(nameSpace);
            Element element = cache.get(key);
            if (element == null) {
                return null;
            } else {
                return element.getObjectValue();
            }
        } else {
            return null;
        }
    }

    public Object getCacheObjectByKey(String key) {
        return getCacheObjectByKey(DEFAULT_CACHE_NAMESPACE, key);
    }

    public Object getCacheCloneByKey(String nameSpace, String key) {
        if (!Strings.isNullOrEmpty(key)) {
            Cache cache = manager.getCache(nameSpace);
            Element element = cache.get(key);
            if (element == null) {
                return null;
            } else {
                return Validator.clone(element.getObjectValue());
            }
        } else {
            return null;
        }
    }

    public Object getCacheCloneByKey(String key) {
        return getCacheCloneByKey(DEFAULT_CACHE_NAMESPACE, key);
    }

    public List<Object> getObjects(String nameSpace, List<String> keys) {
        if (Validator.isEmpty(keys)) {
            return null;
        }
        List<Object> list = Lists.newArrayList();
        for (String key : keys) {
            Object object = getCacheCloneByKey(nameSpace, key);
            if (!Validator.isEmpty(object)) {
                list.add(object);
            }
        }
        return list;
    }

    public List<Object> getObjects(String nameSpace, List<String> keys, String prefix) {
        if (Validator.isEmpty(keys)) {
            return null;
        }
        List<Object> list = Lists.newArrayList();
        for (String key1 : keys) {
            String key = prefix + key1;
            Object object = getCacheCloneByKey(nameSpace, key);
            if (!Validator.isEmpty(object)) {
                list.add(object);
            }
        }
        return list;
    }

    public List<String> getObjects(String nameSpace, List<String> keys, String prefix, List<Object> objs) {
        if (Validator.isEmpty(keys)) {
            return null;
        }
        List<String> list = Lists.newArrayList();
        for (String key1 : keys) {
            String key = prefix + key1;
            Object object = getCacheCloneByKey(nameSpace, key);
            if (!Validator.isEmpty(object)) {
                objs.add(object);
            } else {
                list.add(key1);
            }
        }
        return list;
    }

    public List getAllKeys(String nameSpace) {
        Cache cache = manager.getCache(nameSpace);
        return cache.getKeys();
    }

    public boolean removeCacheObject(String nameSpace, String key) {
        boolean flag = false;
        if (Validator.isNotNull(key)) {
            Cache cache = manager.getCache(nameSpace);
            cache.remove(key);
            flag = true;
        }
        return flag;
    }

    public boolean removeCacheObject(String key) {
        return removeCacheObject(DEFAULT_CACHE_NAMESPACE, key);
    }

    public boolean updateCacheObject(String nameSpace, String key, Object value) {
        return createCacheObject(nameSpace, key, value);
    }

    public void shutdownCache(String nameSpace) {
        if (Objects.nonNull(manager)) {
            manager.shutdown();
        }
    }

    public void removeAll(String nameSpace) {
        Cache cache = manager.getCache(nameSpace);
        cache.removeAll();
    }
}
