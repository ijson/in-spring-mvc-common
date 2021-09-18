package com.ijson.platform.cache;

import com.google.common.base.Strings;
import com.ijson.platform.cache.handler.CacheHandler;
import lombok.Data;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * desc:
 * author: cuiyongxu
 * create_time: 2021/9/18-4:32 PM
 **/
@Data
@Component
public class CacheManager extends ApplicationObjectSupport {

    private String cacheType = "ehcache";

    private Map<String, CacheHandler> handlers;

    public CacheHandler cache;

    @PostConstruct
    public void init() {
        Map<String, CacheHandler> taskCompleteHandler = getApplicationContext().getBeansOfType(CacheHandler.class);
        handlers = taskCompleteHandler.values().stream().collect(Collectors.toMap(CacheHandler::cacheType, item -> item));
        cache = handlers.get(cacheType);
    }


    public CacheHandler cacheHandler(String cacheName) {
        switch (cacheType) {
            case "ehcache":
                getEhcacheManager(cacheName);
                break;
        }
        return cache;
    }

    public CacheHandler cacheHandler() {
        switch (cacheType) {
            case "ehcache":
                getEhcacheManager("");
                break;
        }
        return cache;
    }


    private void getEhcacheManager(String cacheName) {
        if (Strings.isNullOrEmpty((cacheName))) {
            cacheName = "in_cache";
        }
        cache.setCacheName(cacheName);
    }


}
