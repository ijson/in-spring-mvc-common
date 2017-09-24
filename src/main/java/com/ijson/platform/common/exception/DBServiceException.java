package com.ijson.platform.common.exception;

/**
 * Created by cuiyongxu on 17/3/4.
 */
public class DBServiceException extends MVCBusinessException {

    public DBServiceException(String extensionMessage) {
        super(MVCBusinessExceptionCode.DB_SERVICE_EXCEPTION, extensionMessage);
    }
}
