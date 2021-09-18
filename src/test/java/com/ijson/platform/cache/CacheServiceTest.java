package com.ijson.platform.cache;

import com.ijson.platform.cache.impl.CacheServiceImpl;
import com.ijson.platform.cache.manager.CacheManager;
import org.junit.Test;

/**
 * Created by cuiyongxu on 17/9/19.
 */
public class CacheServiceTest {

    private CacheService cache = new CacheServiceImpl();

    @Test
    public void saveOrGet() {
        CacheManager sss = cache.getCache("ijsonCache");
        sss.createCacheObject("555", "6666");
        System.out.println(sss.getCacheObjectByKey("555"));
    }
}
