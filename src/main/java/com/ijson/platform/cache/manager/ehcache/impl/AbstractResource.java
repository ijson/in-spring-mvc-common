package com.ijson.platform.cache.manager.ehcache.impl;

import com.ijson.platform.cache.manager.ehcache.CacheResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;


/**
 * description:  配置资源抽象类
 *
 * @author heppy1.com 创建时间：Jan 24, 2015
 */
public abstract class AbstractResource implements CacheResource {

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public URL getURL() throws IOException {
        throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");
    }

    @Override
    public String getFilename() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

}
