package com.peas.xinrui.api.renew.qo;

import java.util.List;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryBetween;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

public class RenewQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.EQUAL, name = "status")
    private Integer status;
    @QueryField(type = QueryType.EQUAL, name = "payType")
    private Integer payType;
    @QueryField(type = QueryType.BEWTEEN, name = "createdAt")
    private QueryBetween<Long> createdAtQo;
    @QueryField(type = QueryType.IN, name = "schoolId")
    private List<Integer> schoolIds;

    public QueryBetween<Long> getCreatedAtQo() {
        return createdAtQo;
    }

    public void setCreatedAtQo(QueryBetween<Long> createdAtQo) {
        this.createdAtQo = createdAtQo;
    }

    public Integer getStatus() {
        return status == 0 ? null : status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayType() {
        return payType == 0 ? null : payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<Integer> getSchoolIds() {
        return schoolIds;
    }

    public void setSchoolIds(List<Integer> schoolIds) {
        this.schoolIds = schoolIds;
    }

}