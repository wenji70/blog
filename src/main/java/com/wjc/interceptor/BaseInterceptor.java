package com.wjc.interceptor;

import com.wjc.utils.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class BaseInterceptor implements HandlerInterceptor {
    @Autowired
    private Commons commons;

    //在请求处理之前调用。如果返回false，则停止流程，api不会继续往下执行。返回true，则继续执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    //请求处理之后，视图渲染之前调用。可以在这里对数据模型和视图进行修改。
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 用户将封装的Commons工具返回页面
        request.setAttribute("commons",commons);
    }

    //在整个请求结束之后调用，也就是在DispatcherServlet渲染了视图之后执行。这个方法的主要用途是用于资源清理工作。
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}