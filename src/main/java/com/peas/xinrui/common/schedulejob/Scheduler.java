package com.peas.xinrui.common.schedulejob;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peas.xinrui.common.L;

public class Scheduler {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJobThread.class);
    private static List<ScheduledJobThread> threads = new LinkedList<>();
    private static volatile boolean stopped;

    public Scheduler() {
        Runtime.getRuntime().addShutdownHook(new Thread("destroy") {
            public void run() {
                shutdown();
            }
        });
    }

    public class ScheduledJobThread extends Thread {
        private String jobName;
        private CronExpression crontab;
        private ScheduledJob job;

        public ScheduledJobThread(String jobName, String crontab, ScheduledJob job) {
            super("job_schedule");
            this.jobName = jobName;
            try {
                this.crontab = new CronExpression(crontab);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            this.job = job;
        }

        public String getJobName() {
            return jobName;
        }

        public ScheduledJob getJob() {
            return job;
        }

        @Override
        public void run() {
            while (!stopped) {
                try {
                    Date lastCompletedTime = new Date();
                    waitUntilScheduled(lastCompletedTime);
                    if (stopped) {
                        break;
                    }
                    job.run();
                } catch (Throwable e) {
                    LOG.error(null, e);
                    try {
                        Thread.sleep(1000);
                    } catch (Throwable t) {
                        // ignore
                    }
                }
            }
        }

        private void log(String msg) {
            LOG.info("[" + jobName + "] " + msg);
        }

        private void waitUntilScheduled(Date afterTime) {
            if (stopped) {
                return;
            }
            Date scheduleTime = crontab.getNextValidTimeAfter(afterTime);
            long delay = scheduleTime.getTime() - System.currentTimeMillis();
            if (LOG.isInfoEnabled()) {
                log("schedule at " + scheduleTime + ", delay " + TimeUnit.MILLISECONDS.toSeconds(delay) + "s");
            }
            if (delay <= 0) {
                return;
            }
            synchronized (this) {
                try {
                    wait(delay);
                } catch (InterruptedException e) {
                    LOG.error(null, e);
                }
            }
        }

        public void shutdown() {
            L.warn("[" + jobName + "] terminating");
            stopped = true;
            try {
                job.terminate();
            } catch (Exception e) {
                LOG.error(null, e);
            }
            synchronized (this) {
                try {
                    notify();
                } catch (Exception e) {
                    LOG.error(null, e);
                }
            }
            try {
                join();
            } catch (Exception e) {
                L.error(null, e);
            }
            LOG.warn("[" + jobName + "] is terminated");
        }

    }

    public void schedule(String jobName, String crontab, ScheduledJob job) throws Exception {
        synchronized (threads) {
            if (stopped) {
                throw new RuntimeException("Already stopped");
            }
            ScheduledJobThread thread = new ScheduledJobThread(jobName, crontab, job);
            threads.add(thread);
            thread.start();
        }
    }

    private void shutdown() {
        synchronized (threads) {
            stopped = true;
            for (ScheduledJobThread thread : threads) {
                thread.shutdown();
            }
        }
    }

}
