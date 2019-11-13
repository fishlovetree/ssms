package com.ws.ssms.business.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Bean
    public SecurityInterceptor securityInterceptor() {
        return new SecurityInterceptor();
    }

    /**
     * @param registry 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加登录处理拦截器，拦截所有请求，登录请求除外
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(securityInterceptor());
        //排除配置
        interceptorRegistration.excludePathPatterns("/admin/notlogon");
        interceptorRegistration.excludePathPatterns("/admin/unauth");
        interceptorRegistration.excludePathPatterns("/swagger-ui.html/**");
        interceptorRegistration.excludePathPatterns("/webjars/**");
        interceptorRegistration.excludePathPatterns("/swagger-resources/**");
        interceptorRegistration.excludePathPatterns("/v2/**");
        interceptorRegistration.excludePathPatterns("/**/error");
        //配置拦截策略
        interceptorRegistration.addPathPatterns("/**");
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决 swagger-ui.html 404报错
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}