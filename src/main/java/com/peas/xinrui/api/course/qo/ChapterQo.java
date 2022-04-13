package com.peas.xinrui.api.course.qo;

import java.util.List;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

public class ChapterQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.EQUAL, name = "courseId")
    private Long courseId;

    @QueryField(type = QueryType.IN, name = "courseId")
    private List<Long> courseIds;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }

}
