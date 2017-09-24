package com.ijson.platform.common.exception;

public class MVCRuntimeException extends RuntimeException {
    public MVCRuntimeException() {
    }

    public MVCRuntimeException(String message) {
        super(message);
    }

    public MVCRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MVCRuntimeException(Throwable cause) {
        super(cause);
    }

    public MVCRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
