package com.peas.xinrui.api.schadmin.entity;

public enum SchAdmStatus {

    Disable(0), Enable(1);

    private byte state;

    SchAdmStatus(int state) {
        this.state = (byte) state;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }
}