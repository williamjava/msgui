package com.opensource.msgui.ctl.user.controller.config;

import com.opensource.msgui.ctl.user.controller.interceptor.TraceHeaderInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author whj
 *
 * 拦截器配置
 */
@Configuration
public class TraceConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new TraceHeaderInterceptor());
        registration.addPathPatterns("/**");
    }
}
