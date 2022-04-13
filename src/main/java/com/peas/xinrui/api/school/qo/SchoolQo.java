package com.peas.xinrui.api.school.qo;

import java.util.Set;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryBetween;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

public class SchoolQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.FULL_LIKE, name = "name")
    private String name;
    @QueryField(type = QueryType.EQUAL, name = "status")
    private Integer status;
    @QueryField(type = QueryType.LESS_THAN_EQUAL, name = "validThru")
    private QueryBetween<Long> time;
    @QueryField(type = QueryType.IN, name = "id")
    private Set<Integer> ids;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public QueryBetween<Long> getTime() {
        return time;
    }

    public void setTime(QueryBetween<Long> time) {
        this.time = time;
    }

    public Set<Integer> getIds() {
        return ids;
    }

    public void setIds(Set<Integer> ids) {
        this.ids = ids;
    }

}
