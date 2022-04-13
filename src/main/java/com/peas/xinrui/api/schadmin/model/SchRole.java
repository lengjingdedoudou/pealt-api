package com.peas.xinrui.api.schadmin.model;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.peas.xinrui.common.converter.StringArrayConverter;
import com.peas.xinrui.common.model.Permission;

@Entity
@Table(name = "sch_role")
public class SchRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Convert(converter = StringArrayConverter.class)
    private List<String> permissions;

    private Integer schoolId;

    @Transient
    private List<Permission> detailPermissions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getDetailPermissions() {
        return detailPermissions;
    }

    public void setDetailPermissions(List<Permission> detailPermissions) {
        this.detailPermissions = detailPermissions;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

}