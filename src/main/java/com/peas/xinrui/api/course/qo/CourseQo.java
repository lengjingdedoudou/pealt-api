package com.peas.xinrui.api.course.qo;

import java.util.List;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

public class CourseQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.FULL_LIKE, name = "name")
    private String name;

    @QueryField(type = QueryType.EQUAL, name = "type")
    private Byte type;

    @QueryField(type = QueryType.EQUAL, name = "status")
    private Byte status;

    @QueryField(type = QueryType.IN, name = "id")
    private List<Long> ids;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getStatus() {
        return status;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

}
