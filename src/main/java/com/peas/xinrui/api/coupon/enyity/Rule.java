package com.peas.xinrui.api.coupon.enyity;

import java.util.List;

/**
 * Create by 李振威 2021/12/23 11:11
 */
public class Rule {

    private Byte type;

    private List<Integer> values;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }
}
