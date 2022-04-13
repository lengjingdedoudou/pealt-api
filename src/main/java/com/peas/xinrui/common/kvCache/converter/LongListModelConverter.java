package com.peas.xinrui.common.kvCache.converter;

import java.util.Collections;
import java.util.List;

import com.peas.xinrui.common.kvCache.KvCacheException;
import com.peas.xinrui.common.utils.ByteUtils;
import com.peas.xinrui.common.utils.StringUtils;

public class LongListModelConverter implements ModelConverter<List<Long>> {
    private static final LongListModelConverter INSTANCE = new LongListModelConverter();
    private static final byte[] EMPTY_BYTES = new byte[0];

    public static LongListModelConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Long> deserialize(byte[] bytes) throws KvCacheException {
        if (bytes == null || bytes.length == 0) {
            return Collections.emptyList();
        }
        // if (s.length() == 1 && s.equals(",")) {
        // return Collections.emptyList();
        // }
        return StringUtils.splitAsLongList(ByteUtils.toString(bytes), ",");
    }

    @Override
    public byte[] serialize(List<Long> list) throws KvCacheException {
        if (list == null || list.isEmpty()) {
            return EMPTY_BYTES;
        }
        // if (list.isEmpty()) {
        // return ByteUtils.fromString(",");
        // }
        return ByteUtils.fromString(StringUtils.join(list, ","));
    }

}
