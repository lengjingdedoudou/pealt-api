package com.peas.xinrui.common.cache.kvGlobal;

import java.util.Map;

public interface KvGlobalSearchCache<K, V> {
    Map<K, V> findLike(String pattern);
}