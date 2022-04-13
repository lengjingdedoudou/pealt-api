package com.peas.xinrui.common.resources;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.stereotype.Service;

import com.peas.xinrui.common.L;
import com.peas.xinrui.common.utils.ProcessUtils;

@Service
public class StaticBootstrap {

    @PostConstruct
    public void init() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("cn.maidaotech.java07"));
        {
            Set<Class<?>> staticInitClasses = reflections.getTypesAnnotatedWith(StaticInit.class);
            for (Class<?> clazz : staticInitClasses) {
                try {
                    if (L.isInfoEnabled()) {
                        L.info("[StaticBootstrap] calling: " + clazz.getCanonicalName());
                    }
                    Class.forName(clazz.getCanonicalName());
                } catch (Throwable t) {
                    ProcessUtils.exitWithMessage(null, t);
                }
            }
        }
        if (L.isInfoEnabled()) {
            L.info("[StaticBootstrap] done");
        }
    }

}
