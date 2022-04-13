package com.peas.xinrui.common.kvCache;

import java.util.Collection;
import java.util.Map;

public interface KvCache<K, T> {

    T findByKey(K key, boolean cacheOnly);

    T findByKey(K key);

    Map<K, T> findByKeys(Collection<K> keys, boolean cacheOnly) throws KvCacheException;

    Map<K, T> findByKeys(Collection<K> keys) throws KvCacheException;

    // 保存到缓存中
    void save(K key, T value) throws KvCacheException;

    void saveMany(Map<K, T> items) throws KvCacheException;

    // 刷新缓存（重新加载内容，写入缓存并返回）
    T refresh(K key) throws Exception;

    // 从缓存中删除
    void remove(K key) throws KvCacheException;

    // 根据prefix清理全部数据
    void flush() throws KvCacheException;

    // 模糊匹配 针对普通缓存器，匹配路径只能搜索他自己的缓存；全局缓存器可以搜索所有的缓存
    Map<K, T> findLike(String pattern);
}
