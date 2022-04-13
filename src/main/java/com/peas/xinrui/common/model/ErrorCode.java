package com.peas.xinrui.common.model;

public interface ErrorCode {
    // common
    public static final int ERR_UNKNOWN_ERROR = 1;
    public static final int ERR_ILLEGAL_ARGUMENT = 2;
    public static final int ERR_PERMISSION_DENIED = 3;
    public static final int ERR_DETAILED_MESSAGE = 4;
    public static final int ERR_SESSION_EXPIRES = 5;
    public static final int ERR_OPERATION_TOO_FREQUENT = 6;
    public static final int ERR_DATA_NOT_FOUND = 7;
    public static final int ERR_VERSION_UPDATE = 8;
    public static final int ERR_ID_NULL = 9;
    public static final int ERR_OPERATION_NOT_SUPPORT = 10;
    public static final int ERR_OPERATION_ILLEGAL = 11;
    // 账号
    public static final int ERR_ACCOUNT_NOT_EXIST = 101;
    public static final int ERR_ACCOUNT_FORBID = 102;
    // 手机号
    public static final int ERR_MOBILE_INVALID = 110;
    public static final int ERR_MOBILE_EXIST = 111;
    public static final int ERR_MOBILE_NOT_FOUNT = 112;
    // 密码
    public static final int ERR_PASSWORD_INVALID = 120;
    public static final int ERR_PASSWORD_VALID_DENIED = 121;
    public static final int ERR_PASSWORD_INCONSISTENT = 122;
    public static final int ERR_WORN_PASSWORD_INCONSISTENT = 123;
    // 验证码
    public static final int ERR_MOBILE_NOT_VALCODE = 130;
    public static final int ERR_VALCODE = 131;
    public static final int ERR_VALCODE_NONE = 132;
    // 身份证号
    public static final int ERR_IDENTITY_INVALID = 140;
    public static final int ERR_IDENTITY_EXIST = 141;
    // 邮箱
    public static final int ERR_EMAIL_ERROR = 150;
    // file
    public static final int ERR_FILE_SIZE = 200;
    public static final int ERR_IMG_NEEDED = 201;
    public static final int ERR_FILE_NOT_FOUND = 202;

    // wx
    public static final int ERR_APIFACTORY_IS_NULL = 601;
    public static final int ERR_NEED_WX_APPID = 602;
    public static final int ERR_NEED_WX_CODE = 603;
    public static final int ERR_NEED_WX_IV = 604;
    public static final int ERROR_WX_AUTH_FAIL = 605;

    // room
    public static final int ERR_ROOM_STATUS = 300;

    // ui
    public static final int ERR_UI_TYPE_VALID_DENIED = 600;
    public static final int ERR_INPUT_CONTENT = 601;

    // merchant
    public static final int ERR_MERCHANT = 700;

    // merchantRecycler
    public static final int ERR_MERCHANTRECYCLER_SIGNIN = 800;

    // merchantUserRecycler
    public static final int ERR_RECYCLER_NOT_EXISTS = 900;

}
