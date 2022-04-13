package com.peas.xinrui.api.course.entity;

public class CourseWO {

    private boolean isWithCoursePkg;

    private boolean isWithTrainer;

    private boolean isWithCategory;

    private boolean isWithCourse;

    private boolean isWithChapter;

    private boolean isList;

    private boolean isDetail;

    public static CourseWO getNonInstance() {
        return new CourseWO().setWithCourse(true);
    }

    public static CourseWO getCoursePkgListInstance() {
        return getNonInstance().setWithCoursePkg(true);
    }

    public static CourseWO getTrainerInstance() {
        return getNonInstance().setWithTrainer(true);
    }

    public static CourseWO getAllInstance() {
        return getNonInstance().setWithTrainer(true).setWithCoursePkg(true).setWithCategory(true).setWithChapter(true);
    }

    public CourseWO setWithCoursePkg(boolean isWithCoursePkg) {
        this.isWithCoursePkg = isWithCoursePkg;
        return this;
    }

    public CourseWO setWithTrainer(boolean isWithTrainer) {
        this.isWithTrainer = isWithTrainer;
        return this;
    }

    public CourseWO setWithCategory(boolean isWithCategory) {
        this.isWithCategory = isWithCategory;
        return this;
    }

    public CourseWO setWithCourse(boolean isWithCourse) {
        this.isWithCourse = isWithCourse;
        return this;
    }

    public CourseWO setWithChapter(boolean isWithChapter) {
        this.isWithChapter = isWithChapter;
        return this;
    }

    public CourseWO setList(boolean isList) {
        this.isList = isList;
        return this;
    }

    public CourseWO setDetail(boolean isDetail) {
        this.isDetail = isDetail;
        return this;
    }

    public boolean isWithCoursePkg() {
        return isWithCoursePkg;
    }

    public boolean isWithCategory() {
        return isWithCategory;
    }

    public boolean isWithTrainer() {
        return isWithTrainer;
    }

    public boolean isWithCourse() {
        return isWithCourse;
    }

    public boolean isWithChapter() {
        return isWithChapter;
    }

    public boolean isList() {
        return isList;
    }

    public boolean isDetail() {
        return isDetail;
    }
}
