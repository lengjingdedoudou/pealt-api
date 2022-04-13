package com.peas.xinrui.api.trade.qo;

import java.util.List;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

public class CartQo extends DataQueryObjectPage {

    @QueryField(type = QueryType.EQUAL, name = "schoolId")
    private Integer schoolId;

    @QueryField(type = QueryType.EQUAL, name = "userId")
    private Long userId;

    @QueryField(type = QueryType.IN, name = "id")
    private List<Long> ids;

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}