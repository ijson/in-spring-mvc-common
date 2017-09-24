package com.ijson.platform.cache;

import com.ijson.platform.cache.manager.ehcache.impl.EhcacheManagerImpl;
import org.junit.Test;

/**
 * Created by cuiyongxu on 17/9/19.
 */
public class CacheManagerTest {

    private CacheManager cache = new EhcacheManagerImpl();

    @Test
    public void saveOrGet() {
        cache.createCacheObject("555", "6666");
        String aa = (String) cache.getCacheCloneByKey("555");
        System.out.println(aa);
    }
}
