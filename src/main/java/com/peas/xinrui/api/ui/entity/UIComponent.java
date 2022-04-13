package com.peas.xinrui.api.ui.entity;

import java.util.List;

public class UIComponent {

    private String key;
    private String title;
    private List<?> list;
    private Byte listStyle;
    private String contentSortPropertyName;// 商家的商品排序规则 pubAt salesCount priority
    private String placeholder; // 搜索框提示文字
    private Boolean withTitle;
    private Boolean withList;

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

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public Byte getListStyle() {
        return listStyle;
    }

    public void setListStyle(Byte listStyle) {
        this.listStyle = listStyle;
    }

    public String getContentSortPropertyName() {
        return contentSortPropertyName;
    }

    public void setContentSortPropertyName(String contentSortPropertyName) {
        this.contentSortPropertyName = contentSortPropertyName;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
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
}
