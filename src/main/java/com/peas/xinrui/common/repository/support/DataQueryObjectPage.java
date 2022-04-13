package com.peas.xinrui.common.repository.support;

import com.sunnysuperman.commons.util.FormatUtil;

import com.peas.xinrui.common.model.Constants;

public class DataQueryObjectPage extends DataQueryObjectSort {

    protected Integer pageNumber = 1;
    protected Integer pageSize = Constants.PAGESIZE_MIN;

    public Integer getPageNumber() {
        int asInt = FormatUtil.parseIntValue(pageNumber, 0);
        return asInt <= 0 ? 0 : asInt - 1;
    }

    public void setPageNumber(Integer page) {
        this.pageNumber = page;
    }

    public Integer getPageSize() {
        int defaultValue = Constants.PAGESIZE_MIN;
        int maxValue = Constants.PAGESIZE_MAX;
        if (pageSize == null || pageSize <= 0) {
            return defaultValue;
        }
        if (pageSize > maxValue) {
            return Math.min(maxValue, pageSize);
        }
        return pageSize;
    }

    public void setPageSize(Integer size) {
        this.pageSize = size;
    }
}
