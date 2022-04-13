package com.peas.xinrui.api.trade.entity;

import com.peas.xinrui.api.course.model.CoursePkg;

public class TradeItem {
    private Integer price;

    private Long courseId;

    private String courseName;

    private String cover;

    private CoursePkg pkg;

    private Long cartId;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public CoursePkg getPkg() {
        return pkg;
    }

    public void setPkg(CoursePkg pkg) {
        this.pkg = pkg;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

}