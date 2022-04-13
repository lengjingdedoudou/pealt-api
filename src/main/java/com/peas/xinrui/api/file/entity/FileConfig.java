package com.peas.xinrui.api.file.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "fs", ignoreUnknownFields = false)
@Validated
@Component
public class FileConfig {

    private String tmpDir;

    private String domain;

    public String getTmpDir() {
        return tmpDir;
    }

    public void setTmpDir(final String tmpDir) {
        this.tmpDir = tmpDir;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(final String domain) {
        this.domain = domain;
    }
}
