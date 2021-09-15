package com.ijson.platform.common.exception;

/**
 * desc:
 * author: cuiyongxu
 * create_time: 2021/9/15-11:46 PM
 **/
public class GeneratorException extends MVCBusinessException{
    public GeneratorException(Integer code, String extensionMessage) {
        super(code, extensionMessage);
    }
}
