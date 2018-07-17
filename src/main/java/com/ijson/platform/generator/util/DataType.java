package com.ijson.platform.generator.util;

import com.google.common.collect.Lists;

import java.util.List;

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
     * @param length     length
     * @return value
     */
    public static String getDataType(String columnType, boolean isAll, int length) {
        String result = "ERROR";

        List iDate = Lists.newArrayList("TIMESTAMP", "DATE", "TIME", "DATETIME");
        List iInteger = Lists.newArrayList("INTEGER", "INT", "TINYINT");
        List iLong = Lists.newArrayList("BIGINT", "LONG");
        List iDumber = Lists.newArrayList("NUMBER");
        List iString = Lists.newArrayList("VARCHAR","VARCHAR2","LONGTEXT","TEXT");


        columnType = columnType.toUpperCase();
        if (iDate.contains(columnType)) {
            result = "java.util.Date";
        } else if (iInteger.contains(columnType)) {
            result = "java.lang.Integer";
        } else if (iLong.contains(columnType)) {
            result = "java.lang.Long";
        } else if (iDumber.contains(columnType)) {
            if (length < 20 && length > 10) {
                result = "java.lang.Double";
            } else if (length <= 10) {
                result = "java.lang.Integer";
            } else {
                result = "java.lang.Long";
            }
        } else if ("DOUBLE".equalsIgnoreCase(columnType)) {
            result = "java.lang.Double";
        } else if ("FLOAT".equalsIgnoreCase(columnType)) {
            result = "java.lang.Float";
        } else if (iString.contains(columnType)) {
            result = "java.lang.String";
        } else if ("BOOLEAN".equalsIgnoreCase(columnType)) {
            result = "java.lang.Boolean";
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

    /**
     * description: 获取数据类型
     *
     * @param columnType type
     * @param precision  precision
     * @return type
     */
    public static String getJdbcType(String columnType, int precision) {
        String colType = columnType.toUpperCase();
        if ("INT".equals(colType) || "TINYINT".equals(colType) || "SMALLINT".equals(colType)) {
            colType = "INTEGER";
        } else if ("NUMBER".equals(colType)) {
            if (precision > 20) {
                colType = "DOUBLE";
            } else {
                colType = "BIGINT";
            }
        } else if ("VARCHAR2".equals(colType) || "TEXT".equals(colType) || "CHAR".equals(colType)) {
            colType = "VARCHAR";
        } else if ("TIMESTAMP".equalsIgnoreCase(colType)) {
            colType = "TIMESTAMP";
        } else if ("DATE".equals(colType) || "TIME".equals(colType)) {
            colType = "DATETIME";
        }
        return colType;
    }
}
