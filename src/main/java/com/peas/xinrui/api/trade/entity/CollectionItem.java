package com.peas.xinrui.api.trade.entity;

import javax.persistence.Convert;

import com.peas.xinrui.api.trade.converter.BankInfoConverter;

public class CollectionItem {
    private Byte type;

    private String img;

    @Convert(converter = BankInfoConverter.class)
    private BankInfo bankInfo;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public BankInfo getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }
}