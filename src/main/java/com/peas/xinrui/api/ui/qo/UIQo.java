package com.peas.xinrui.api.ui.qo;

import com.peas.xinrui.common.repository.support.DataQueryObjectSort;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

public class UIQo extends DataQueryObjectSort {

    @QueryField(type = QueryType.EQUAL, name = "type")
    private Byte type;
    @QueryField(type = QueryType.EQUAL, name = "schoolId")
    private Integer schoolId;

    public UIQo() {
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }
}
