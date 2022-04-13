package com.peas.xinrui.common.cache.kvGlobal;

import java.util.Map;

import com.peas.xinrui.common.cache.KvCacheWrap;
import com.peas.xinrui.common.kvCache.KvCacheExecutor;
import com.peas.xinrui.common.kvCache.KvCachePolicy;
import com.peas.xinrui.common.kvCache.KvCacheSaveFilter;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.ModelConverter;
import com.peas.xinrui.common.task.TaskService;

public class KvGlobalSearchWrapper<K, V> extends KvCacheWrap<K, V> implements KvGlobalSearchCache {
    public KvGlobalSearchWrapper(KvCacheExecutor executor, KvCachePolicy policy, RepositoryProvider<K, V> repository,
            ModelConverter<V> converter, KvCacheSaveFilter<K, V> saveFilter, TaskService taskService) {
        super(executor, policy, repository, converter, saveFilter, taskService);
    }

    @Override
    public Map<K, V> findLike(String pattern) {
        return this.handPattern(pattern, true);
    }
}