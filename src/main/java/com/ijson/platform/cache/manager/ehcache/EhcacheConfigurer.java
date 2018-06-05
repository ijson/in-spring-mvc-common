package com.ijson.platform.cache.manager.ehcache;

import com.ijson.platform.cache.resource.CacheResource;
import com.ijson.platform.cache.resource.LoadCacheResource;
import com.ijson.platform.common.util.Validator;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;

/**
 * description:  ehcache资源配置类
 *
 * @author heppy1.com 创建时间：Jan 24, 2015
 */
@Slf4j
public class EhcacheConfigurer {

    private CacheResource resource;
    private static CacheManager manager = null;

    public void setLocations(CacheResource resource) {
        this.resource = resource;
    }

    private void init() {
        try {
            if (null == manager) {
                if (null == resource) {
                    resource = new LoadCacheResource("ehcache.xml");
                }
                URL url = resource.getURL();
                manager = new CacheManager(url);
            }
        } catch (Exception e) {
            log.error("init:", e);
        }
    }

    public String[] getNames() {
        init();
        return manager.getCacheNames();
    }

    /**
     * description: 获得缓存
     *
     * @param storage 存储库名称
     * @return 返回一个cache对象
     * @author cuiyongxu
     */
    public Cache getCache(String storage) {
        init();
        if (null == storage || "".equals(storage))
            storage = "ijsonCache";
        if ("defaultCache".equalsIgnoreCase(storage)) {
            if (Validator.isEmpty(manager.getCache(storage))) {
                manager.addCache("defaultCache");
            }
        }
        return manager.getCache(storage);
    }

    /**
     * description:    关闭ehcache缓存，每次调用完cache，需要关闭缓存。
     *
     * @author cuiyongxu
     */
    public void shutdownCache() {
        manager.shutdown();
    }
}
