package com.peas.xinrui.common.kvCache.converter;

import com.peas.xinrui.common.kvCache.KvCacheException;

public class BooleanModelConverter implements ModelConverter<Boolean> {
    private static final BooleanModelConverter INSTANCE = new BooleanModelConverter();

    private BooleanModelConverter() {

    }

    public static final BooleanModelConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Boolean deserialize(byte[] value) throws KvCacheException {
        return value[0] > 0;
    }

    @Override
    public byte[] serialize(Boolean model) throws KvCacheException {
        byte b = (byte) (model.booleanValue() ? 1 : 0);
        return new byte[] { b };
    }

}
