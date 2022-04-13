package com.peas.xinrui.common.kvCache.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peas.xinrui.common.kvCache.KvCacheExecutor;
import com.peas.xinrui.common.kvCache.KvCachePolicy;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

public class RedisKvCacheExecutor implements KvCacheExecutor {

    protected static final Logger LOG = LoggerFactory.getLogger(RedisKvCacheExecutor.class);
    protected static final boolean INFO_ENABLED = LOG.isInfoEnabled();

    protected JedisPool pool;

    public RedisKvCacheExecutor(JedisPool pool) {
        super();
        this.pool = pool;
    }

    public JedisPool getPool() {
        return pool;
    }

    @Override
    public byte[] find(String key, KvCachePolicy policy) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(SafeEncoder.encode(key));
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Map<String, byte[]> findMany(List<String> keys, KvCachePolicy policy) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[][] bkeys = new byte[keys.size()][0];
            int i = 0;
            for (String key : keys) {
                bkeys[i] = SafeEncoder.encode(key);
                i++;
            }
            List<byte[]> values = jedis.mget(bkeys);
            i = -1;
            Map<String, byte[]> kv = new HashMap<>();
            for (String key : keys) {
                i++;
                byte[] value = values.get(i);
                if (value == null) {
                    continue;
                }
                kv.put(key, value);
            }
            return kv;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void save(String key, byte[] value, KvCachePolicy policy) {
        Jedis jedis = null;
        int expireIn = policy.getExpireIn();
        try {
            jedis = pool.getResource();
            jedis.setex(SafeEncoder.encode(key), expireIn, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void saveMany(Map<String, byte[]> items, KvCachePolicy policy) {
        Jedis jedis = null;
        int expireIn = policy.getExpireIn();
        try {
            jedis = pool.getResource();
            for (Entry<String, byte[]> item : items.entrySet()) {
                jedis.setex(SafeEncoder.encode(item.getKey()), expireIn, item.getValue());
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void remove(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void batchRemove(String pattern) {
        Jedis jedis = null;
        Set<String> keys = new HashSet<>();
        try {
            jedis = pool.getResource();
            keys = jedis.keys(pattern);
            for (String key : keys) {
                jedis.del(key);
                if (INFO_ENABLED) {
                    LOG.info("[KvCache] remove <{}>", key);
                }
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Set<String> findLike(String pattern) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<String> keys = jedis.keys(pattern);
            return keys;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
