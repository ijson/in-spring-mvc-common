package com.ijson.platform.common.exception;

/**
 * Created by cuiyongxu on 16/12/22.
 */
public class MVCBusinessException extends MVCException {
    public MVCBusinessException(MVCBusinessExceptionCode code) {
        super(code);
    }

    public MVCBusinessException(MVCBusinessExceptionCode code, String extensionMessage) {
        super(code, extensionMessage);
    }

    public MVCBusinessException(Integer code, String extensionMessage) {
        super(code, extensionMessage);
    }

}
