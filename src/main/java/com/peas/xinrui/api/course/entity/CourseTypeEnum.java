package com.peas.xinrui.api.course.entity;

public enum CourseTypeEnum {
    Common(2), Official(1);

    private byte val;

    CourseTypeEnum(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }

    public void setVal(byte val) {
        this.val = val;
    }

}