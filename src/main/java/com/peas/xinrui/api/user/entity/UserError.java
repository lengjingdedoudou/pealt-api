package com.peas.xinrui.api.user.entity;

import com.peas.xinrui.common.model.ErrorCode;

public interface UserError extends ErrorCode {
    public static final int ERR_USER_NOT_FOUND = 1100;
    public static final int ERR_UESR_NAME_TOO_LONG = 1101;
    public static final int ERR_USER_NAME_EMPTY = 11002;
    public static final int ERR_USER_MOBILE_EMPTY = 1103;
    public static final int ERR_USER_PASSWORD_EMPTY = 1104;
    public static final int ERR_USER_NAME_EXISTS = 1105;
}