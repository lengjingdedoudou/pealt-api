package com.peas.xinrui.common.kvCache.converter;

import com.peas.xinrui.common.kvCache.KvCacheException;

public class ByteModelConverter implements ModelConverter<Byte> {
    private static final ByteModelConverter INSTANCE = new ByteModelConverter();

    private ByteModelConverter() {

    }

    public static final ByteModelConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public Byte deserialize(final byte[] value) throws KvCacheException {
        return value[0];
    }

    @Override
    public byte[] serialize(final Byte model) throws KvCacheException {
        return new byte[] { model };
    }
}
