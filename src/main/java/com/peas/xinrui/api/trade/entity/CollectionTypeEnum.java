package com.peas.xinrui.api.trade.entity;

public enum CollectionTypeEnum {
    BANK(1), ALIPAY(2), WEIXIN(3);

    private byte val;

    CollectionTypeEnum(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }

    public void setVal(byte val) {
        this.val = val;
    }

}