package com.ijson.platform.cache;

import com.ijson.platform.BaseTest;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cuiyongxu on 17/9/19.
 */
public class CacheManagerTest extends BaseTest {


    @Test
    public void saveOrGet() {
        Ehcache.ice.createCacheObject("555", "6666");
        String aa = (String) Ehcache.ice.getCacheCloneByKey("555");
        System.out.println(aa);
    }

    @Test
    public void saveOrGet2() {
        Ehcache.ice.createCacheObject("example", "ccc", "6666");
        String aa = (String) Ehcache.ice.getCacheCloneByKey("example", "ccc");
        System.out.println(aa);
    }


    private ExecutorService rst = Executors.newFixedThreadPool(10);


    @Test
    public void saveInfoThread() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            rst.execute(() -> {

                String aaaKey = "aaa" + UUID.randomUUID();
                Ehcache.ice.createCacheObject("aaa", aaaKey, aaaKey);
                System.out.println(Ehcache.ice.getCacheCloneByKey("aaa", aaaKey));

                System.out.println("==========");

                String bbbKey = "bbb" + UUID.randomUUID();
                Ehcache.ice.createCacheObject("bbb", bbbKey, bbbKey);
                System.out.println(Ehcache.ice.getCacheCloneByKey("bbb", bbbKey));

                String default_ = "default_" + UUID.randomUUID();
                Ehcache.ice.createCacheObject(default_, default_);
                System.out.println(Ehcache.ice.getCacheCloneByKey(default_));
            });
        }
        Thread.sleep(10 * 1000);
    }

}


