package com.peas.xinrui.api.user.qo;

import java.util.Set;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;
import com.peas.xinrui.common.utils.StringUtils;

public class UserQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.IN, name = "id")
    private Set<Long> ids;

    @QueryField(type = QueryType.FULL_LIKE, name = { "name", "mobile" })
    private String nameOrMobile;

    @QueryField(type = QueryType.EQUAL, name = "identity")
    private String identity;

    @QueryField(type = QueryType.EQUAL, name = "schoolId")
    private Integer schoolId;

    @QueryField(type = QueryType.EQUAL, name = "salesmanId")
    private Integer salesmanId;

    public Set<Long> getIds() {
        return ids;
    }

    public void setIds(Set<Long> ids) {
        this.ids = ids;
    }

    public String getNameOrMobile() {
        return nameOrMobile;
    }

    public void setNameOrMobile(String nameOrMobile) {
        this.nameOrMobile = StringUtils.isEmpty(nameOrMobile) ? null : nameOrMobile;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = StringUtils.isEmpty(identity) ? null : identity;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(Integer salesmanId) {
        this.salesmanId = salesmanId;
    }

}