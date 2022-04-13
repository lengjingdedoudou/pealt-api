package com.peas.xinrui.api.bill.qo;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryBetween;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

public class BillQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.EQUAL, name = "payNumber")
    private Integer payNumber;
    @QueryField(type = QueryType.EQUAL, name = "payType")
    private Integer payType;
    @QueryField(type = QueryType.EQUAL, name = "referType")
    private Integer referType;
    @QueryField(type = QueryType.BEWTEEN, name = "createdAt")
    private QueryBetween<Long> createdAtQo;

    public QueryBetween<Long> getCreatedAtQo() {
        return createdAtQo;
    }

    public void setCreatedAtQo(QueryBetween<Long> createdAtQo) {
        this.createdAtQo = createdAtQo;
    }

    public Integer getPayType() {
        return payType == 0 ? null : payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(Integer payNumber) {
        this.payNumber = payNumber;
    }

    public Integer getReferType() {
        return referType;
    }

    public void setReferType(Integer referType) {
        this.referType = referType;
    }

}