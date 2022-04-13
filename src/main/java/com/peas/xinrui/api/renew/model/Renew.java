package com.peas.xinrui.api.renew.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.peas.xinrui.api.admin.model.Admin;
import com.peas.xinrui.api.school.model.School;

@Entity
@Table(name = "renew")
public class Renew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer schoolId;

    private Byte status;
    private Long salemanId;

    private Long auditId;

    private String duration;
    private Byte free;

    private Integer amount;
    private Byte payType;
    private String payNumber;
    private String voucher;
    private String remark;
    private String rejectReason;
    @Column(updatable = false)
    private Long createdAt;
    private Long auditAt;

    @Transient
    private School school;

    @Transient
    private Admin saleman;

    @Transient
    private Admin auditor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getSalemanId() {
        return salemanId;
    }

    public void setSalemanId(Long salemanId) {
        this.salemanId = salemanId;
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Byte getFree() {
        return free;
    }

    public void setFree(Byte free) {
        this.free = free;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = payNumber;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getAuditAt() {
        return auditAt;
    }

    public void setAuditAt(Long auditAt) {
        this.auditAt = auditAt;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Admin getSaleman() {
        return saleman;
    }

    public void setSaleman(Admin saleman) {
        this.saleman = saleman;
    }

    public Admin getAuditor() {
        return auditor;
    }

    public void setAuditor(Admin auditor) {
        this.auditor = auditor;
    }
}