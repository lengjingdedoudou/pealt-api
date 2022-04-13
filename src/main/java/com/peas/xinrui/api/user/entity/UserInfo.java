package com.peas.xinrui.api.user.entity;

import javax.persistence.Entity;

import com.peas.xinrui.common.utils.DateUtils;

@SuppressWarnings("all")
public class UserInfo {
    private Byte gender;
    private Long birthDate;
    private String constellation;

    private Byte profession;
    private Byte education;

    private String remark;

    private String avatar;

    private String descr;

    public Byte getGender() {
        return gender == null ? 1 : gender;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Long getBirthDate() {
        return birthDate == null ? 0 : birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    public String getConstellation() {
        if (birthDate == null) {
            return "未知";
        }
        return DateUtils.getBirthDateConstellation(birthDate);
    }

    public Byte getProfession() {
        return profession == null ? 0 : profession;
    }

    public void setProfession(Byte profession) {
        this.profession = profession;
    }

    public Byte getEducation() {
        return education;
    }

    public void setEducation(Byte education) {
        this.education = education;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

}