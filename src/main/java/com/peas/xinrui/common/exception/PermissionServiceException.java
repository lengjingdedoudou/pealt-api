package com.peas.xinrui.common.exception;

import com.peas.xinrui.common.model.ErrorCode;

public class PermissionServiceException extends ServiceException {

    private static final long serialVersionUID = 3716329185472602376L;

    public PermissionServiceException() {
        super(ErrorCode.ERR_PERMISSION_DENIED);
    }
}
