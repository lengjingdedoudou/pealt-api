package com.peas.xinrui.common.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ServiceManager implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static Object get(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T get(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
