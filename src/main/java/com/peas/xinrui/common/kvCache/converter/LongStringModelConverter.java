package com.peas.xinrui.common.kvCache.converter;

import com.peas.xinrui.common.kvCache.KvCacheException;
import redis.clients.util.SafeEncoder;

public class LongStringModelConverter implements ModelConverter<Long> {
    private static final LongStringModelConverter INSTANCE = new LongStringModelConverter();

    private LongStringModelConverter() {

    }

    public static final LongStringModelConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Long deserialize(byte[] value) throws KvCacheException {
        return Long.parseLong(SafeEncoder.encode(value));
    }

    @Override
    public byte[] serialize(Long model) throws KvCacheException {
        return SafeEncoder.encode(model.toString());
    }
}
