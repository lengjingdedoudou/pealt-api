package com.peas.xinrui.common.model;

public class Constants {

    public static final byte INIT_NONE = 0;
    public static final int INIT_DEFAULT = 0;
    public static final byte STATUS_OK = 1;// 默认
    public static final byte STATUS_HALT = 2;// 删除、停用、取消

    public static final byte ACCOUNT_BANK = 1; // 银行卡
    public static final byte ACCOUNT_ZFB = 2; // 支付宝
    public static final byte ACCOUNT_WX = 3; // 微信

    public static final byte TRADE_NOT_PAY = 1; // 未支付
    public static final byte TRADE_PAIED = 2; // 支付
    public static final byte TRADE_AUDITED = 3; // 审核通过
    public static final byte TRADE_CANCEL = 4; // 取消
    public static final byte TRADE_DELETE = 5; // 作废

    public static final byte BILL_WEIT = 1; // 待审核
    public static final byte BILL_AUDIT_SUCCESS = 2; // 审核成功
    public static final byte BILL_AUDIT_FAIL = 3; // 审核失败

    // 一天的毫秒值
    public static int ONE_DAY_MS = 86400000;

    // 打点时长
    public static int FLOW_TIME = 10;

    // 题
    public static byte QUESTION_NORMAL = 1;
    public static byte QUESTION_REMOVE = 3;

    // 学校姓名
    public static int MERCHANT_NAME_LENGTH_MAX = 20;

    // bill 类型
    public static final byte BILL_OUT = 1;// 支出
    public static final byte BILL_IN = 2;// 充值 or 收入
    // paid 类型
    public static final byte PAID_OVER = 1;// 完成
    public static final byte PAID_INIT = 2;// 默认未完成
    public static int PAGESIZE_MIN = 20;
    public static int PAGESIZE_MED = 20;
    public static int PAGESIZE_MAX = 50;
    public static int PAGESIZE_TOP = 1000;
    public static int PAGESIZE_INF = 10000;
    public static int CACHE_REDIS_EXPIRE = 3600 * 48;
    public static int SESSION_EXPIRE_DAYS = 2;
    // 权限操作级别
    public static String LEVEL_PRIMARY = "blue";
    public static String LEVEL_IMPORTANT = "red";
    public static String LEVEL_WARNING = "orange";

    // 文件
    public static int EXPIRE_UPLOAD = 60 * 60;
    public static int MAX_UPLOAD_SIZE = 200 * 1024 * 1024;

    // 被收藏的种类
    public static Byte TRAINER = 1;
    public static Byte ARTICLE = 2;
    public static Byte COURSE_STAR = 3;
    public static Byte QUESTION = 4;
    public static Byte LIVE = 5;

    // 被收藏的种类更新的种类
    public static Integer STAR_NUM_PLUS = 1;
    public static Integer STAR_NUM_MINUS = -1;

    // 学生性别
    public static final byte ARTISAN_MAN = 1;// 男
    public static final byte ARTISAN_WOMAN = 2;// 女

    // 机构是否默认
    public static final byte DEFAULT = 1;// 默认
    public static final byte NOT_DEFAULT = 2;

    // 优惠券类别
    public static final byte ALL = 1;// 通用
    public static final byte CATEGORY = 2;// 分类
    public static final byte COURSE_VIDEO = 3;// 单课录播
    public static final byte COURSE_LIVE = 4;// 单课直播

    // 试卷完成状态
    public static final byte UNFINISHED = 1;// 未完成
    public static final byte COMPLETED = 2;// 已完成

    // 学校端查询课程分类状态
    public static final String SCHOOL_STATUS_ALL = "0";
    public static final String SCHOOL_STATUS_OK = "1";

    // 用户类型
    public static Byte _ADMIN = 1;
    public static Byte _USER = 2;

    // 用户账号类型
    public static final Byte ACCOUNT_TYPE_MOBILE = 1;
    public static final Byte ACCOUNT_TYPE_EMAIL = 2;

    public final static byte BILL_TYPE_RENEW = 1;
}
