package com.peas.xinrui.common.kvCache.converter;

import com.sunnysuperman.commons.util.ByteUtil;
import com.peas.xinrui.common.kvCache.KvCacheException;

public class LongModelConverter implements ModelConverter<Long> {
    private static final LongModelConverter INSTANCE = new LongModelConverter();

    private LongModelConverter() {

    }

    public static final LongModelConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Long deserialize(byte[] value) throws KvCacheException {
        return ByteUtil.toLong(value);
    }

    @Override
    public byte[] serialize(Long model) throws KvCacheException {
        return ByteUtil.fromLong(model);
    }
}
