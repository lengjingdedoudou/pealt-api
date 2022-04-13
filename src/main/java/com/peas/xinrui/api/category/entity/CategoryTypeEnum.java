package com.peas.xinrui.api.category.entity;

public enum CategoryTypeEnum {
    Trainer(2), Course(1);

    private byte val;

    CategoryTypeEnum(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }

    public void setVal(byte val) {
        this.val = val;
    }

}