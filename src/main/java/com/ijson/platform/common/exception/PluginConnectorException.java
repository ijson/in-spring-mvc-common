package com.ijson.platform.common.exception;

/**
 * Created by cuiyongxu on 17/3/4.
 */
public class PluginConnectorException extends MVCBusinessException {

    public PluginConnectorException(String extensionMessage) {
        super(MVCBusinessExceptionCode.PLUGIN_CONNECTOR_EXCEPTION, extensionMessage);
    }
}
