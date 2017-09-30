package com.ijson.platform.common.exception;

/**
 * mvc 业务异常码 <p> Created by cuiyongxu on 16/8/23.
 */
public enum MVCBusinessExceptionCode {

    /*
     * 提示
     */
    CONFIG_NAME_DUPLICATE(101_01_0001, "配置名称重复"),

    /**
     * 警告
     */

    PARAMETER_ERROR(201_01_0001, "参数异常"),

    /***********************************************
     * 错误
     ***********************************************/
    SERVICE_EXCEPTION(301_01_0001, "服务异常"),

    /**
     * 业务异常
     */
    PLUGIN_CONNECTOR_EXCEPTION(201_01_0001, "插件执行异常"),
    DB_SERVICE_EXCEPTION(201_01_0002, "数据服务执行异常"),
    MANAGER_BUSSINESS_EXCEPTION(201_01_0003, "业务服务执行失败");

    private int code;
    private String message;

    MVCBusinessExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static MVCBusinessExceptionCode valueOf(int errorCode) {
        MVCBusinessExceptionCode[] codes = MVCBusinessExceptionCode.values();
        for (MVCBusinessExceptionCode code : codes) {
            if (code.getCode() == errorCode) {
                return code;
            }
        }

        return null;
    }

}
