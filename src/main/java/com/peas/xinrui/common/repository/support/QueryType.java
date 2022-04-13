package com.peas.xinrui.common.repository.support;

/**
 * 查询类型。所有类型的isCanBeNull都为false
 */
public enum QueryType {

    EQUAL(false), BEWTEEN(false), LESS_THAN(false), LESS_THAN_EQUAL(false), GREATEROR_THAN(false),
    GREATEROR_THAN_EQUAL(false), NOT_EQUAL(false), IS_NULL(true), IS_NOT_NULL(true), RIGHT_LIKE(false),
    LEFT_LIKE(false), FULL_LIKE(false), BATCH_FULL_LIKE(false), DEFAULT_LIKE(false), NOT_LIKE(false), IN(false);

    // 是否可以为空
    private boolean isCanBeNull;

    private QueryType(boolean isCanBeNull) {
        this.isCanBeNull = isCanBeNull;
    }

    public boolean isCanBeNull() {
        return this.isCanBeNull;
    }
}
