package com.ijson.platform.generator.util;

public class DataType {

    public static final String INTEGER = "Integer";
    public static final String LONG = "Long";
    public static final String SHORT = "Short";
    public static final String STRING = "String";
    public static final String DOUBLE = "Double";
    public static final String FLOAT = "Float";
    public static final String BYTEArray = "Byte[]";
    public static final String DATE = "Date";
    public static final String BYTE = "Byte";

    /**
     * 获取字段的类型
     *
     * @param columnType columnType
     * @param isAll      isAll
     * @return value
     * @param length  length
     */
    public static String getDataType(String columnType, boolean isAll, int length) {
        String result = columnType;
        if ("DATE".equalsIgnoreCase(columnType) || "TIMESTAMP".equalsIgnoreCase(columnType)
                || "TIME".equalsIgnoreCase(columnType) || "DATETIME".equalsIgnoreCase(columnType)) {
            result = "java.util.Date";
        } else if ("INTEGER".equalsIgnoreCase(columnType) || "INT".equalsIgnoreCase(columnType)) {
            result = "java.lang.Integer";
        } else if ("BIGINT".equalsIgnoreCase(columnType)) {
            result = "java.lang.Long";
        } else if ("NUMBER".equalsIgnoreCase(columnType)) {
            if (length < 20 && length > 10)
                result = "java.lang.Double";
            else if (length <= 10)
                result = "java.lang.Integer";
            else
                result = "java.lang.Long";
        } else if ("DOUBLE".equalsIgnoreCase(columnType)) {
            result = "java.lang.Double";
        } else if ("FLOAT".equalsIgnoreCase(columnType)) {
            result = "java.lang.Float";
        } else {
            result = "java.lang.String";
        }
        if (!isAll) {
            result = result.replaceAll("java.lang.", "");
            result = result.replaceAll("java.util.", "");
        }
        return result;
    }

    public static boolean isStringType(String columnType) {
        boolean mark = false;
        if ("DATE".equalsIgnoreCase(columnType) || "TIMESTAMP".equalsIgnoreCase(columnType)
                || "TIME".equalsIgnoreCase(columnType) || "DATETIME".equalsIgnoreCase(columnType)
                || "INTEGER".equalsIgnoreCase(columnType) || "INT".equalsIgnoreCase(columnType)
                || "BIGINT".equalsIgnoreCase(columnType) || "NUMBER".equalsIgnoreCase(columnType)
                || "DOUBLE".equalsIgnoreCase(columnType) || "FLOAT".equalsIgnoreCase(columnType)) {
        } else {
            mark = true;
        }
        return mark;
    }
}
