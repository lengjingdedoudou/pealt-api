package com.peas.xinrui.common.exception;

import com.peas.xinrui.common.model.ErrorCode;

public class DetailedServiceException extends ServiceException {

    private static final long serialVersionUID = -2140213924124009663L;

    public DetailedServiceException(String errorMsg) {
        super(ErrorCode.ERR_DETAILED_MESSAGE, errorMsg);
    }

}
