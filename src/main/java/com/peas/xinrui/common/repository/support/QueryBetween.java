package com.peas.xinrui.common.repository.support;

public class QueryBetween<T extends Comparable<?>> {

    public T before;
    public T after;

    public QueryBetween() {
    }

    public QueryBetween(T before, T after) {
        this.before = before;
        this.after = after;
    }

    public T getBefore() {
        return before;
    }

    public void setBefore(T curTime) {
        this.before = curTime;
    }

    public T getAfter() {
        return after;
    }

    public void setAfter(T after) {
        this.after = after;
    }
}
