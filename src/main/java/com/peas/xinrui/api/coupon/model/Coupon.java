package com.peas.xinrui.api.coupon.model;

import com.peas.xinrui.api.coupon.converter.PayloadConverter;
import com.peas.xinrui.api.coupon.converter.RuleConverter;
import com.peas.xinrui.api.coupon.enyity.Payload;
import com.peas.xinrui.api.coupon.enyity.Rule;

import javax.persistence.*;

/**
 * Create by 李振威 2021/12/23 11:02
 */
@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer schoolId;

    private Integer duration;

    private Byte status;

    @Convert(converter = PayloadConverter.class)
    private Payload payload;

    @Convert(converter = RuleConverter.class)
    private Rule rule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
