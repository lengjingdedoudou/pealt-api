package com.peas.xinrui.api.admin.model;

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
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Convert(converter = StringArrayConverter.class)
    private List<String> permissions;

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

}