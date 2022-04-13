package com.peas.xinrui.common.exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.peas.xinrui.common.model.ErrorCode;

public class ArgumentServiceException extends ServiceException {
    private static final long serialVersionUID = 8659114628660349452L;

    public ArgumentServiceException(String key, Serializable value) {
        super(ErrorCode.ERR_ILLEGAL_ARGUMENT);
        Map<String, Object> errorData = new HashMap<String, Object>(2);
        errorData.put("key", key);
        errorData.put("value", value);
        this.setErrorData(errorData);
    }

    public ArgumentServiceException(String key) {
        this(key, null);
    }

    public ArgumentServiceException(int code) {
        super(code);
    }
}
