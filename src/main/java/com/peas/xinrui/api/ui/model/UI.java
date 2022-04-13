package com.peas.xinrui.api.ui.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.peas.xinrui.api.ui.converter.UIComponentArrayConverter;
import com.peas.xinrui.api.ui.entity.UIComponent;

@Entity
@Table
public class UI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "school_id")
    private Integer schoolId;

    @Column
    private String title;

    @Column
    private Byte type;

    @Column
    @Convert(converter = UIComponentArrayConverter.class)
    private List<UIComponent> components;

    @Column(updatable = false)
    private Long createdAt;

    @Column
    private Byte isDefault;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public List<UIComponent> getComponents() {
        return components;
    }

    public void setComponents(List<UIComponent> components) {
        this.components = components;
    }

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
    }
}
