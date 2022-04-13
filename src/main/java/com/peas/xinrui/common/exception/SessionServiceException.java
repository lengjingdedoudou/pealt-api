package com.peas.xinrui.common.exception;

import com.peas.xinrui.common.model.ErrorCode;

public class SessionServiceException extends ServiceException {
    private static final long serialVersionUID = 8703530907578894942L;

    public SessionServiceException() {
        super(ErrorCode.ERR_SESSION_EXPIRES);
    }
}
