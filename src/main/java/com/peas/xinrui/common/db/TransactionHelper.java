package com.peas.xinrui.common.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.peas.xinrui.common.model.TransactionWork;

@Component
public class TransactionHelper {
    @Autowired
    @Qualifier("transactionTemplate")
    private TransactionTemplate transactionTemplate;

    private static class MyTransactionCallback<T> implements TransactionCallback<T> {
        private TransactionWork<T> work;
        private Throwable exception;

        public MyTransactionCallback(TransactionWork<T> work) {
            super();
            this.work = work;
        }

        @Override
        public T doInTransaction(TransactionStatus status) {
            try {
                return work.doInTransaction();
            } catch (Throwable t) {
                exception = t;
                status.setRollbackOnly();
                return null;
            }
        }

        public Throwable getException() {
            return exception;
        }

    }

    public <T> T doTransactionWork(TransactionWork<T> work) throws Exception {
        MyTransactionCallback<T> callback = new MyTransactionCallback<T>(work);
        T result = transactionTemplate.execute(callback);
        Throwable exception = callback.getException();
        if (exception != null) {
            if (exception instanceof Exception) {
                throw (Exception) exception;
            } else {
                throw new RuntimeException(exception);
            }
        }
        return result;
    }

}
