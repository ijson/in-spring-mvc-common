package com.ijson.platform.cache.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;


/**
 * description:  配置资源抽象类
 * @author heppy1.com 创建时间：Jan 24, 2015  
 */
public abstract class AbstractResource implements CacheResource {

	public boolean exists() {
		return false;
	}

	public URL getURL() throws IOException {
		throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");
	}

	public String getFilename() {
		return null;
	}

	public String getDescription() {
		return null;
	}

}
