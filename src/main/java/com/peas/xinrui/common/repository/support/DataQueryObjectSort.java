package com.peas.xinrui.common.repository.support;

public class DataQueryObjectSort implements DataQueryObject {

    protected String sortPropertyName = "id";
    protected boolean sortAscending = false;

    public String getSortPropertyName() {
        return sortPropertyName;
    }

    public void setSortPropertyName(String sortPropertyName) {
        this.sortPropertyName = sortPropertyName;
    }

    public boolean isSortAscending() {
        return sortAscending;
    }

    public void setSortAscending(boolean sortAscending) {
        this.sortAscending = sortAscending;
    }
}
