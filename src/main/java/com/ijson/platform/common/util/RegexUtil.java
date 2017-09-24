package com.ijson.platform.common.util;

/**
 * description:  正则表达式工具，包含如下功能：
 * <p>判断一个字符串是否为：double型、int型、long型<p>
 *
 * @author heppy1.com 创建时间：Jun 29, 2015
 */
public class RegexUtil {

    /**
     * description:  验证正则
     *
     * @param str      待验证的字符串
     * @param regexStr 正则表达式
     * @param isTrim   为true去掉str空格后再比较；为false不取出空格，直接比较。
     * @return boolean 为true表示参数str匹配regexStr；为false表示参数str不匹配regexStr。
     * @author cuiyongxu
     */
    private static boolean isMatches(String str, String regexStr, boolean isTrim) {
        if (null == str || null == regexStr) {
            return false;
        }
        if (isTrim) {
            str = str.trim();
        }
        return str.matches(regexStr);
    }

    /**
     * description:判断一个字符串是否为 整数
     *
     * @param str    待验证的字符串
     * @param isTrim 是否忽略首尾空格
     * @return boolean 为true表示是，为false表示否。
     * @author cuiyongxu
     */
    public static boolean isInt(String str, boolean isTrim) {
        return isMatches(str, RegexStr.Num.INT_ALL, isTrim);
    }

    /**
     * description:判断一个字符串是否为正整数
     *
     * @param str    待验证的字符串
     * @param isTrim 是否忽略首尾空格
     * @return boolean 为true表示是；为false表示否。
     * @author cuiyongxu
     */
    public static boolean isIntPlus(String str, boolean isTrim) {
        return isMatches(str, RegexStr.Num.INT_PLUS, isTrim);
    }

    /**
     * description:判断一个字符串是否为负整数
     *
     * @param str    待验证的字符串
     * @param isTrim 是否忽略首尾空格
     * @return boolean 为true表示是；为false表示否。
     * @author cuiyongxu
     */
    public static boolean isIntDec(String str, boolean isTrim) {
        return isMatches(str, RegexStr.Num.INT_DEC, isTrim);
    }

    /**
     * description:判断一个字符串是否为 长整型
     *
     * @param str    待验证的字符串
     * @param isTrim 是否忽略首尾空格
     * @return boolean 为true表示是；为false表示否。
     * @author cuiyongxu
     */
    public static boolean isLong(String str, boolean isTrim) {
        return isMatches(str, RegexStr.Num.LONG_ALL, isTrim);
    }

    /**
     * description:判断一个字符串是否为 正 长整型
     *
     * @param str    待验证的字符串
     * @param isTrim 是否忽略首尾空格
     * @return boolean 为true表示是；为false表示否。
     * @author cuiyongxu
     */
    public static boolean isLongPlus(String str, boolean isTrim) {
        return isMatches(str, RegexStr.Num.LONG_PLUS, isTrim);
    }

    /**
     * description:判断一个字符串是否为 负 长整型
     *
     * @param str    待验证的字符串
     * @param isTrim 是否忽略首尾空格
     * @return boolean 为true表示是；为false表示否。
     * @author cuiyongxu
     */
    public static boolean isLongDec(String str, boolean isTrim) {
        return isMatches(str, RegexStr.Num.LONG_DEC, isTrim);
    }

    /**
     * description:判断一个字符串是否为 double型
     *
     * @param str     待验证的字符串
     * @param isTrim  是否忽略首尾空格
     * @param acceptE 是否兼容科学记数法。true：接受；false：不接受。
     * @return boolean 为true表示是；为false表示否。
     * @author cuiyongxu
     */
    public static boolean isDouble(String str, boolean isTrim, boolean acceptE) {
        if (acceptE) {
            return isMatches(str, RegexStr.Num.DOUBLE_E_ALL, isTrim);
        } else {
            return isMatches(str, RegexStr.Num.DOUBLE_ALL, isTrim);
        }

    }

    /**
     * description:判断一个字符串是否为 正 double型
     *
     * @param str     待验证的字符串
     * @param isTrim  是否忽略首尾空格
     * @param acceptE 是否兼容科学记数法。true：接受；false：不接受。
     * @return boolean 为true表示是；为false表示否。
     * @author cuiyongxu
     */
    public static boolean isDoublePlus(String str, boolean isTrim, boolean acceptE) {
        if (acceptE) {
            return isMatches(str, RegexStr.Num.DOUBLE_E_PLUS, isTrim);
        } else {
            return isMatches(str, RegexStr.Num.DOUBLE_PLUS, isTrim);
        }

    }

    /**
     * description:判断一个字符串是否为 double型负数
     *
     * @param str     待验证的字符串
     * @param isTrim  是否忽略首尾空格
     * @param acceptE 是否兼容科学记数法。true：接受；false：不接受。
     * @return boolean 为true表示是；为false表示否。
     * @author cuiyongxu
     */
    public static boolean isDoubleDec(String str, boolean isTrim, boolean acceptE) {
        if (acceptE) {
            return isMatches(str, RegexStr.Num.DOUBLE_E_DEC, isTrim);
        } else {
            return isMatches(str, RegexStr.Num.DOUBLE_DEC, isTrim);
        }
    }

    private class RegexStr {

        /**
         * 数值的正则表达式常量
         */
        class Num {

            /**
             * 正 整数
             */
            static final String INT_PLUS = "^\\d+$";

            /**
             * 负 整数
             */
            static final String INT_DEC = "^-\\d+$";

            /**
             * (正/负)整数
             */
            static final String INT_ALL = "^-?\\d+$";

            /**
             * 正 长整形
             * <p>
             * 为了扩展防止数据越界，因此没和整型用一个
             */
            static final String LONG_PLUS = "^\\d+$";

            /**
             * 负 长整形
             * 为了扩展防止数据越界，因此没和整型用一个
             */
            static final String LONG_DEC = "^-\\d+$";

            /**
             * (正/负)长整形
             * 为了扩展防止数据越界，因此没和整型用一个
             */
            static final String LONG_ALL = "^-?\\d+$";

            /**
             * 正 浮点数
             */
            static final String DOUBLE_PLUS = "^\\d+(\\.\\d+)?$";

            /**
             * 负 浮点数
             */
            static final String DOUBLE_DEC = "^-\\d+(\\.\\d+)?$";

            /**
             * (正/负) 浮点数
             */
            static final String DOUBLE_ALL = "^-?\\d+(\\.\\d+)?$";

            /**
             * 正浮点数,兼容科学计数法
             */
            static final String DOUBLE_E_PLUS = "^\\d+(\\.\\d+((e|E)\\d+)?)?$";

            /**
             * 正浮点数,兼容科学计数法
             */
            static final String DOUBLE_E_DEC = "^-\\d+(\\.\\d+((e|E)\\d+)?)?$";

            /**
             * (正/负)浮点数,兼容科学计数法
             */
            static final String DOUBLE_E_ALL = "^-?\\d+(\\.\\d+((e|E)\\d+)?)?$";

        }

    }
}
