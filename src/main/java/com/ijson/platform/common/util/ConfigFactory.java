package com.ijson.platform.common.util;

public enum ConfigFactory {
    intance;

    public static String get(String key) {
        return SystemUtil.instance.getConstant(key);
    }

    public static Integer getInt(String key) {
        return Integer.parseInt(get(key));
    }



    public static Long getLong(String key) {
        return Long.parseLong(get(key));
    }
}
