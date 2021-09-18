package com.ijson.platform.common.exception;

/**
 * desc:
 * author: cuiyongxu
 * create_time: 2021/9/15-11:46 PM
 **/
public class CacheException extends MVCBusinessException{
    public CacheException(Integer code, String extensionMessage) {
        super(code, extensionMessage);
    }
}
