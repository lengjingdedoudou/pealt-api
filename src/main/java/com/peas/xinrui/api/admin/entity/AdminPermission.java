package com.peas.xinrui.api.admin.entity;

import com.peas.xinrui.common.model.Constants;

public enum AdminPermission {
    // none
    NONE("", ""),

    /* 功能模块 */
    // admin&role
    ROLE_LIST("管理组列表", Constants.LEVEL_IMPORTANT), ROLE_EDIT("管理组管理", Constants.LEVEL_IMPORTANT),
    ADMIN_LIST("管理员列表", Constants.LEVEL_WARNING), ADMIN_EDIT("编辑管理员", Constants.LEVEL_IMPORTANT),

    // school
    SCHOOL_LIST("学校列表", Constants.LEVEL_IMPORTANT), SCHOOL_EDIT("学校编辑", Constants.LEVEL_IMPORTANT),

    // renew
    SCHOOL_RENEW("学校续期", Constants.LEVEL_IMPORTANT), SCHOOL_BILL("学校账单", Constants.LEVEL_IMPORTANT),
    FINANCIAL_AUDIT("财务审核", Constants.LEVEL_IMPORTANT),

    // course
    COURSE_EDIT("课程编辑", Constants.LEVEL_IMPORTANT), COURSE_LIST("课程列表", Constants.LEVEL_IMPORTANT),
    COURSE_NUM("课程统计", Constants.LEVEL_IMPORTANT), QUESTION_EDIT("问题编辑", Constants.LEVEL_IMPORTANT),
    ACCOUNT_EDIT("账户编辑", Constants.LEVEL_IMPORTANT),;

    AdminPermission(String val, String level) {
        this.val = val;
        this.level = level;
    }

    private String val;
    private String level;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

}