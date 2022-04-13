package com.peas.xinrui.api.trainer.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.peas.xinrui.api.category.model.Category;
import com.peas.xinrui.api.course.model.CourseSchool;

@Entity
@Table(name = "trainer")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer schoolId;

    private Long categoryId;

    @Transient
    private String categoryName;

    private String title;

    private String mobile;

    private Integer fans;

    private String intro;

    private String avatar;

    private String homeCover;

    private String introDetail;

    private Byte status;

    @Transient
    private Boolean isFollow;

    @Transient
    private Boolean isLike;

    @Transient
    private Category category;

    @Transient
    private List<CourseSchool> courseSchools;

    @Column(updatable = false)
    private Long createdAt;

    public Trainer baseTrainer() {
        Trainer trainer = new Trainer();
        trainer.setName(name);
        trainer.setAvatar(avatar);
        trainer.setId(id);

        return trainer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHomeCover() {
        return homeCover;
    }

    public void setHomeCover(String homeCover) {
        this.homeCover = homeCover;
    }

    public String getIntroDetail() {
        return introDetail;
    }

    public void setIntroDetail(String introDetail) {
        this.introDetail = introDetail;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<CourseSchool> getCourseSchools() {
        return courseSchools;
    }

    public void setCourseSchools(List<CourseSchool> courseSchools) {
        this.courseSchools = courseSchools;
    }

    public Boolean getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(Boolean isFollow) {
        this.isFollow = isFollow;
    }

}