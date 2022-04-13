package com.peas.xinrui.api.coupon.enyity;

/**
 * Create by 李振威 2021/12/23 11:11
 */
public enum RuleTypeEnum {

    FULLMINUS(1), EVERYMINUS(2), DIRECTREDUCTION(3);

    private byte type;

    RuleTypeEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
