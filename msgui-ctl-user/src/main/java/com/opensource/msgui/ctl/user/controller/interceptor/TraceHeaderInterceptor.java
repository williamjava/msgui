package com.opensource.msgui.ctl.user.controller.interceptor;

import com.opensource.msgui.apm.context.ApmContext;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author whj
 *
 * 拦截器处理TraceId
 */
public class TraceHeaderInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        response.setHeader("traceId", ApmContext.traceId());
        return true;
    }
}

