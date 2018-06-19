package com.ijson.platform.common.util.browser;

import com.ijson.platform.common.util.ConfigFactory;
import com.ijson.platform.common.util.Validator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 专门对cookie进行操作
 *
 * @author cuiyongxu 创建时间：2015-12-22
 */
@Slf4j
public class CookieTools {

    private static CookieTools instance;

    private CookieTools() {

    }

    public static CookieTools getInstance() {
        if (null == instance) {
            instance = new CookieTools();
        }
        return instance;
    }

    public static void add2Cookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                  String cookieValue, int maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);

        String str = ConfigFactory.get("isDoMain");
        if ("T".equals(str)) {
            cookie.setDomain(ConfigFactory.get("domain"));
        } else {
            //cookie.setDomain("");
        }
        cookie.setPath("/");
        //cookie.setMaxAge(maxAge); // cookie一年内有效60*60*24*365
        response.addCookie(cookie);

    }

    /**
     * 删除cookie
     *
     * @param request    请求
     * @param response   响应
     * @param cookieName cookieName
     * @return success or faild
     * @author cuiyongxu
     */
    public boolean deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        String domain = ConfigFactory.get("domain");
        String isDoMain = ConfigFactory.get("isDoMain");
        if (cookieName != null) {
            Cookie cookie = getCookie(request, cookieName);//getCookie()方法在下面
            if (cookie != null) {
                cookie.setMaxAge(0);// 如果0，就说明立即删除
                cookie.setPath("/");// 不要漏掉
                if ("T".equals(isDoMain)) {
                    cookie.setDomain(domain);
                }
                response.addCookie(cookie);
                return true;
            }
        }
        return false;
    }

    /**
     * 获取cookie值
     *
     * @param request    请求
     * @param cookieName cookieName
     * @return cookie
     * @author cuiyongxu
     */
    public Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        try {
            if (!Validator.isEmpty(cookies)) {
                for (Cookie cooky : cookies) {
                    cookie = cooky;
                    if (cookieName.equals(cookie.getName())) {
                        return cookie;
                    }
                }
            }
        } catch (Exception e) {
            log.error("CookieTools getCookie : ", e);
        }
        return cookie;
    }
}
