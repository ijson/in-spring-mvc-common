package com.ijson.platform.common.exception;

/**
 * Created by cuiyongxu on 17/3/4.
 */
public class ManagerBusinessException extends MVCBusinessException {

    public ManagerBusinessException(String extensionMessage) {
        super(MVCBusinessExceptionCode.MANAGER_BUSSINESS_EXCEPTION, extensionMessage);
    }
}
