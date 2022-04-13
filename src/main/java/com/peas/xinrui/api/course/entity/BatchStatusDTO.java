package com.peas.xinrui.api.course.entity;

import java.util.List;

public class BatchStatusDTO {

    private Byte status;

    private List<Long> ids;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

}