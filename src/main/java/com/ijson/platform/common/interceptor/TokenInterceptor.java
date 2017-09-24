package com.ijson.platform.common.interceptor;

import com.google.common.base.Strings;
import com.ijson.platform.common.annotation.Token;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.UUID;


/**
 * description: 防止数据重复提交拦截器
 * 用法：在表里加入<input type="hidden" name="formToken" value="${formToken}" />
 * 在需要生成token的controller上增加@Token(save=true)，而在需要检查重复提交的controller上添加@Token(remove=true)就可以了
 * 如：initSave()方法上加@Token(save=true); doSave()方法上加@Token(remove=true)
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

    /**
     * 方法执行前拦截
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {//方法级拦截
            //HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = ((HandlerMethod) handler).getMethod();
            Token annotation = method.getAnnotation(Token.class);
            if (annotation != null) {
                boolean needSaveSession = annotation.save();
                if (needSaveSession) {//加入token
                    HttpSession session = request.getSession(true);
                    if (null != session)
                        session.setAttribute("formToken", UUID.randomUUID().toString());
                }
                boolean needRemoveSession = annotation.remove();
                if (needRemoveSession) {//移出token
                    if (isRepeatSubmit(request)) {
                        return false;
                    }
                    request.getSession(false).removeAttribute("formToken");
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    /**
     * 判断是否重复提交
     */
    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession(false).getAttribute("formToken");
        if (Strings.isNullOrEmpty(serverToken)) {
            return true;
        }
        String clinetToken = request.getParameter("formToken");
        return Strings.isNullOrEmpty(clinetToken) || !serverToken.equals(clinetToken);
    }
}
