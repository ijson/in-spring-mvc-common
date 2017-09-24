package com.ijson.platform.common.util.browser;

import com.ijson.platform.common.util.SystemUtil;
import com.ijson.platform.common.util.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * description: 浏览器工具类.
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
public class BrowserUtils {

    private final static String IE11 = "rv:11.0";
    private final static String IE10 = "MSIE 10.0";
    private final static String IE9 = "MSIE 9.0";
    private final static String IE8 = "MSIE 8.0";
    private final static String IE7 = "MSIE 7.0";
    private final static String IE6 = "MSIE 6.0";
    private final static String MAXTHON = "Maxthon";
    private final static String QQ = "QQBrowser";
    private final static String GREEN = "GreenBrowser";
    private final static String SE360 = "360SE";
    private final static String FIREFOX = "Firefox";
    private final static String OPERA = "Opera";
    private final static String CHROME = "Chrome";
    private final static String SAFARI = "Safari";
    private final static String CAMINO = "Camino";
    private final static String OTHER = "其它";

    private final static String EASYUI = "easyui";
    private final static String BOOTSTARP = "bootstarp";
    private final static String EXT = "ext";
    private final static String STYLEOTHER = "styleOther";

    /**
     * description:  获取当前浏览器样式
     *
     * @return
     * @author cuiyongxu
     * @update Nov 3, 2015
     */
    public static StyleType getCurrStyle() {
        StyleType styleType = null;
        String ts = SystemUtil.getInstance().getConstant("style");
        String style = "easyui";
        if (!Validator.isNull(ts)) {
            style = ts.trim();
        }

        if (style.equals(EASYUI)) {
            styleType = StyleType.EASYUI;
        }
        if (style.equals(BOOTSTARP)) {
            styleType = StyleType.BOOTSTARP;
        }
        if (style.equals(EXT)) {
            styleType = StyleType.EXT;
        }
        if (style.equals(STYLEOTHER)) {
            styleType = StyleType.STYLEOTHER;
        }
        return styleType;
    }

    /**
     * description:  判断是否是IE
     *
     * @param request
     * @return
     * @author cuiyongxu
     * @update Nov 3, 2015
     */
    public static boolean isIE(HttpServletRequest request) {
        return (getUserAgent(request).toLowerCase().indexOf("msie") > 0 || getUserAgent(request).toLowerCase().indexOf(
                "rv:11.0") > 0) ? true : false;
    }

    /**
     * description:  获取IE版本
     *
     * @param request
     * @return
     * @author cuiyongxu
     * @update Oct 27, 2015
     */
    public static Double getIEversion(HttpServletRequest request) {
        Double version = 0.0;
        if (getBrowserType(request, IE11)) {
            version = 11.0;
        } else if (getBrowserType(request, IE10)) {
            version = 10.0;
        } else if (getBrowserType(request, IE9)) {
            version = 9.0;
        } else if (getBrowserType(request, IE8)) {
            version = 8.0;
        } else if (getBrowserType(request, IE7)) {
            version = 7.0;
        } else if (getBrowserType(request, IE6)) {
            version = 6.0;
        }
        return version;
    }

    /**
     * 获取浏览器类型
     *
     * @param request
     * @return
     */
    public static BrowserType getBrowserType(HttpServletRequest request) {
        BrowserType browserType = null;
        if (getBrowserType(request, IE11)) {
            browserType = BrowserType.IE11;
        }
        if (getBrowserType(request, IE10)) {
            browserType = BrowserType.IE10;
        }
        if (getBrowserType(request, IE9)) {
            browserType = BrowserType.IE9;
        }
        if (getBrowserType(request, IE8)) {
            browserType = BrowserType.IE8;
        }
        if (getBrowserType(request, IE7)) {
            browserType = BrowserType.IE7;
        }
        if (getBrowserType(request, IE6)) {
            browserType = BrowserType.IE6;
        }
        if (getBrowserType(request, FIREFOX)) {
            browserType = BrowserType.Firefox;
        }
        if (getBrowserType(request, SAFARI)) {
            browserType = BrowserType.Safari;
        }
        if (getBrowserType(request, CHROME)) {
            browserType = BrowserType.Chrome;
        }
        if (getBrowserType(request, OPERA)) {
            browserType = BrowserType.Opera;
        }
        if (getBrowserType(request, CAMINO)) {
            browserType = BrowserType.Camino;
        }
        return browserType;
    }

    private static boolean getBrowserType(HttpServletRequest request, String brosertype) {
        return getUserAgent(request).indexOf(brosertype) > 0 ? true : false;
    }

    public static String checkBrowse(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (regex(OPERA, userAgent))
            return OPERA;
        if (regex(CHROME, userAgent))
            return CHROME;
        if (regex(FIREFOX, userAgent))
            return FIREFOX;
        if (regex(SAFARI, userAgent))
            return SAFARI;
        if (regex(SE360, userAgent))
            return SE360;
        if (regex(GREEN, userAgent))
            return GREEN;
        if (regex(QQ, userAgent))
            return QQ;
        if (regex(MAXTHON, userAgent))
            return MAXTHON;
        if (regex(IE11, userAgent))
            return IE11;
        if (regex(IE10, userAgent))
            return IE10;
        if (regex(IE9, userAgent))
            return IE9;
        if (regex(IE8, userAgent))
            return IE8;
        if (regex(IE7, userAgent))
            return IE7;
        if (regex(IE6, userAgent))
            return IE6;
        return OTHER;
    }

    public static boolean regex(String regex, String str) {
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 客户端信息
     *
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("USER-AGENT");
    }

}
