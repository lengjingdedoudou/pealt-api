package com.peas.xinrui.common.kvCache.converter;

import com.peas.xinrui.common.kvCache.KvCacheException;
import redis.clients.util.SafeEncoder;

public class IntegerStringModelConverter implements ModelConverter<Integer> {
    private static final IntegerStringModelConverter INSTANCE = new IntegerStringModelConverter();

    private IntegerStringModelConverter() {

    }

    public static final IntegerStringModelConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Integer deserialize(byte[] value) throws KvCacheException {
        return Integer.parseInt(SafeEncoder.encode(value));
    }

    @Override
    public byte[] serialize(Integer model) throws KvCacheException {
        return SafeEncoder.encode(model.toString());
    }
}
