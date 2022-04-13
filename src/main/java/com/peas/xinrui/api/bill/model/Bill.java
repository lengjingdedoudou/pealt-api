package com.peas.xinrui.api.bill.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.peas.xinrui.api.school.model.School;

@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer amount;
    private Byte payType;
    private String payNumber;
    private Byte renferType;
    private Long renferId;
    @Column(updatable = false)
    private Long createdAt;
    @Transient
    private School school;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Byte getRenferType() {
        return renferType;
    }

    public void setRenferType(Byte renferType) {
        this.renferType = renferType;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getRenferId() {
        return renferId;
    }

    public void setRenferId(Long renferId) {
        this.renferId = renferId;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Bill(Integer amount, Byte payType, String payNumber, Byte renferType, Long renferId, Long createdAt) {
        this.amount = amount;
        this.payType = payType;
        this.payNumber = payNumber;
        this.renferType = renferType;
        this.renferId = renferId;
        this.createdAt = createdAt;
    }

    public Bill() {
    }

}