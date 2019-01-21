package com.example.permission.config;

import com.example.permission.filter.AclControllerFilter;
import com.example.permission.filter.LoginFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author CookiesEason
 * 2019/01/21 11:47
 */
@Configuration
public class MyFilterConfigurer {

    @Bean
    public FilterRegistrationBean loginRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LoginFilter());
        registration.setName("loginFilter");
        registration.addUrlPatterns("/api/sys/*");
        registration.addUrlPatterns("/api/user/info");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean aclRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AclControllerFilter());
        registration.setName("aclControllerFilter");
        registration.addUrlPatterns("/api/sys/*");
        registration.addInitParameter("exclusionUrls", "/api/user/login,/api/user/logout");
        registration.setOrder(2);
        return registration;
    }

}
