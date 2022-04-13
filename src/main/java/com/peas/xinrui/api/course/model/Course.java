package com.peas.xinrui.api.course.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.peas.xinrui.api.course.converter.CoursewareConverter;
import com.peas.xinrui.api.course.entity.Courseware;
import com.peas.xinrui.api.trainer.model.Trainer;
import com.peas.xinrui.common.converter.StringArrayConverter;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = StringArrayConverter.class)
    private List<String> imgs;

    @Column(name = "is_free")
    private Byte free;

    private String content;

    private String name;

    private Integer priority;

    @Convert(converter = CoursewareConverter.class)
    private List<Courseware> coursewares;

    private Integer sectionNum;

    private Integer chapterNum;

    private String descr;

    private Byte type;

    private Integer classHour;

    @Transient
    private List<CoursePkg> coursePkgs;

    @Transient
    private Boolean isCollected;

    @Transient
    private Boolean isBuying;

    private Byte status;

    @Column(updatable = false)
    private Long createdAt;

    @Transient
    private Trainer trainer;

    @Transient
    private List<Chapter> chapters;

    @Transient
    private Long lowPrice;

    @Transient
    private Byte canApply;

    public Course baseCourse() {
        Course course = new Course();
        course.setChapterNum(chapterNum);
        course.setSectionNum(sectionNum);
        course.setClassHour(classHour);
        course.setCreatedAt(createdAt);

        return course;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public Byte getFree() {
        return free;
    }

    public void setFree(Byte free) {
        this.free = free;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(Integer sectionNum) {
        this.sectionNum = sectionNum;
    }

    public Integer getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(Integer chapterNum) {
        this.chapterNum = chapterNum;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Integer getClassHour() {
        return classHour;
    }

    public void setClassHour(Integer classHour) {
        this.classHour = classHour;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public List<Courseware> getCoursewares() {
        return coursewares;
    }

    public void setCoursewares(List<Courseware> coursewares) {
        this.coursewares = coursewares;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public List<CoursePkg> getCoursePkgs() {
        return coursePkgs;
    }

    public void setCoursePkgs(List<CoursePkg> coursePkgs) {
        this.coursePkgs = coursePkgs;
    }

    public Long getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Long lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Course(List<String> imgs, Byte free, String content, String name, Integer priority,
            List<Courseware> coursewares, Integer sectionNum, Integer chapterNum, String descr, Byte type,
            Integer classHour, List<CoursePkg> coursePkgs, Byte status, Long createdAt, List<Chapter> chapters,
            Long lowPrice) {
        this.imgs = imgs;
        this.free = free;
        this.content = content;
        this.name = name;
        this.priority = priority;
        this.coursewares = coursewares;
        this.sectionNum = sectionNum;
        this.chapterNum = chapterNum;
        this.descr = descr;
        this.type = type;
        this.classHour = classHour;
        this.coursePkgs = coursePkgs;
        this.status = status;
        this.createdAt = createdAt;
        this.chapters = chapters;
        this.lowPrice = lowPrice;
    }

    public Course() {
    }

    public Boolean getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(Boolean isCollected) {
        this.isCollected = isCollected;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Boolean getIsBuying() {
        return isBuying;
    }

    public void setIsBuying(Boolean isBuying) {
        this.isBuying = isBuying;
    }

    public Byte getCanApply() {
        return canApply;
    }

    public void setCanApply(Byte canApply) {
        this.canApply = canApply;
    }

}