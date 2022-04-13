package com.peas.xinrui.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.sunnysuperman.commons.config.PropertiesConfig;
import com.sunnysuperman.commons.util.JSONUtil;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.peas.xinrui.common.L;

//TODO use zookeeper instead
@Service
public class DistributeLock {
    @Value("${redis.distribute-lock}")
    private String db;
    private RedissonClient client;

    @PostConstruct
    public void init() {
        PropertiesConfig cfg = new PropertiesConfig(JSONUtil.parseJSONObject(db));
        Config config = new Config();
        SingleServerConfig singleSerververConfig = config.useSingleServer();
        singleSerververConfig.setAddress(cfg.getString("host") + ":" + cfg.getInt("port", 6379));
        singleSerververConfig.setPassword(cfg.getString("password"));
        singleSerververConfig.setDatabase(cfg.getInt("db"));
        singleSerververConfig.setConnectionMinimumIdleSize(cfg.getInt("minIdle"));
        singleSerververConfig.setConnectionPoolSize(cfg.getInt("maxTotal"));
        client = Redisson.create(config);
    }

    public static interface InLockWork {

        void run() throws Exception;

    }

    // TODO use zookeeper
    public boolean doInLock(String key, InLockWork work, int timeout, int leaseTime, boolean defaultLockGetted)
            throws Exception {
        RLock lock = client.getLock(key);
        boolean lockGetted = false;
        try {
            lockGetted = lock.tryLock(timeout, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            L.error(e);
        } catch (Exception e) {
            lockGetted = defaultLockGetted;
        }
        if (!lockGetted) {
            return false;
        }
        try {
            work.run();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean doInLock(String key, InLockWork work, int timeout, int leaseTime) throws Exception {
        return doInLock(key, work, timeout, leaseTime, false);
    }

    public boolean doInLock(List<String> keys, InLockWork work, int timeout, int leaseTime) throws Exception {
        List<RLock> locks = new ArrayList<>(keys.size());
        try {
            for (String key : keys) {
                try {
                    RLock lock = client.getLock(key);
                    if (!lock.tryLock(timeout, leaseTime, TimeUnit.SECONDS)) {
                        return false;
                    }
                    locks.add(lock);
                } catch (Exception ex) {
                    L.error(ex);
                    return false;
                }
            }
            work.run();
            return true;
        } finally {
            for (RLock lock : locks) {
                try {
                    lock.unlock();
                } catch (Exception e) {
                    L.error(e);
                }
            }
        }
    }
}
