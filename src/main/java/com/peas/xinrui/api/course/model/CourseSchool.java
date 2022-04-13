package com.peas.xinrui.api.course.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.peas.xinrui.api.category.model.Category;
import com.peas.xinrui.api.trainer.model.Trainer;

@Entity
@Table(name = "course_school")
public class CourseSchool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer schoolId;

    private Long courseId;

    @Transient
    private Course course;

    private Long categoryId;

    private Integer buyNum = 0;

    @Column(name = "is_free")
    private Byte free;

    private String name;

    private Integer priority;

    private Integer starNum = 0;

    private Byte status;

    private Long trainerId;

    private Integer visitNum = 0;

    private Byte type;

    @Transient
    private Trainer trainer;

    @Transient
    private Category category;

    @Transient
    private String cover;

    @Transient
    private Integer lowPrice;

    public CourseSchool() {
    }

    public CourseSchool(Integer schoolId, Long courseId, String name, Integer priority, Byte status, Byte free,
            Byte type, Long categoryId, Long trainerId) {
        this.schoolId = schoolId;
        this.courseId = courseId;
        this.name = name;
        this.priority = priority;
        this.status = status;
        this.free = free;
        this.type = type;
        this.categoryId = categoryId;
        this.trainerId = trainerId;
    }

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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId == null ? 0 : categoryId;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStarNum() {
        return starNum;
    }

    public void setStarNum(Integer starNum) {
        this.starNum = starNum;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId == null ? 0 : trainerId;
    }

    public Integer getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Byte getFree() {
        return free;
    }

    public void setFree(Byte free) {
        this.free = free;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Integer lowPrice) {
        this.lowPrice = lowPrice;
    }
}