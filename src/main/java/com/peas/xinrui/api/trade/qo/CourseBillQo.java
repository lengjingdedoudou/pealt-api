package com.peas.xinrui.api.trade.qo;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryBetween;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;
import com.peas.xinrui.common.utils.StringUtils;

public class CourseBillQo extends DataQueryObjectPage {

    @QueryField(type = QueryType.EQUAL, name = "schoolId")
    private Integer schoolId;

    @QueryField(type = QueryType.EQUAL, name = "status")
    private Byte status;

    @QueryField(type = QueryType.EQUAL, name = "userId")
    private Integer userId;

    @QueryField(type = QueryType.EQUAL, name = "payNumber")
    private String payNumber;

    @QueryField(type = QueryType.BEWTEEN, name = "createdAt")
    private QueryBetween<Long> time;

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(String payNumber) {
        this.payNumber = StringUtils.isEmpty(payNumber) ? null : payNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public QueryBetween<Long> getTime() {
        return time;
    }

    public void setTime(QueryBetween<Long> time) {
        this.time = time;
    }

}