package com.peas.xinrui.api.school.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.peas.xinrui.api.course.converter.AuthCoursesConverter;
import com.peas.xinrui.api.course.entity.AuthCourse;
import com.peas.xinrui.api.schadmin.model.SchAdmin;
import com.peas.xinrui.api.school.converter.LocationConverter;
import com.peas.xinrui.api.school.converter.OemConverter;
import com.peas.xinrui.api.school.entity.Location;
import com.peas.xinrui.api.school.entity.Oem;

@Entity
@Table(name = "school")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String director;
    private String logo;

    private String mobile;

    private Byte status;

    @Convert(converter = LocationConverter.class)
    private Location location;

    @Convert(converter = OemConverter.class)
    private Oem oem;

    private Long validThru;

    @Convert(converter = AuthCoursesConverter.class)
    private List<AuthCourse> authCourses;

    @Column(updatable = false)
    private Long createdAt;

    @Transient
    private List<SchAdmin> admins;

    public Integer getId() {
        return id;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Oem getOem() {
        return oem;
    }

    public void setOem(Oem oem) {
        this.oem = oem;
    }

    public Long getValidThru() {
        return validThru;
    }

    public void setValidThru(Long validThru) {
        this.validThru = validThru;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setAuthCourses(List<AuthCourse> authCourses) {
        this.authCourses = authCourses;
    }

    public List<AuthCourse> getAuthCourses() {
        return authCourses;
    }

    public List<SchAdmin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<SchAdmin> admins) {
        this.admins = admins;
    }
}