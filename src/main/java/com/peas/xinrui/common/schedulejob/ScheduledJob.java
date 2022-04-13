package com.peas.xinrui.common.schedulejob;

public interface ScheduledJob {

    void run() throws Exception;

    void terminate() throws Exception;

}
