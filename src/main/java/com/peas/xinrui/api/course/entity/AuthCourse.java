package com.peas.xinrui.api.course.entity;

import java.util.List;

import com.peas.xinrui.api.course.model.Course;
import com.peas.xinrui.api.course.model.CoursePkg;

public class AuthCourse {

    private Long courseId;

    private List<Long> pkgIds;

    private Course course;

    private List<CoursePkg> coursePkgs;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<CoursePkg> getCoursePkgs() {
        return coursePkgs;
    }

    public void setCoursePkgs(List<CoursePkg> coursePkgs) {
        this.coursePkgs = coursePkgs;
    }

    public List<Long> getPkgIds() {
        return pkgIds;
    }

    public void setPkgIds(List<Long> pkgIds) {
        this.pkgIds = pkgIds;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}