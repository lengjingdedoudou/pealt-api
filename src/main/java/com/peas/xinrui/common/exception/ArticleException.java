package com.peas.xinrui.common.exception;

public class ArticleException extends ServiceException {

    private static final long serialVersionUID = 7406626440721080410L;

    public ArticleException(int errorCode) {
        super(errorCode);
    }

    public ArticleException(int errorCode, Object... objects) {
        super(errorCode, objects);
    }
}