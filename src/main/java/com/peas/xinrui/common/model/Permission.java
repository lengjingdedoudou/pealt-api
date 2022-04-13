package com.peas.xinrui.common.model;

public class Permission {
    private String key;
    private String label;
    private String level;

    public Permission(String key, String label, String level) {
        this.key = key;
        this.level = level;
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}