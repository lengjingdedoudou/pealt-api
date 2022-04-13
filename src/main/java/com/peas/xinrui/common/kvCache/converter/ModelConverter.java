package com.peas.xinrui.common.kvCache.converter;

import com.peas.xinrui.common.kvCache.KvCacheException;

public interface ModelConverter<T> {

    T deserialize(byte[] value) throws KvCacheException;

    byte[] serialize(T model) throws KvCacheException;

}
