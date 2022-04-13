package com.peas.xinrui.api.trade.qo;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryBetween;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;
import com.peas.xinrui.common.utils.StringUtils;

public class TradeQo extends DataQueryObjectPage {

    @QueryField(type = QueryType.EQUAL, name = "status")
    private Byte status;

    @QueryField(type = QueryType.EQUAL, name = "tradeNumber")
    private String tradeNumber;

    @QueryField(type = QueryType.EQUAL, name = "schoolId")
    private Integer schoolId;

    @QueryField(type = QueryType.EQUAL, name = "userId")
    private Integer userId;

    @QueryField(type = QueryType.BEWTEEN, name = "createdAt")
    private QueryBetween<Long> createdAtQo;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public QueryBetween<Long> getCreatedAtQo() {
        return createdAtQo;
    }

    public void setCreatedAtQo(QueryBetween<Long> createdAtQo) {
        this.createdAtQo = createdAtQo;
    }

    public String getTradeNumber() {
        return tradeNumber;
    }

    public void setTradeNumber(String tradeNumber) {
        this.tradeNumber = StringUtils.isEmpty(tradeNumber) ? null : tradeNumber;
    }
}