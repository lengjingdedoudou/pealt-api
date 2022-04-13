package com.peas.xinrui.common.kvCache;

public interface KvCacheSaveFilter<K, T> {

    boolean filter(K key, T value);

}
