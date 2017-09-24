package com.ijson.platform.common.util;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ToolsUtil {

    /**
     * 判断字符串是否以“/”结束，如果不是为其加上
     *
     * @param text 待处理的字符串
     */
    public static String endsWith(String text) {
        if (Validator.isNotNull(text)) {
            if (!text.endsWith(File.separator) && !text.endsWith("/"))
                text += "/";
        }
        return text;
    }

    /**
     * 判断字符串是否以“/”结束，如果不是为其加上
     *
     * @param text 待处理的字符串
     */
    public static boolean isendWith(String text) {
        boolean mark = true;
        if (Validator.isNotNull(text)) {
            if (!text.endsWith(File.separator) && !text.endsWith("/"))
                mark = false;
        }
        return mark;
    }

    /**
     * 获取指定格式的时间串
     *
     * @param timer            时间LONG值
     * @param simpleDateFormat 格式化串
     * @return 返回格式后的时间串
     */
    public static String getTimeUrl(Long timer, String simpleDateFormat) {
        SimpleDateFormat mydate = new SimpleDateFormat(simpleDateFormat);
        if (timer > 0) {
            Calendar calendar = Calendar.getInstance();
            int offset = calendar.getTimeZone().getRawOffset();
            Date date = new Date(timer + offset);
            return mydate.format(date);
        }
        return "";
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param key   基础串
     * @param value 待比串
     * @return true为真，false为假
     */
    public static boolean StringEquals(String key, String value) {
        boolean result = false;
        if (Validator.isNotNull(key) && Validator.isNotNull(value)) {
            result = key.trim().equalsIgnoreCase(value.trim());
        }
        return result;
    }

    /**
     * 是否有指定的字符串
     *
     * @param keystr 验证标准
     * @param text   要检查的字符串
     * @return 返回true表示含有，false表示不含有
     */
    public static boolean getStringtrue(String keystr, String text) {
        if (Validator.isNull(keystr) || Validator.isNull(text)) {
            return false;
        }
        int count = text.toUpperCase().indexOf(keystr.toUpperCase());
        return count != -1;
    }

    public static boolean isstartWith(String text) {
        boolean mark = true;
        if (Validator.isNotNull(text)) {
            if (!text.startsWith(File.separator) && !text.startsWith("/"))
                mark = false;
        }
        return mark;
    }

    /**
     * 格式字任串
     *
     * @param text   待格字任串
     * @param regx   规则
     * @param target 替换内容
     */
    public static String formatString(String text, String regx, String target) {
        if (Validator.isNull(text) || Validator.isNull(regx))
            return null;
        return text = text.replaceAll(regx, target);
    }

    /**
     * 首字母大写
     *
     */
    public static String toUpperFirst(String str) {
        String names[] = str.split("_");
        StringBuilder sb = new StringBuilder();
        for (String name : names) {
            sb.append(name.substring(0, 1).toUpperCase()).append(name.substring(1));
        }
        return sb.toString();
    }

    /**
     * 属性值生成,符合骆驼命名
     *
     */
    public static String toCamelNamed(String str) {
        String names[] = str.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            if (i == 0) {
                sb.append(names[i].toLowerCase());
            } else {
                sb.append(names[i].substring(0, 1).toUpperCase()).append(names[i].substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }
}
