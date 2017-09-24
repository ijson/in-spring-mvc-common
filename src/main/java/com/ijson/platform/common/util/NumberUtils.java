package com.ijson.platform.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * description:  提供高精度的运算支持.所以函数以double为参数类型，兼容int与float.
 *
 * @author heppy1.com 创建时间：Jun 29, 2015
 */
public class NumberUtils {

    private static final String FMT_INT_PART = "################################0";

    private static final String FMT_INT_PART_SOUTHAND = "###,###,###,###,###,###,###,###,###,##0";

    private static final String FMT_DECIMAL_PART_NOZERO = "##########";// 小数部分，非必须 十位

    private static final String FMT_DECIMAL_PART_ZERO = "0000000000";// 小数部分，十位

    /**
     * description:
     * 四舍五入double
     *
     * @param d          操作数
     * @param scale      要保留的小数位数
     * @param roundModle 舍取模式：
     * @return double    把参数d根据roundModle的取舍模式四舍五入，并保留scale位小数
     * @author cuiyongxu
     */
    private static double round(double d, int scale, int roundModle) {
        if (scale < 0) {
            // 如果小数位数不合法，则小数部分取0，即舍去小数部分
            scale = 0;
            // throw new IllegalArgumentException( "The scale must be a positive
            // integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(d));
        return b.divide(new BigDecimal("1"), scale, roundModle).doubleValue();
    }

    /**
     * 格式化一个double数值
     *
     * @param d      待格式化的数
     * @param fmtStr 格式化模板
     * @return String 把参数d按照参数fmtStr进行格式化
     * @author cuiyongxu
     */
    private static String formatDouble(double d, String fmtStr) {
        DecimalFormat df = new DecimalFormat(fmtStr);
        return df.format(d);
    }

    /**
     * 格式化double
     *
     * @param d             要格式化的数
     * @param scale         保留小数位数
     * @param roundModle    入模式
     * @param thousandSplit 是否使用千分位分隔符，true：使用，false：不使用
     * @param decMust       小数位数不足时，是否自动补齐。
     *                      <p>如21.2显示两位小数时，decMust为true：显示为21.20 ，decMust为false：显示为21.2
     * @return String   按照roundModle模式格式化参数d;
     * 保留scale位小数;
     * 根据thousandSplit可以定义是否使用千分位分隔符;
     * 根据decMust可以定义小数位数不足时，是否自动补齐。
     * @author cuiyongxu
     */
    private static String formatDouble(double d, int scale, int roundModle, boolean thousandSplit, boolean decMust) {
        if (scale < 0) {
            scale = 0;
        }
        String decimalPartStr = "";
        if (scale > 0) {
            decimalPartStr = decMust ? FMT_DECIMAL_PART_ZERO : FMT_DECIMAL_PART_NOZERO;
            if (scale > decimalPartStr.length()) {
                scale = decimalPartStr.length();
            }
            decimalPartStr = "." + decimalPartStr.substring(0, scale);
        }

        double d2 = round(d, scale, roundModle);
        String intPartStr = thousandSplit ? FMT_INT_PART_SOUTHAND : FMT_INT_PART;
        return formatDouble(d2, intPartStr + decimalPartStr);
    }

    /**
     * 四舍五入 double
     *
     * @param d     要格式化的数
     * @param scale 保留小数位数
     * @return double 对double类型的参数d进行四舍五入，并保留scale位小数。
     * @author cuiyongxu
     */
    public static double round_halfUp(double d, int scale) {
        return round(d, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 将double格式化，按四舍五入截取小数部分
     *
     * @param d             ： 要格式化的数值
     * @param scale         ：小数部分位数 ，不足补0
     * @param thousandSplit ： 是否用千分位分隔符
     * @return String       ： 格式化后的字符串
     * @author cuiyongxu
     */
    public static String format(double d, int scale, boolean thousandSplit) {
        return formatDouble(d, scale, BigDecimal.ROUND_HALF_UP, thousandSplit, true);
    }

    /**
     * 精确的加法运算.
     *
     * @param v1 被加数
     * @param v2 加数
     * @return double 返回v1+v2的和
     * @author cuiyongxu
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 精确的减法运算.
     *
     * @param v1 被减数
     * @param v2 减数
     * @return double 返回v1-v2的值
     * @author cuiyongxu
     */
    public static double subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算.
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return double 返回v1*v2的值
     * @author cuiyongxu
     */
    public static double multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算，并对运算结果截位.
     *
     * @param v1    被乘数
     * @param v2    乘数
     * @param scale 运算结果小数后精确的位数
     * @return double 返回v1*v2值，并保留scale位小数
     * @author cuiyongxu
     */
    public static double multiply(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算.
     *
     * @param v1 被除数
     * @param v2 除数
     * @return double 返回v1除以v2的值
     * @author cuiyongxu
     * @see #divide(double, double, int)
     */
    public static double divide(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算.
     * 由scale参数指定精度，以后的数字四舍五入.
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位
     * @return double 返回v1除以v2的值，并保留scale位小数
     * @author cuiyongxu
     */
    public static double divide(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理.
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return double 对double类型的参数v进行四舍五入，并保留scale位小数
     * @author cuiyongxu
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //--------------------------

    /**
     * 将一个字符串转化为整数，如果转化失败，则返回给定的默认值
     *
     * @param str          要转化的字符串
     * @param defaultValue 转化失败后的默认值
     * @return int         把参数str转化为整数
     * @author cuiyongxu
     */
    public static int getInt(String str, int defaultValue) {
        Integer num = null;
        if (RegexUtil.isInt(str, true)) {
            num = Integer.valueOf(str.trim());
        }
        return null == num ? defaultValue : num;
    }

    /**
     * 将给定字符串转化成Long行数值
     *
     * @param str          要转换的字符串
     * @param defaultValue 缺省值
     * @return long        将字符串str转化为long型数值
     * @author cuiyongxu
     */
    public static long getLong(String str, long defaultValue) {
        Long l = null;
        if (RegexUtil.isLong(str, true)) {
            l = new Long(str.trim());
        }
        return null == l ? defaultValue : l;
    }

    /**
     * 将一个字符串转化为double
     *
     * @param str          要转化的字符串
     * @param defaultValue 默认值
     * @param acceptE      是否兼容科学记数法。true：接受，false：不接受
     * @return double      将字符串str转化为double类型，缺省值为defaultValue，可定义是否兼容科学记数法
     * @author cuiyongxu
     */
    public static double getDouble(String str, double defaultValue, boolean acceptE) {
        Double d = null;
        if (RegexUtil.isDouble(str, true, acceptE)) {
            d = new Double(str.trim());
        }
        return d == null ? defaultValue : d;
    }

}
