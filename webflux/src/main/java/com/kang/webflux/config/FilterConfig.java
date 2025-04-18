package com.kang.webflux.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kang.webflux.filter.MyFilter;

import jakarta.servlet.Filter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> addFilter() {

        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new MyFilter());

        bean.addUrlPatterns("/*");

        return bean;

    }
    
}