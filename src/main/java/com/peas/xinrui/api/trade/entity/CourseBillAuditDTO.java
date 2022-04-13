package com.peas.xinrui.api.trade.entity;

public class CourseBillAuditDTO {
    Long courseBillId;
    Byte status;
    String rejectReason;

    public Long getCourseBillId() {
        return courseBillId;
    }

    public void setCourseBillId(Long courseBillId) {
        this.courseBillId = courseBillId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}