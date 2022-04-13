package com.peas.xinrui.common.kvCache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.sunnysuperman.commons.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.DataNotFoundServiceException;
import com.peas.xinrui.common.kvCache.converter.ModelConverter;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.StringUtils;

public class DefaultKvCache<K, T> implements KvCache<K, T> {
    protected static final Logger LOG = LoggerFactory.getLogger(DefaultKvCache.class);
    protected static final boolean INFO_ENABLED = LOG.isInfoEnabled();
    protected KvCacheExecutor executor;
    protected KvCachePolicy policy;
    protected RepositoryProvider<K, T> repository;
    protected ModelConverter<T> converter;
    protected KvCacheSaveFilter<K, T> saveFilter;

    public DefaultKvCache(final KvCacheExecutor executor, final KvCachePolicy policy,
            final RepositoryProvider<K, T> repository, final ModelConverter<T> converter,
            final KvCacheSaveFilter<K, T> saveFilter) {
        this.executor = executor;
        this.policy = policy;
        this.repository = repository;
        this.converter = converter;
        this.saveFilter = saveFilter;
        policy.validate();
    }

    public KvCacheExecutor getExecutor() {
        return executor;
    }

    public KvCachePolicy getPolicy() {
        return policy;
    }

    public RepositoryProvider<K, T> getRepository() {
        return repository;
    }

    public ModelConverter<T> getConverter() {
        return converter;
    }

    public KvCacheSaveFilter<K, T> getSaveFilter() {
        return saveFilter;
    }

    protected String makeFullKey(final K key) {
        final String prefix = policy.getPrefix();
        if (prefix == null || prefix.length() == 0) {
            return key.toString();
        }
        return prefix + key.toString();
    }

    private T findFromCache(final K key) throws KvCacheException {
        final String fullKey = makeFullKey(key);
        final byte[] value = executor.find(fullKey, policy);
        if (INFO_ENABLED) {
            LOG.info("[KvCache] find <{}> <{}>", fullKey, value != null ? "cached" : "not found");
        }
        if (value == null) {
            return null;
        }
        return converter.deserialize(value);
    }

    private Map<K, T> findFromCache(final Collection<K> keys) throws KvCacheException {
        final List<String> fullKeys = new ArrayList<>(keys.size());
        for (final K key : keys) {
            fullKeys.add(makeFullKey(key));
        }
        final Map<String, byte[]> bkv = executor.findMany(fullKeys, policy);
        final List<String> foundKeys = INFO_ENABLED ? new ArrayList<String>(bkv.size()) : null;
        int i = -1;
        final Map<K, T> kv = new HashMap<>();
        for (final K key : keys) {
            i++;
            final String fullKey = fullKeys.get(i);
            final byte[] bvalue = bkv.get(fullKey);
            if (bvalue == null) {
                continue;
            }
            final T value = converter.deserialize(bvalue);
            kv.put(key, value);
            if (foundKeys != null) {
                foundKeys.add(fullKey);
            }
        }
        if (INFO_ENABLED) {
            LOG.info("[KvCache] find <{}>, found: <{}>", StringUtil.join(fullKeys, " "),
                    StringUtil.join(foundKeys, " "));
        }
        return kv;
    }

    @Override
    public void save(final K key, final T value) throws KvCacheException {
        final String fullKey = makeFullKey(key);
        final byte[] data = converter.serialize(value);
        if (data == null) {
            throw new RuntimeException("[KvCache] could not serialize to null");
        }
        executor.save(fullKey, data, policy);
        if (INFO_ENABLED) {
            LOG.info("[KvCache] save <{}>", fullKey);
        }
    }

    @Override
    public void saveMany(final Map<K, T> items) throws KvCacheException {
        if (items.isEmpty()) {
            return;
        }
        final Map<String, byte[]> dataMap = new HashMap<>();
        for (final Entry<K, T> entry : items.entrySet()) {
            final String fullKey = makeFullKey(entry.getKey());
            final byte[] data = converter.serialize(entry.getValue());
            if (data == null) {
                throw new RuntimeException("[KvCache] could not serialize to null");
            }
            dataMap.put(fullKey, data);
        }
        executor.saveMany(dataMap, policy);
        if (INFO_ENABLED) {
            LOG.info("[KvCache] saveMany <{}>", StringUtil.join(dataMap.keySet()));
        }
    }

    @Override
    public T refresh(final K key) throws Exception {
        return findAndSave(key, false);
    }

    private T findAndSave(final K key, final boolean ignoreSaveError) throws Exception {
        // 从存储层中查找
        T model;
        model = repository.findByKey(key);
        if (model == null) {
            return null;
        }
        // 保存到缓存
        if (saveFilter == null || saveFilter.filter(key, model)) {
            if (ignoreSaveError) {
                try {
                    save(key, model);
                } catch (final Exception e) {
                    LOG.error(null, e);
                }
            } else {
                save(key, model);
            }
        }
        return model;
    }

    @Override
    public T findByKey(final K key) {
        return findByKey(key, false);
    }

    @Override
    public T findByKey(final K key, final boolean cacheOnly) {
        if (key == null) {
            return null;
        }
        T model = null;
        // 从缓存中查找
        try {
            model = findFromCache(key);
        } catch (final Exception e) {
            LOG.error(null, e);
        }
        if (model != null) {
            return model;
        }
        if (cacheOnly) {
            return null;
        }
        try {
            return findAndSave(key, false);
        } catch (final Exception e) {
        }
        return null;
    }

    @Override
    public Map<K, T> findByKeys(final Collection<K> keys, final boolean cacheOnly) throws KvCacheException {
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<K, T> map = null;
        try {
            map = findFromCache(keys);
        } catch (final Exception e) {
            LOG.error(null, e);
            map = Collections.emptyMap();
        }
        if (cacheOnly || map.size() == keys.size()) {
            return map;
        }
        final List<K> queryKeys = new ArrayList<>(Math.max(keys.size() - map.size(), 1));
        for (final K key : keys) {
            if (!map.containsKey(key)) {
                queryKeys.add(key);
            }
        }
        if (queryKeys.isEmpty()) {
            LOG.warn("queryKeys is empty, should not happened!!!");
            return map;
        }
        // 到repository中查找
        Map<K, T> freshMap;
        try {
            freshMap = repository.findByKeys(queryKeys);
        } catch (final Exception e) {
            throw new KvCacheException(e);
        }
        if (freshMap.isEmpty()) {
            return map;
        }
        // 合并缓存结果和新查数据结果
        map.putAll(freshMap);
        // 保存新加入缓存的数据
        Map<K, T> saveItems;
        if (saveFilter == null) {
            saveItems = freshMap;
        } else {
            saveItems = new HashMap<>();
            for (final Entry<K, T> entry : freshMap.entrySet()) {
                final K key = entry.getKey();
                final T value = entry.getValue();
                if (saveFilter.filter(key, value)) {
                    saveItems.put(key, value);
                }
            }
        }
        saveMany(saveItems);
        // 返回
        return map;
    }

    @Override
    public Map<K, T> findByKeys(final Collection<K> keys) throws KvCacheException {
        return findByKeys(keys, false);
    }

    @Override
    public void remove(final K key) throws KvCacheException {
        final String fullKey = makeFullKey(key);
        executor.remove(fullKey);
        if (INFO_ENABLED) {
            LOG.info("[KvCache] remove <{}>", fullKey);
        }
    }

    @Override
    public void flush() throws KvCacheException {
        final String prefix = this.policy.getPrefix() + "*";
        System.out.println("========================================");
        System.out.println(prefix);

        this.executor.batchRemove(prefix);
        if (INFO_ENABLED) {
            LOG.info("[KvCache] remove <{}>", prefix);
        }
    }

    @Override
    public Map<K, T> findLike(String pattern) {
        return handPattern(pattern, false);
    }

    protected Map<K, T> handPattern(String pattern, Boolean global) {
        if (!global) {
            pattern = subLastColon(pattern);
            pattern = policy.getPrefix() + pattern;
        }

        Set<String> keys = this.executor.findLike(pattern);

        if (!global) {
            keys = keys.stream().map(key -> subLastColon(key)).collect(Collectors.toSet());
        }

        if (CollectionUtils.isEmpty(keys)) {
            throw new DataNotFoundServiceException();
        }

        return findFromCache((Collection<K>) keys);
    }

    private String subLastColon(String str) {
        if (StringUtils.isEmpty(str)) {
            throw new ArgumentServiceException("pattern is empty");
        }

        return str.contains(":") ? str.substring(str.lastIndexOf(":") + 1, str.length()) : str;
    }
}
