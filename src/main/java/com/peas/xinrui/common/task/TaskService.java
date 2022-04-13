package com.peas.xinrui.common.task;

import javax.annotation.PostConstruct;

import com.sunnysuperman.commons.task.TaskEngine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.peas.xinrui.common.L;

@Service
public class TaskService {
    @Value("${taskservice.max-pool-size}")
    private int maxPoolSize;
    @Value("${taskservice.threshold}")
    private int threshold;
    private TaskEngine engine;

    @PostConstruct
    public void init() {
        if (maxPoolSize <= 0) {
            throw new IllegalArgumentException("Bad maxPoolSize");
        }
        if (threshold <= 0) {
            throw new IllegalArgumentException("Bad threshold");
        }
        engine = new TaskEngine("taskservice", maxPoolSize);
    }

    public void addTask(Runnable task) {
        int taskNum = engine.getTasksNum();
        if (taskNum >= threshold) {
            L.warn("Too many tasks to execute: " + taskNum);
        }
        engine.addTask(task);
    }

    public void scheduleTask(Runnable task, long delay) {
        int taskNum = engine.getTasksNum();
        if (taskNum >= threshold) {
            L.warn("Too many tasks to execute: " + taskNum);
        }
        engine.scheduleTask(task, delay);
    }

    public void scheduleTask(Runnable task, long delay, long period) {
        engine.scheduleTask(task, delay, period);
    }
}
