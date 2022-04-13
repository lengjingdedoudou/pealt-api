package com.peas.xinrui.api.bill.entity;

public enum ReferTypeEnum {
    SCHOOL_COURSE(3), SCHOOL_DUA(1);

    private byte val;

    ReferTypeEnum(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }

    public void setVal(byte val) {
        this.val = val;
    }

}