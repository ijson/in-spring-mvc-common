package com.ijson.platform.common.util;

public enum IConfig {
    intance;

    public static String get(String key) {
        return SystemUtil.getInstance().getConstant(key);
    }

    public static Integer getInt(String key) {
        return Integer.parseInt(get(key));
    }



    public static Long getLong(String key) {
        return Long.parseLong(get(key));
    }
}
