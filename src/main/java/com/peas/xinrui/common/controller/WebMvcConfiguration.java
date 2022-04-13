package com.peas.xinrui.common.controller;

import com.peas.xinrui.common.controller.auth.AdmInterceptor;
import com.peas.xinrui.common.controller.auth.CommonInterceptor;
import com.peas.xinrui.common.controller.auth.SchInterceptor;
import com.peas.xinrui.common.controller.auth.UserInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Autowired
    private AdmInterceptor adminInterceptor;

    @Autowired
    private UserInterceptor userInterceptor;

    @Autowired
    private CommonInterceptor commonInterceptor;

    @Autowired
    private SchInterceptor schInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor).addPathPatterns("/adm/**");
        registry.addInterceptor(userInterceptor).addPathPatterns("/usr/**");
        registry.addInterceptor(commonInterceptor).addPathPatterns("/common/**");
        registry.addInterceptor(schInterceptor).addPathPatterns("/sch/**");

    }

}