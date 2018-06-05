package com.ijson.platform.cache;

import com.ijson.platform.cache.impl.LoadCacheFactory;

import org.junit.Test;

/**
 * Created by cuiyongxu on 17/9/19.
 */
public class CacheManagerTest {

    private CacheManager<String> cacheManager = LoadCacheFactory.instance.getCacheManager("ijsonCache");

    @Test
    public void saveOrGet() {
        cacheManager.createCacheObject("555", "6666");
        String aa = cacheManager.getCacheCloneByKey("555");
        System.out.println(aa);
    }
}
