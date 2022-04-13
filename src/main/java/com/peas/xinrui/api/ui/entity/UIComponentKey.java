package com.peas.xinrui.api.ui.entity;

import java.util.List;

public enum UIComponentKey {

    AD(false, "广告位", false, null), BANNER(false, "轮播图", false, null), NAV(false, "二级导航", false, null),

    TOP(true, "排行榜", false, null),

    FREE(true, "免费课程", true, RequiredKey.freeCourseInstance), PAY(true, "付费课程", true, RequiredKey.payCourseInstance),
    TRAINER(true, "名师推荐", true, RequiredKey.trainerInstance), LIVE(true, "精彩直播", false, null),
    ARTICLE(true, "精彩资讯", false, null), NOTIFY(true, "热门资讯", false, null);

    private Boolean withTitle;
    private String title;
    private Boolean withList;
    private List<RequiredKey> requiredKeys;

    private UIComponentKey(Boolean withTitle, String title, Boolean withList, List<RequiredKey> requiredKeys) {
        this.withTitle = withTitle;
        this.title = title;
        this.withList = withList;
        this.requiredKeys = requiredKeys;
    }

    public Boolean getWithTitle() {
        return withTitle;
    }

    public void setWithTitle(Boolean withTitle) {
        this.withTitle = withTitle;
    }

    public Boolean getWithList() {
        return withList;
    }

    public void setWithList(Boolean withList) {
        this.withList = withList;
    }

    public List<RequiredKey> getRequiredKeys() {
        return requiredKeys;
    }

    public void setRequiredKeys(List<RequiredKey> requiredKeys) {
        this.requiredKeys = requiredKeys;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
