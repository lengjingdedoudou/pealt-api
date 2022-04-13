package com.peas.xinrui.common.cache;

import java.util.HashSet;

import javax.annotation.PostConstruct;

import com.sunnysuperman.commons.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.peas.xinrui.common.cache.kvGlobal.KvGlobalSearchCache;
import com.peas.xinrui.common.cache.kvGlobal.KvGlobalSearchWrapper;
import com.peas.xinrui.common.kvCache.KvCacheExecutor;
import com.peas.xinrui.common.kvCache.KvCachePolicy;
import com.peas.xinrui.common.kvCache.KvCacheSaveFilter;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.ModelConverter;
import com.peas.xinrui.common.kvCache.converter.StringModelConverter;
import com.peas.xinrui.common.kvCache.redis.RedisKvCacheExecutor;
import com.peas.xinrui.common.redis.RedisClient;
import com.peas.xinrui.common.redis.RedisUtils;
import com.peas.xinrui.common.task.TaskService;
import redis.clients.jedis.JedisPool;

@Service
public class KvCacheFactory {
    @Value("${redis.cache}")
    private String config;
    @Autowired
    private TaskService taskService;
    private KvCacheExecutor executor;
    private RedisClient client;
    private java.util.Set<String> registeredKeys = new HashSet<>();

    @PostConstruct
    public void init() {
        JedisPool redisPool = RedisUtils.createPool(config);
        executor = new RedisKvCacheExecutor(redisPool);
        client = new RedisClient(redisPool, true);
    }

    public <K, T> KvCacheWrap<K, T> create(CacheOptions options, RepositoryProvider<K, T> repository,
            ModelConverter<T> converter, KvCacheSaveFilter<K, T> saveFilter) {
        if (StringUtil.isEmpty(options.getKey())) {
            throw new IllegalArgumentException("Bad cache key");
        }
        if (registeredKeys.contains(options.getKey())) {
            throw new IllegalArgumentException("Duplicate cache key");
        }
        registeredKeys.add(options.getKey());
        if (options.getVersion() <= 0) {
            throw new IllegalArgumentException("Bad cache version");
        }
        if (options.getExpireIn() <= 0) {
            throw new IllegalArgumentException("Bad cache expireIn");
        }
        KvCachePolicy policy = new KvCachePolicy();
        policy.setPrefix(options.getKey() + ":" + options.getVersion() + ":");
        policy.setExpireIn(options.getExpireIn());
        return new KvCacheWrap<K, T>(executor, policy, repository, converter, saveFilter, taskService);
    }

    public <K, T> KvCacheWrap<K, T> create(CacheOptions options, RepositoryProvider<K, T> repository,
            ModelConverter<T> converter) {
        return create(options, repository, converter, null);
    }

    // 创建特定的kvCacheWrapper接口
    public <K, T> KvGlobalSearchCache<K, T> createGlobal(CacheOptions options, KvCachePolicy policy,
            RepositoryProvider<K, T> repository, ModelConverter<T> converter, KvCacheSaveFilter<K, T> saveFilter) {
        return new KvGlobalSearchWrapper<>(executor, policy, repository, converter, saveFilter, taskService);
    }

    public KvGlobalSearchCache<Object, String> createGlobalCacheWrap() {
        KvCachePolicy policy = new KvCachePolicy();
        policy.setPrefix("");
        policy.setExpireIn(1);
        return createGlobal(null, policy, null, StringModelConverter.getInstance(), null);
    }

    public RedisClient getClient() {
        return client;
    }
}
