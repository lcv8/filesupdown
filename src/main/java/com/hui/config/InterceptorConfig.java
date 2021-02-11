package com.hui.config;

import com.hui.inteceputer.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor())//添加拦截器
                .addPathPatterns("/hello/**")//添加拦截路径
                .excludePathPatterns("/hello/world");//配置不经过拦截器的请求
    }
}
