package com.peas.xinrui.common.schedulejob;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peas.xinrui.common.L;
import com.peas.xinrui.common.service.EnvironmentService;
import com.peas.xinrui.common.utils.CollectionUtils;

@Service
public class SchedulerManager {
    @Autowired
    private JobConfig config;
    @Autowired
    private EnvironmentService environmentHelper;

    private boolean mqEnabled;
    private Scheduler instance;

    @PostConstruct
    public void init() {
        boolean jobSchedule = "job".contentEquals(config.getType());
        if (jobSchedule) {
            instance = new Scheduler();
            L.warn("Enable job-executing");
        }
        mqEnabled = jobSchedule;
        if (mqEnabled) {
            if (CollectionUtils.isNotEmpty(config.getWhitelist()) && config.getWhitelist().contains("!mq")) {
                mqEnabled = false;
            }
        }
    }

    /** deprecated **/
    public boolean isScheduleEnabled() {
        return mqEnabled;
    }

    public boolean isMQEnabled() {
        return mqEnabled;
    }

    public void schedule(String jobName, String crontab, ScheduledJob job) throws Exception {
        if (instance == null) {
            return;
        }
        boolean contains;
        if (CollectionUtils.isEmpty(config.getWhitelist())) {
            contains = true;
        } else {
            contains = config.getWhitelist().contains(jobName);
            if (!contains) {
                int moduleEndIndex = jobName.indexOf('.');
                if (moduleEndIndex < 0) {
                    // TODO 以后jobName只能以模块名+"."开头
                    moduleEndIndex = jobName.indexOf('_');
                }
                String module = jobName.substring(0, moduleEndIndex);
                if (module.indexOf(".") >= 0 || module.indexOf("_") >= 0 || module.indexOf("-") >= 0) {
                    throw new RuntimeException("Bad job name: " + jobName);
                }
                contains = config.getWhitelist().contains(module);
            }
        }
        if (contains) {
            instance.schedule(jobName, environmentHelper.isDev() ? "0/5 * * * * ?" : crontab, job);
        }
    }
}
