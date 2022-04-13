package com.peas.xinrui.api.trade.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.peas.xinrui.api.trade.converter.TradeItemConverter;
import com.peas.xinrui.api.trade.entity.TradeItem;
import com.peas.xinrui.api.user.model.User;

@Entity
@Table(name = "trade")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Convert(converter = TradeItemConverter.class)
    private List<TradeItem> items;

    private Integer schoolId;

    private Integer total;

    private Long couponId = 0L;

    private String tradeNumber;

    private Byte status;

    private Long paidAt;

    private Integer paidAmount;

    private Long endAt;

    @Column(updatable = false)
    private Long createdAt;

    @Transient
    private User user;

    @Transient
    private Integer unApprovedAmount;

    @Transient
    private Integer approvalFailedAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<TradeItem> getItems() {
        return items;
    }

    public void setItems(List<TradeItem> items) {
        this.items = items;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = tradeNumber;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getEndAt() {
        return endAt;
    }

    public void setEndAt(Long endAt) {
        this.endAt = endAt;
    }

    public Long getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Long paidAt) {
        this.paidAt = paidAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUnApprovedAmount() {
        return unApprovedAmount;
    }

    public void setUnApprovedAmount(Integer unApprovedAmount) {
        this.unApprovedAmount = unApprovedAmount;
    }

    public Integer getApprovalFailedAmount() {
        return approvalFailedAmount;
    }

    public void setApprovalFailedAmount(Integer approvalFailedAmount) {
        this.approvalFailedAmount = approvalFailedAmount;
    }

}