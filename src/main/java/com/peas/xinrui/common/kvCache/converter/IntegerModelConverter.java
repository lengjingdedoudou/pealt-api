package com.peas.xinrui.common.kvCache.converter;

import com.sunnysuperman.commons.util.ByteUtil;

import com.peas.xinrui.common.kvCache.KvCacheException;

public class IntegerModelConverter implements ModelConverter<Integer> {
    private static final IntegerModelConverter INSTANCE = new IntegerModelConverter();

    private IntegerModelConverter() {

    }

    public static final IntegerModelConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Integer deserialize(byte[] value) throws KvCacheException {
        return ByteUtil.toInt(value);
    }

    @Override
    public byte[] serialize(Integer model) throws KvCacheException {
        return ByteUtil.fromInt(model);
    }
}
