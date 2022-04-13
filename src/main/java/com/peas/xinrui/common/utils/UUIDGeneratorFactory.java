package com.peas.xinrui.common.utils;

import com.sunnysuperman.commons.model.ObjectId;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

public final class UUIDGeneratorFactory {

    public static class UUIDGenerator {
        private final AtomicInteger counter = new AtomicInteger(new SecureRandom().nextInt());

        public String generate() {
            return new ObjectId(counter).toHexString();
        }
    }

    public static UUIDGenerator create() {
        return new UUIDGenerator();
    }

}