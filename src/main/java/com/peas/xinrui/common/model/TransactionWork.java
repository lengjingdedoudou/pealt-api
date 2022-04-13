package com.peas.xinrui.common.model;

public interface TransactionWork<T> {
    T doInTransaction() throws Exception;
}
