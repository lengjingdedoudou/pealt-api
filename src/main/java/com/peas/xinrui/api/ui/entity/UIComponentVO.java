package com.peas.xinrui.api.ui.entity;

import java.util.List;

public class UIComponentVO {
    private String key;
    private Boolean withTitle;
    private String title;
    private Boolean withList;
    private List<RequiredKey> requiredKeys;

    public UIComponentVO(String key, Boolean withTitle, String title, Boolean withList,
            List<RequiredKey> requiredKeys) {
        this.key = key;
        this.withTitle = withTitle;
        this.title = title;
        this.withList = withList;
        this.requiredKeys = requiredKeys;
    }

    public Boolean getWithTitle() {
        return withTitle;
    }

    public void setWithTitle(Boolean withTitle) {
        this.withTitle = withTitle;
    }

    public Boolean getWithList() {
        return withList;
    }

    public void setWithList(Boolean withList) {
        this.withList = withList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RequiredKey> getRequiredKeys() {
        return requiredKeys;
    }

    public void setRequiredKeys(List<RequiredKey> requiredKeys) {
        this.requiredKeys = requiredKeys;
    }

}