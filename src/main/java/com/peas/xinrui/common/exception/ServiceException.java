package com.peas.xinrui.common.exception;

import java.util.Map;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 8705784642583986316L;

    private int errorCode;

    private Object[] errorParams;

    private Map<?, ?> errorData;

    public ServiceException(int errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(int errorCode, Object... errorParams) {
        super();
        this.errorCode = errorCode;
        this.errorParams = errorParams;
    }

    public ServiceException(int errorCode, Object[] errorParams, Map<?, ?> errorData) {
        super();
        this.errorCode = errorCode;
        this.errorParams = errorParams;
        this.errorData = errorData;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Object[] getErrorParams() {
        return errorParams;
    }

    public void setErrorParams(Object[] errorParams) {
        this.errorParams = errorParams;
    }

    public Map<?, ?> getErrorData() {
        return errorData;
    }

    public void setErrorData(Map<?, ?> errorData) {
        this.errorData = errorData;
    }

}