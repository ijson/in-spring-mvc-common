package com.ijson.platform.cache;

import com.ijson.platform.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cuiyongxu on 17/9/19.
 */
public class CacheManagerTest extends BaseTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void saveOrGet() {
        cacheManager.cacheHandler().createCacheObject("555", "6666");
        String aa = (String) cacheManager.cacheHandler().getCacheCloneByKey("555");
        System.out.println(aa);
    }

    @Test
    public void saveOrGet2() {
        cacheManager.cacheHandler("example").createCacheObject("ccc", "6666");
        String aa = (String) cacheManager.cacheHandler("example").getCacheCloneByKey("ccc");
        System.out.println(aa);
    }
}


