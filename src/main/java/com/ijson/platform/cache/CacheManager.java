package com.ijson.platform.cache;

import java.util.List;

/**
 * description:  系统缓存操作接口
 *
 * @author ijson.com 创建时间：Jan 24, 2015
 */
public interface CacheManager extends IBaseCache {
    /**
     * description: 创建单个缓存对象。
     *
     * @param key    缓存对象的key
     * @param object 传递的实体model对象
     * @return 如果key和object 都不为空则返回true,否则返回false。
     * @author cuiyongxu
     */
    boolean createCacheObject(String key, Object object);

    /**
     * description: 检查是否存在缓存
     *
     * @param key 缓存对象的key。
     * @return 如果存在该缓存返回true，否则返回false。
     * @author cuiyongxu
     */
    boolean checkCacheObject(String key);

    /**
     * description: 获取缓存结果集，主要应用于jdbc结果集缓存。(扩展应用)
     *
     * @param key 缓存对象的key
     * @return 返回数据库结果集
     * @author cuiyongxu
     */
    Object getCacheObjectByKey(String key);

    /**
     * description: 获取缓存结果集，主要应用于jdbc结果集缓存。(扩展应用)
     *
     * @param key 缓存对象的key
     * @return 返回数据库结果集
     * @author cuiyongxu
     */
    Object getCacheCloneByKey(String key);

    /**
     * description: 获取list对象集合
     *
     * @param keys 缓存对象的key的集合
     * @return 缓存对象List集合。如果keys为空，返回null
     * @author cuiyongxu
     */
    List<Object> getObjects(List<String> keys);

    /**
     * description: 获取list对象集合
     *
     * @param keys   缓存对象的key的集合
     * @param prefix 前缀
     * @return 缓存对象List集合。如果keys为空，返回null
     * @author cuiyongxu
     */
    List<Object> getObjects(List<String> keys, String prefix);

    /**
     * description: 获取list对象集合
     *
     * @param keys   缓存对象的key的集合
     * @param prefix 前缀
     * @param objs   keys中在缓存中存在的缓存对象List集合
     * @return 返回keys中不在缓存中存在的key集合
     * @author cuiyongxu
     */
    List<String> getObjects(List<String> keys, String prefix, List<Object> objs);

    /**
     * description: 得到此域中所有缓存的key
     *
     * @return 返回缓存中所有KEY集合
     * @author cuiyongxu
     */
    List getAllKeys();

    /**
     * description: 删除缓存对象
     *
     * @param key 缓存对象的key。
     * @return 如果key不为空返回true。否则返回false。
     * @author cuiyongxu
     */
    boolean removeCacheObject(String key);

    /**
     * description: 更新缓存对象
     *
     * @param key   缓存对象的key。
     * @param value 需要更新的缓存对象
     * @return 如果key和object 都不为空则返回true,否则返回false。
     * @author cuiyongxu
     */
    boolean updateCacheObject(String key, Object value);

    /**
     * description:    关闭ehcache缓存，每次调用完cache，需要关闭缓存。
     *
     * @author cuiyongxu
     */
    void shutdownCache();

    /**
     * description:  清空空间内所有缓存
     *
     * @author cuiyongxu
     */
    void removeAll();
}
