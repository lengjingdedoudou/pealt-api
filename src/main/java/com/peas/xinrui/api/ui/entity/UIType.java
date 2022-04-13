package com.peas.xinrui.api.ui.entity;

public enum UIType {

    WX_HOME((byte) 1), PC_HOME((byte) 2);// PROFILE(3);

    private Byte type;

    UIType(Byte type) {
        this.type = type;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
