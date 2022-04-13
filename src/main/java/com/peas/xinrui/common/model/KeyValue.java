package com.peas.xinrui.common.model;

public class KeyValue {
    private Integer key;
    private String val;

    public KeyValue() {
    }

    public KeyValue(Integer key, String val) {
        this.setKey(key);
        this.setVal(val);
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
