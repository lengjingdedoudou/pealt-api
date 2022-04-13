package com.peas.xinrui.api.user.entity;

public class Verify {
    private String idCardFront;
    private String idCardback;
    private String addressCode;

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getIdCardback() {
        return idCardback;
    }

    public void setIdCardback(String idCardback) {
        this.idCardback = idCardback;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

}