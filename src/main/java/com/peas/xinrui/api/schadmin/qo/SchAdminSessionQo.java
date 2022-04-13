package com.peas.xinrui.api.schadmin.qo;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

public class SchAdminSessionQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.EQUAL, name = "schAdminId")
    private Integer adminId;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

}