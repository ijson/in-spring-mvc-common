package com.ijson.platform.common.exception;

import com.google.common.base.Strings;

public class MVCException extends MVCRuntimeException {
    private int errorCode;

    public MVCException(MVCBusinessExceptionCode code) {
        this(code.getCode(), code.getMessage());
    }

    public MVCException(MVCBusinessExceptionCode code, String extensionMessage) {
        this(code.getCode(), Strings.isNullOrEmpty(extensionMessage) ? code.getMessage() : extensionMessage);
    }

    public MVCException(int errorCode, String message) {
        this(message, false);
        this.errorCode = errorCode;
    }

    private MVCException(String message, boolean stackTrace) {
        super(message, null, true, stackTrace);
    }

    public int getErrorCode() {
        return errorCode;
    }
}
