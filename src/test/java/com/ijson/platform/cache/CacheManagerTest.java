package com.ijson.platform.cache;

import com.google.common.collect.Lists;

import com.ijson.platform.cache.impl.LoadCacheFactory;
import com.ijson.platform.common.annotation.JunitError;
import com.ijson.platform.common.util.JacksonUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by cuiyongxu on 17/9/19.
 */
@Slf4j
public class CacheManagerTest {


    @Before
    public void setup() {
        LoadCacheFactory.instance.getCacheManager().createCacheObject("main.url", "http://www.ijson.com");

    }

    @Test
    public void getCacheCloneStringByKey() {
        log.info(LoadCacheFactory.instance
                .getCacheManager("").getCacheCloneStringByKey("main.url"));
    }

    @Test
    public void checkCacheObject() {
        boolean flag = LoadCacheFactory.instance
                .getCacheManager("").checkCacheObject("main.url");
        log.info("key 当前:{}", flag ? "存在" : "不存在");
    }


    @Test
    public void getCacheObjectByKey() {
        log.info((String) LoadCacheFactory.instance
                .getCacheManager("").getCacheObjectByKey("main.url"));
    }

    @Test
    public void getCacheCloneByKey() {
        log.info((String) LoadCacheFactory.instance
                .getCacheManager("").getCacheCloneByKey("main.url"));
    }

    @Test
    public void getObjects() {
        List<String> value = LoadCacheFactory.instance
                .getCacheManager("").getObjects(Lists.newArrayList("main.url"));
        log.info("value:{}", JacksonUtil.toJson(value));
    }


    @Test
    public void getObjectsByPrefix() {
        List<String> value = LoadCacheFactory.instance
                .getCacheManager("").getObjects(Lists.newArrayList("url"), "main.");
        log.info("value:{}", JacksonUtil.toJson(value));
    }


    @JunitError()
    @Test
    public void getObjectsByPrefixOrListObjs() {
        List<String> value = LoadCacheFactory.instance
                .getCacheManager("").getObjects(Lists.newArrayList("url"), "main.", Lists.newArrayList("main.dept"));
        log.info("value:{}", JacksonUtil.toJson(value));
    }


    @Test
    public void getAllKeys() {
        List<String> value = LoadCacheFactory.instance
                .getCacheManager("").getAllKeys();
        log.info("value:{}", JacksonUtil.toJson(value));
    }


    @Test
    public void removeCacheObject() {
        boolean flag = LoadCacheFactory.instance
                .getCacheManager("").removeCacheObject("main.url");
        log.info("value:{}", flag);
    }


    @Test
    public void updateCacheObject() {
        boolean flag = LoadCacheFactory.instance
                .getCacheManager("").updateCacheObject("main.url", "http://git.ijson.com");
        log.info("flag:{},value:{}", flag, LoadCacheFactory.instance
                .getCacheManager("").getCacheCloneStringByKey("main.url"));
    }


}
