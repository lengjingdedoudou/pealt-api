package com.peas.xinrui.common.kvCache;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface KvCacheExecutor {

    byte[] find(String key, KvCachePolicy policy);

    Map<String, byte[]> findMany(List<String> keys, KvCachePolicy policy);

    void save(String key, byte[] value, KvCachePolicy policy);

    void saveMany(Map<String, byte[]> items, KvCachePolicy policy);

    void remove(String key);

    void batchRemove(String pattern);

    Set<String> findLike(String pattern);
}
