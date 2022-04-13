package com.peas.xinrui.common.exception;

import java.util.HashMap;
import java.util.Map;

import com.peas.xinrui.common.model.ErrorCode;

public class DataNotFoundServiceException extends ServiceException {
    private static final long serialVersionUID = 7404151147635683478L;

    public DataNotFoundServiceException() {
        super(ErrorCode.ERR_DATA_NOT_FOUND);
    }

    public DataNotFoundServiceException(String message) {
        super(ErrorCode.ERR_DATA_NOT_FOUND);
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("message", message);
        setErrorData(errorData);
    }

}
