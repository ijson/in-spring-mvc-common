package com.ijson.platform.cache.manager.ehcache.impl;

import com.ijson.platform.common.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * description:  加载缓存资源
 *
 * @author ijson.com 创建时间：Jan 24, 2015
 */
public class LoadCacheResource extends AbstractResource {

    private String path;
    private ClassLoader classLoader;

    public LoadCacheResource() {

    }

    public LoadCacheResource(String path) {
        this.path = StringUtils.cleanPath(path);
        this.classLoader = (classLoader != null ? classLoader : getDefaultClassLoader());
    }

    public LoadCacheResource(ClassLoader classLoader, String path) {
        String pathToUse = StringUtils.cleanPath(path);
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        this.path = pathToUse;
        this.classLoader = (classLoader != null ? classLoader : getDefaultClassLoader());
    }

    private ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = LoadCacheResource.class.getClassLoader();
        }
        return cl;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = this.classLoader.getResourceAsStream(this.path);
        if (is == null) {
            throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
        }
        return is;
    }

    @Override
    public boolean exists() {
        URL url = this.classLoader.getResource(this.path);
        return (url != null);
    }

    @Override
    public URL getURL() throws IOException {
        URL url = this.classLoader.getResource(this.path);
        if (null == url) {
            url = this.classLoader.getClass().getResource("/ehcache.xml");//获取默认的配置信息
            if (url == null) {
                url = LoadCacheResource.class.getClassLoader().getResource("/ehcache/ehcache.xml");
            }
        }
        if (url == null) {
            throw new FileNotFoundException(getDescription() + " cannot be resolved to URL because it does not exist");
        }
        return url;
    }

    @Override
    public String getFilename() {
        return StringUtils.getFilename(this.path);
    }

    @Override
    public String getDescription() {
        return "class path resource [" + this.path +
                ']';
    }

    public String getPath() {
        return path;
    }

    public final ClassLoader getClassLoader() {
        return (this.classLoader != null ? this.classLoader : getDefaultClassLoader());
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
