package com.peas.xinrui.api.coupon.enyity;

/**
 * Create by 李振威 2021/12/23 11:04
 */
public enum CouponTypeEnum {

    CURRENTCY(1), CATEGORY(2), COURSE(3);

    private byte type;

    CouponTypeEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
