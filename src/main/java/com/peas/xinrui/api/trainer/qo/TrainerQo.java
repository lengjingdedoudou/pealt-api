package com.peas.xinrui.api.trainer.qo;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

public class TrainerQo extends DataQueryObjectPage {

    @QueryField(type = QueryType.FULL_LIKE, name = "name")
    private String name;

    @QueryField(type = QueryType.EQUAL, name = "status")
    private Byte status;

    @QueryField(type = QueryType.EQUAL, name = "schoolId")
    private Integer schoolId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

}