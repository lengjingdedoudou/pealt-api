package com.peas.xinrui.api.course.qo;

import java.util.List;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

import org.apache.commons.lang3.StringUtils;

public class CourseSchoolQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.FULL_LIKE, name = "name")
    private String name;

    @QueryField(type = QueryType.EQUAL, name = "categoryId")
    private Long categoryId;

    @QueryField(type = QueryType.EQUAL, name = "free")
    private Byte free;

    @QueryField(type = QueryType.IN, name = "courseId")
    private List<Long> courseIds;

    @QueryField(type = QueryType.IN, name = "status")
    private List<Byte> statusList;

    @QueryField(type = QueryType.EQUAL, name = "status")
    private Byte status;

    @QueryField(type = QueryType.EQUAL, name = "schoolId")
    private Integer schoolId;

    @QueryField(type = QueryType.EQUAL, name = "type")
    private Byte type;

    public String getName() {
        return StringUtils.isEmpty(name) ? null : name;
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

    public CourseSchoolQo(String sortType, Integer schoolId) {
        this.sortPropertyName = sortType;
        this.schoolId = schoolId;
    }

    public CourseSchoolQo() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId == 0 ? null : categoryId;
    }

    public Byte getFree() {
        return free;
    }

    public void setFree(Byte free) {
        this.free = free;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public List<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }

    public List<Byte> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Byte> statusList) {
        this.statusList = statusList;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

}
