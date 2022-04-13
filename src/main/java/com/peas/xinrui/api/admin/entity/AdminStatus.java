package com.peas.xinrui.api.admin.entity;

public enum AdminStatus {

    Disable(0), Enable(1);

    private byte state;

    AdminStatus(int state) {
        this.state = (byte) state;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }
}