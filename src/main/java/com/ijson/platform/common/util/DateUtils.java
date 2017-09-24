package com.ijson.platform.common.util;

import com.google.common.base.Strings;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;


/**
 * description:  日期Util类
 *
 * @author cuiyongxu 创建时间：Oct 8, 2015
 */
public class DateUtils {

    private static String defaultDatePattern = "yyyy-MM-dd";
    private static final long oneDayMillSeconds = 24 * 60 * 60 * 1000;

    private DateUtils() {
    }

    private static DateUtils instance;

    public static DateUtils getInstance() {
        if (null == instance) {
            instance = new DateUtils();
        }
        return instance;
    }

    /**
     * description: 获取毫秒数
     *
     * @return
     * @author cuiyongxu
     * @update Oct 27, 2015
     */
    public long getLongTime() {
        Date today = new Date();
        return today.getTime();
    }

    /**
     * description: 返回预设Format的当前日期字符串
     *
     * @return String 把当前日期格式化为"yyyy-MM-dd"形式的字符串
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String getToday() {
        Date today = new Date();
        return format(today);
    }

    /**
     * description:  获取当前时间,yyyy-MM-dd HH:mm:ss
     *
     * @author cuiyongxu
     * @update Nov 12, 2015
     */
    public String getTodayTime() {
        Date today = new Date();
        String timePattern = "yyyy-MM-dd HH:mm:ss";
        return format(today, timePattern);
    }

    /**
     * description:使用预设Format格式化Date成字符串
     *
     * @param date 给定日期
     * @return String 将给定日期格式化为"yyyy-MM-dd"形式的字符串
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String format(Date date) {
        return date == null ? "" : format(date, defaultDatePattern);
    }

    /**
     * description:  使用参数Format格式化Date成字符串
     *
     * @param date    给定日期
     * @param pattern 给定的格式化字符串
     * @return String 将给定的日期按照pattern进行格式化，并返回格式化好的日期字符串。
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String format(Date date, String pattern) {
        return date == null ? "" : new SimpleDateFormat(pattern).format(date);
    }

    /**
     * description:  使用预设格式将字符串转为Date
     *
     * @param strDate 给定的字符串
     * @return Date 将给定的字符串格式化为"yyyy-MM-dd"的日期类型返回
     * @throws java.text.ParseException
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date parse(String strDate) throws ParseException {
        return Strings.isNullOrEmpty(strDate) ? null : parse(strDate, defaultDatePattern);
    }

    /**
     * description: 使用参数Format将字符串转为Date
     *
     * @param strDate 给定的字符串
     * @param pattern 给定的格式化格式
     * @return Date   将给定的字符串按照给定的格式格式化成日期类型返回
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date parse(String strDate, String pattern) throws ParseException {
        return Strings.isNullOrEmpty(strDate) ? null : new SimpleDateFormat(pattern).parse(strDate);
    }

    /**
     * description: 根据年月日获得指定的日期
     *
     * @param year  给定的年份
     * @param month 给定的月份
     * @param day   给定的日期
     * @return Date 根据给定的年月日返回指定的日期
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, 0, 0, 0);
        return cal.getTime();
    }

    /**
     * description: 判断给定日期是否为当月的最后一天
     *
     * @param date 给定日期
     * @return boolean 为true表示该日期为当月最后一天，为false表示该日期不是当月的最后一天。
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public boolean isEndOfTheMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return cal.get(Calendar.DATE) == maxDay;
    }

    /**
     * description: 判断给定日期是否为当年的最后一天
     *
     * @param date 给定的日期
     * @return boolean 为true表示该日期为当年最后一天，为false表示该日期不是当年最后一天
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public boolean isEndOfTheYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (11 == cal.get(Calendar.MONTH)) && (31 == cal.get(Calendar.DATE));
    }

    /**
     * description: 获得给定日期的月份的最后一天
     *
     * @param date 给定的日期
     * @return int 给定日期月份的最后一天
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public int getLastDayOfTheMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * description: 判断开始日期是否比结束日期早
     *
     * @param startTime 给定的开始时间
     * @param endTime   给定的结束时间
     * @return boolean  为true表示开始时间比结束时间早，为false表示开始时间比结束时间晚。
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public boolean isStartBeforeEndTime(Date startTime, Date endTime) {
        Validator.notNull(startTime, "StartTime is null");
        Validator.notNull(endTime, "EndTime is null");
        return startTime.getTime() < endTime.getTime();
    }

    /**
     * description: 比较两个日期相差天数
     *
     * @param startTime 给定的开始时间
     * @param endTime   给定的结束时间
     * @return long     给定的开始时间和给定的结束时间相差天数，返回long型值。
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public long comparisonDifferenceDays(Date startTime, Date endTime) throws ParseException {
        Validator.notNull(startTime, "StartTime is null");
        Validator.notNull(endTime, "EndTime is null");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        long timethis = calendar.getTimeInMillis();
        calendar.setTime(endTime);
        long timeend = calendar.getTimeInMillis();
        return (timeend - timethis) / (1000 * 60 * 60 * 24);
    }

    /**
     * description: 判断给定日期是否为对应日期月份的第一天
     *
     * @param date 给定日期
     * @return boolean 为true表示给定日期是对应日期月份的第一天，为false表示给定日期不是对应日期的第一天。
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public boolean isStartOfTheMonth(Date date) {
        Validator.notNull(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return 1 == cal.get(Calendar.DATE);
    }

    /**
     * description: 判断给定日期是否为对应日期年份的第一天
     *
     * @param date 给定日期
     * @return boolean 为true表示给定日期是对应日期年份的第一天，为false表示给定日期不是对应日期年份的第一天。
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public boolean isStartOfTheYear(Date date) {
        Validator.notNull(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (1 == cal.get(Calendar.MONTH)) && (1 == cal.get(Calendar.DATE));
    }

    /**
     * description: 获取给定日期的月份
     *
     * @param date 给定日期
     * @return int  给定日期的月份
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public int getMonth(Date date) {
        Validator.notNull(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    /**
     * description: 获取给定日期的年份
     *
     * @param date 给定日期
     * @return int  给定日期的年份
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public int getYear(Date date) {
        Validator.notNull(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * description: 获取不含不含小时分钟秒的系统日期
     *
     * @return Date 系统当前日期，不含小时分钟秒。
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date getSystemDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(cal.getTime().getTime());
    }

    /**
     * description: 获取系统的 Timestamp
     *
     * @return Timestamp 系统当前时间的时间戳
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Timestamp getSystemTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public void main(String[] args) {
        Object dd = DateUtils.getInstance().format(new Date(), "yyyy");
        System.out.println(dd);
    }

    /**
     * description: 由某个日期，前推若干毫秒
     *
     * @param date        给定的日期
     * @param millSeconds 给定前推的秒数
     * @return Date       将给定的日期前推给定的秒数后的日期
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date before(Date date, long millSeconds) {
        return fromLong(date.getTime() - millSeconds);
    }

    /**
     * description: 由某个日期，后推若干毫秒
     *
     * @param date        给定的日期
     * @param millSeconds 给定后推的秒数
     * @return Date       将给定的日期后推给定的秒数后的日期
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date after(Date date, long millSeconds) {
        return fromLong(date.getTime() + millSeconds);
    }

    /**
     * description: 得到某个日期之后n天后的日期
     *
     * @param date 给定日期
     * @param nday 给定天数
     * @return Date 给定日期n天后的日期
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date after(Date date, int nday) {
        return fromLong(date.getTime() + nday * oneDayMillSeconds);
    }

    /**
     * description: 得到当前日期之后n天后的日期
     *
     * @param n 给定天数
     * @return Date 当前日期n天后的日期
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date afterNDays(int n) {
        return after(getDate(), n * oneDayMillSeconds);
    }

    /**
     * description: 得到当前日期n天前的日期
     *
     * @param n 给定天数
     * @return Date 当前日期n天数的日期
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date beforeNDays(int n) {
        return beforeNDays(getDate(), n);

    }

    /**
     * description: 得到某个日期n天前的日期
     *
     * @param date 给定日期
     * @param n    给定天数
     * @return Date 给定日期n天前的日期
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date beforeNDays(Date date, int n) {
        return fromLong(date.getTime() - n * oneDayMillSeconds);
    }

    /**
     * description: 昨天
     *
     * @return Date 昨天
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date yesterday() {
        return before(getDate(), oneDayMillSeconds);
    }

    /**
     * description: 明天
     *
     * @return Date 明天
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date tomorrow() {
        return after(getDate(), oneDayMillSeconds);
    }

    public long getA_B(Date dateA, Date dateB) {
        return dateA.getTime() - dateB.getTime();
    }

    /**
     * description: 获取当前系统时间
     *
     * @return Date 当前系统时间
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date getDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * description: 得到一个日期的毫秒表达
     *
     * @param date 给定日期
     * @return long 给定日期的毫秒表达值（Long型）
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public long toLong(Date date) {
        return date.getTime();
    }

    /**
     * description:  将毫秒的日期数值转化为Date对象
     *
     * @param time 给定毫秒值
     * @return Date 把给定的time转换成日期类型
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date fromLong(long time) {
        Date date = getDate();
        date.setTime(time);
        return date;
    }

    /**
     * description: 根据某个字符串得到日期对象
     *
     * @param dateStr 给定的日期字符串
     * @param fmtStr  给定的日期格式类
     * @return Date    根据日期格式类fmtStr把字符串dateStr转换成日期类型
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date strToDate(String dateStr, FmtStr fmtStr) {
        DateFormat df = new SimpleDateFormat(fmtStr.toString());
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * description: 根据某个字符串得到日期对象
     *
     * @param dateStr 给定的日期字符串
     * @param fmtStr  给定的字符串格式
     * @return Date    把dateStr日期字符串格式成fmtStr的日期类型
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public Date strToDate(String dateStr, String fmtStr) {
        DateFormat df = new SimpleDateFormat(fmtStr);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * description: 将毫秒数值日期转化为字符串日期
     *
     * @param time   毫秒数
     * @param fmtStr 日期格式类
     * @return String 将参数time转换成格式为fmtStr的字符串日期
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String longToStr(long time, FmtStr fmtStr) {

        return format(fromLong(time), fmtStr.toString());
    }

    /**
     * description: 将字符串日期转化为毫秒的数值日期
     *
     * @param dateStr 字符串日期
     * @param fmtStr  日期格式类
     * @return long   将字符串日期dateStr转换成毫秒的数值日期
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public long strToLong(String dateStr, FmtStr fmtStr) {
        return strToDate(dateStr, fmtStr).getTime();
    }

    /**
     * description:得到环境变量中操作系统时区，即得到系统属性：user.timezone
     *
     * @return String 得到环境变量中操作系统时区，即得到系统属性：user.timezone
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String getTimeZoneOfSystem() {
        Properties sysProp = new Properties(System.getProperties());
        return sysProp.getProperty("user.timezone");
    }

    /**
     * description:得到jvm中系统时区
     *
     * @return String 得到jvm中系统时区
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String getTimeZoneOfJVM() {
        return TimeZone.getDefault().getID();
    }

    /**
     * description:检验当前操作系统时区是否正确。<br>
     * 判断依据：操作系统环境变量的时区和jvm得到的时区是否一致，一致则表明正确，否则错误。
     *
     * @return boolean true:正确；false:错误
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public boolean checkTimeZone() {
        String sysTimeZone = getTimeZoneOfSystem();
        String jvmTimeZone = getTimeZoneOfJVM();
        return sysTimeZone != null && sysTimeZone.equals(jvmTimeZone);
    }

    /**
     * description:初始化当前日期
     *
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String getNow() {
        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return simp.format(new Date());
    }

    /**
     * description:格式化日期yyyy-MM-dd
     *
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String getDate(String date) {
        String result = "";
        if (!Validator.isNull(date)) {
            result = date.substring(0, 10);
        }
        return result;
    }

    /**
     * description:格式化日期yyyy-MM-dd HH:mm:ss
     *
     * @return
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String getTime(String date) {
        String result = "";
        if (!Validator.isNull(date)) {
            result = date.substring(0, 19);
        }
        return result;
    }

    /**
     * description:格式化日期yyyy-MM
     *
     * @return
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String getMonth(String date) {
        String result = "";
        if (!Validator.isNull(date)) {
            result = date.substring(0, 7);
        }
        return result;
    }

    /**
     * description:查询当前年
     *
     * @return
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * description:查询当前月份
     *
     * @return
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * description:格式化当前月份
     * yyyyMM
     *
     * @return
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public String getNowMonth() {
        int month = getCurrentMonth();
        if (month < 10) {
            return getCurrentYear() + "0" + month;
        } else {
            return getCurrentYear() + "" + month;
        }

    }

    /**
     * description:获取当前日期值
     *
     * @return
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    public Long getLongDate(String date) {
        Long result = 0L;
        try {
            SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
            Date da = simp.parse(date);
            result = da.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * description: 日志格式类，包含常用的日期时间格式
     *
     * @author cuiyongxu
     * @update Jul 3, 2015
     */
    public static class FmtStr {
        private String fmtStr;

        private FmtStr(String str) {
            this.fmtStr = str;
        }

        public String toString() {
            return this.fmtStr;
        }

        public static FmtStr yyyy = new FmtStr("yyyy");
        public static FmtStr yyyyMM = new FmtStr("yyyy-MM");
        public static FmtStr yyyyMMdd = new FmtStr("yyyy-MM-dd");
        public static FmtStr yyyyMMdd_HH = new FmtStr("yyyy-MM-dd HH");
        public static FmtStr yyyyMMdd_HHmm = new FmtStr("yyyy-MM-dd HH:mm");
        public static FmtStr yyyyMMdd_HHmmss = new FmtStr("yyyy-MM-dd HH:mm:ss");
        public static FmtStr yyyyMMdd_HHmmssSSS = new FmtStr("yyyy-MM-dd HH:mm:ss:SSS");

        public static FmtStr HHmm = new FmtStr("HH:mm");
        public static FmtStr HHmmss = new FmtStr("HH:mm:ss");
        public static FmtStr hhmmssSSS = new FmtStr("HH:mm:ss:SSS");

        public static FmtStr CN_yyyyMMdd = new FmtStr("yyyy年MM月dd日");
        /**
         * 中文格式：yyyy年MM月dd日
         */
        public static FmtStr CN_HHmmss = new FmtStr("HH时mm分ss秒");
        /**
         * 中文格式：HH时mm分ss秒
         */
        public static FmtStr CN_yyyyMMdd_HHmmss = new FmtStr("yyyy年MM月dd日 HH时mm分ss秒");
        /** 中文格式：yyyy年MM月dd日 HH时mm分ss秒 */
    }

}
