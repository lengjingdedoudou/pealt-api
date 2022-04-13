package com.peas.xinrui.api.user.entity;

public enum UserState {
    Disable(0), Enable(1);

    private byte state;

    UserState(int state) {
        this.state = (byte) state;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }
}