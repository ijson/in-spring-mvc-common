package com.ijson.platform.cache;

import com.ijson.platform.cache.manager.CacheManager;

import java.util.List;

/**
 * 缓存服务  对上层业务提供的缓存
 *
 * @author ijson.com 创建时间：Jan 24, 2015
 */
public interface CacheService {

    /**
     * 创建单个缓存对象。
     *
     * @param cacheName 缓存名
     * @param key       缓存对象的key
     * @param object    传递的实体model对象
     * @return 如果key和object 都不为空则返回true,否则返回false。
     * @author cuiyongxu
     */
    boolean createCacheObject(String cacheName, String key, Object object);

    /**
     * 检查是否存在缓存
     *
     * @param cacheName 缓存名
     * @param key       缓存对象的key。
     * @return 如果存在该缓存返回true，否则返回false。
     * @author cuiyongxu
     */
    boolean checkCacheObject(String cacheName, String key);

    /**
     * 获取缓存结果集，主要应用于jdbc结果集缓存。(扩展应用)
     *
     * @param cacheName 缓存名
     * @param key       缓存对象的key
     * @return 返回数据库结果集
     * @author cuiyongxu
     */
    Object getCacheObjectByKey(String cacheName, String key);

    /**
     * 获取list对象集合
     *
     * @param cacheName 缓存名
     * @param keys      缓存对象的key的集合
     * @param prefix    前缀
     * @return 缓存对象List集合。如果keys为空，返回null
     * @author cuiyongxu
     */
    List<Object> getObjects(String cacheName, List<String> keys, String prefix);

    /**
     * 获取list对象集合
     *
     * @param cacheName 缓存名
     * @param keys      缓存对象的key的集合
     * @param prefix    前缀
     * @param objs      keys中在缓存中存在的缓存对象List集合
     * @return 返回keys中不在缓存中存在的key集合
     * @author cuiyongxu
     */
    List<String> getObjects(String cacheName, List<String> keys, String prefix, List<Object> objs);

    /**
     * 删除缓存对象
     *
     * @param cacheName 缓存名
     * @param key       缓存对象的key。
     * @return 如果key不为空返回true。否则返回false。
     * @author cuiyongxu
     */
    boolean removeCacheObject(String cacheName, String key);

    CacheManager getCache(String cacheName);

}
