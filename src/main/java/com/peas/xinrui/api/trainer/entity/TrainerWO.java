package com.peas.xinrui.api.trainer.entity;

public class TrainerWO {
    private boolean isWithCourseList;

    private boolean isWithCategory;

    private boolean isUser;

    public static TrainerWO getNonInstance() {
        return new TrainerWO();
    }

    public static TrainerWO getCategoryInstance() {
        return new TrainerWO().setWithCategory(true);
    }

    public static TrainerWO getCourseListInstance() {
        return new TrainerWO().setWithCourseList(true);
    }

    public static TrainerWO getAllInstance() {
        return getNonInstance().setWithCategory(true).setWithCourseList(true);
    }

    public TrainerWO setUser(boolean isUser) {
        this.isUser = isUser;
        return this;
    }

    public TrainerWO setWithCategory(boolean isWithCategory) {
        this.isWithCategory = isWithCategory;
        return this;
    }

    public TrainerWO setWithCourseList(boolean isWithCourseList) {
        this.isWithCourseList = isWithCourseList;
        return this;
    }

    public boolean isWithCategory() {
        return isWithCategory;
    }

    public boolean isUser() {
        return isUser;
    }

    public boolean isWithCourseList() {
        return isWithCourseList;
    }
}
