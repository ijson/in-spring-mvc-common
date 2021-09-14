package com.ijson.platform.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * description: 统一异常处理器
 */
@Slf4j
public class UnityExceptionResolver implements HandlerExceptionResolver {


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {

        String requestInfo = "URL:" + request.getRequestURI();
        requestInfo += " Query:" + request.getQueryString();
        String header = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(header) ? true : false;
        String message = "系统运行异常！";
        /*// 根据不同错误转向不同页面
        if (ex instanceof DaoAccessException) {
            writeLog(ex, requestInfo);
            message = "数据库操作异常！";
        } else if (ex instanceof BusinessException) {
            writeLog(ex, requestInfo);
            message = "业务方法执行异常！";
        } else if (ex instanceof ServiceException) {
            writeLog(ex, requestInfo);
            message = "service调用异常！";
        } else {
            writeLog(ex, requestInfo);
        }*/
        if (isAjax) {//ajax请求
            response.setContentType("text/html;charset=utf-8");
            try {
                PrintWriter writer = response.getWriter();
                writer.write(message);
                writer.flush();
                writer.close();
            } catch (IOException e) {
            }
            return new ModelAndView();
        } else {//正常请求
            request.setAttribute("errors", message);
            return new ModelAndView("error/error");
        }
    }

}
