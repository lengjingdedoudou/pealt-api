package com.peas.xinrui.common.utils;

import com.sunnysuperman.commons.model.ObjectId;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

public final class UUIDCreatorFactory {

    private static final UUIDCreator DEFAULT_CREATOR = UUIDCreatorFactory.get();

    public static UUIDCreator get() {
        return new UUIDCreator();
    }

    public static UUIDCreator getDefault() {
        return DEFAULT_CREATOR;
    }

    public static class UUIDCreator {
        private final AtomicInteger counter = new AtomicInteger(new SecureRandom().nextInt());

        public String create() {
            return new ObjectId(counter).toHexString();
        }
    }

}
