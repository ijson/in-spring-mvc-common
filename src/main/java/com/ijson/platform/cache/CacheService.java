package com.ijson.platform.cache;

import java.util.List;

/**
 * description:缓存服务
 *
 * @author heppy1.com 创建时间：Jan 24, 2015
 */
public interface CacheService {

    /**
     * description: 创建单个缓存对象。
     *
     * @param key    缓存对象的key
     * @param object 传递的实体model对象
     * @return 如果key和object 都不为空则返回true,否则返回false。
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    boolean createCacheObject(String cacheName, String key, Object object);

    /**
     * description: 检查是否存在缓存
     *
     * @param key 缓存对象的key。
     * @return 如果存在该缓存返回true，否则返回false。
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    boolean checkCacheObject(String cacheName, String key);

    /**
     * description: 获取缓存结果集，主要应用于jdbc结果集缓存。(扩展应用)
     *
     * @param key 缓存对象的key
     * @return 返回数据库结果集
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    Object getCacheObjectByKey(String cacheName, String key);

    /**
     * description: 获取list对象集合
     *
     * @param keys   缓存对象的key的集合
     * @param prefix 前缀
     * @return 缓存对象List集合。如果keys为空，返回null
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    List<Object> getObjects(String cacheName, List<String> keys, String prefix);

    /**
     * description: 获取list对象集合
     *
     * @param keys   缓存对象的key的集合
     * @param prefix 前缀
     * @param objs   keys中在缓存中存在的缓存对象List集合
     * @return 返回keys中不在缓存中存在的key集合
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    List<String> getObjects(String cacheName, List<String> keys, String prefix, List<Object> objs);

    /**
     * description: 删除缓存对象
     *
     * @param key 缓存对象的key。
     * @return 如果key不为空返回true。否则返回false。
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    boolean removeCacheObject(String cacheName, String key);

    CacheManager getCache(String cacheName);

}
