package com.peas.xinrui.api.coupon.enyity;

import com.peas.xinrui.api.category.model.Category;
import com.peas.xinrui.api.course.model.CourseSchool;

/**
 * Create by 李振威 2021/12/23 11:03
 */
public class Payload {

    private Byte type;

    private Category category;

    private CourseSchool courseSchool;

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

    public CourseSchool getCourseSchool() {
        return courseSchool;
    }

    public void setCourseSchool(CourseSchool courseSchool) {
        this.courseSchool = courseSchool;
    }
}
