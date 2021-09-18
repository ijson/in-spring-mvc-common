package com.ijson.platform.cache.manager.ehcache;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * description: 配置资源接口
 *
 * @author ijson.com 创建时间：Jan 24, 2015
 */
public interface CacheResource {

    InputStream getInputStream() throws IOException;

    boolean exists();

    URL getURL() throws IOException;

    String getFilename();

    String getDescription();
}
