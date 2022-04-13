package com.peas.xinrui.common.cache;

import com.peas.xinrui.common.kvCache.RepositoryProvider;

public abstract class SingleRepositoryProvider<K, V> implements RepositoryProvider<K, V> {

    @Override
    public final java.util.Map<K, V> findByKeys(java.util.Collection<K> keys) throws Exception {
        throw new UnsupportedOperationException("findByKeys");
    }

}
