package com.peas.xinrui.common.service;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentService {
    @Autowired
    private Environment env;

    public boolean isDev() {
        return ArrayUtils.contains(env.getActiveProfiles(), "dev");
    }

    public boolean isSandbox() {
        return ArrayUtils.contains(env.getActiveProfiles(), "sandbox");
    }

    public boolean isProd() {
        return ArrayUtils.contains(env.getActiveProfiles(), "prod");
    }

}
