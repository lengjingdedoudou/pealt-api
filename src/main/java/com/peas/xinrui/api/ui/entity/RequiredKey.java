package com.peas.xinrui.api.ui.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequiredKey {

    private String key;

    private List<RequiredKey> subKeys;

    public static List<RequiredKey> freeCourseInstance = new ArrayList<>();
    public static List<RequiredKey> payCourseInstance = new ArrayList<>();
    public static List<RequiredKey> trainerInstance = new ArrayList<>();

    static {
        trainerInstance = Arrays.asList(new RequiredKey("name"), new RequiredKey("homeCover"), new RequiredKey("fans"),
                new RequiredKey("categoryId"), new RequiredKey("id"));

        payCourseInstance = Arrays.asList(new RequiredKey("courseId"), new RequiredKey("name"),
                new RequiredKey("cover"), new RequiredKey("buyNum"),
                new RequiredKey("trainer", Arrays.asList(new RequiredKey("name"))), new RequiredKey("course"),
                new RequiredKey("lowPrice"), new RequiredKey("free"),
                new RequiredKey("category", Arrays.asList(new RequiredKey("color"), new RequiredKey("name"))));

        freeCourseInstance = Arrays.asList(new RequiredKey("courseId"), new RequiredKey("free"),
                new RequiredKey("cover"), new RequiredKey("name"), new RequiredKey("course"),
                new RequiredKey("buyNum"));
    }

    public RequiredKey(String key, List<RequiredKey> subKeys) {
        this.key = key;
        this.subKeys = subKeys;
    }

    public RequiredKey() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<RequiredKey> getSubKeys() {
        return subKeys;
    }

    public void setSubKeys(List<RequiredKey> subKeys) {
        this.subKeys = subKeys;
    }

    public RequiredKey(String key) {
        this.key = key;
    }

}