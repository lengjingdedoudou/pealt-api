package com.peas.xinrui.api.trade.entity;

public class CartWO {
    private boolean isWithCoursePkg;

    private boolean isWithCourse;

    public static CartWO getNonInstance() {
        return new CartWO();
    }

    public static CartWO getAllInstance() {
        return getNonInstance().setWithCoursePkg(true).setWithCourse(true);
    }

    public CartWO setWithCoursePkg(boolean isWithCoursePkg) {
        this.isWithCoursePkg = isWithCoursePkg;
        return this;
    }

    public CartWO setWithCourse(boolean isWithCourse) {
        this.isWithCourse = isWithCourse;
        return this;
    }

    public boolean isWithCoursePkg() {
        return isWithCoursePkg;
    }

    public boolean isWithCourse() {
        return isWithCourse;
    }
}
