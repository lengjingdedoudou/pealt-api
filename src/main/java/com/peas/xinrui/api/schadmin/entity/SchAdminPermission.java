package com.peas.xinrui.api.schadmin.entity;

import com.peas.xinrui.common.model.Constants;

public enum SchAdminPermission {
    // none
    NONE("", ""),

    /* 功能模块 */
    // admin&role
    ROLE_LIST("管理组列表", Constants.LEVEL_IMPORTANT), ROLE_EDIT("管理组管理", Constants.LEVEL_IMPORTANT),
    ADMIN_LIST("管理员列表", Constants.LEVEL_WARNING), ADMIN_EDIT("编辑管理员", Constants.LEVEL_IMPORTANT),

    USER_LIST("用户列表", Constants.LEVEL_PRIMARY), USER_EDIT("编辑用户", Constants.LEVEL_PRIMARY),

    COURSE_LIST("课程列表", Constants.LEVEL_IMPORTANT), COURSE_EDIT("课程编辑", Constants.LEVEL_IMPORTANT),

    UI_EDIT("首页自定义", Constants.LEVEL_WARNING),

    TRADE_LIST("订单列表", Constants.LEVEL_WARNING), TRADE_CHECK("订单审核", Constants.LEVEL_IMPORTANT),
    TRADE_DROP("订单作废", Constants.LEVEL_IMPORTANT),

    COMMISSION_EDIT("佣金设置", Constants.LEVEL_IMPORTANT), ACCOUNT_EDIT("收款配置", Constants.LEVEL_IMPORTANT),
    COUNT_LIST("查看统计", Constants.LEVEL_IMPORTANT), BILL_LIST("查看账单", Constants.LEVEL_WARNING),
    BILL_CHECK("账单审核", Constants.LEVEL_IMPORTANT),

    CUSTOMER_LIST("客服设置", Constants.LEVEL_PRIMARY), SALESMAN_IDENTITY("业务员", Constants.LEVEL_PRIMARY),;

    SchAdminPermission(String val, String level) {
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