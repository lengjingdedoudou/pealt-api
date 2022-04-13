package com.peas.xinrui.api.course.qo;

import java.util.List;

import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.repository.support.QueryField;
import com.peas.xinrui.common.repository.support.QueryType;

public class LessonQo extends DataQueryObjectPage {
    @QueryField(type = QueryType.EQUAL, name = "chapterId")
    private Long chapterId;

    @QueryField(type = QueryType.EQUAL, name = "courseId")
    private String courseId;

    @QueryField(type = QueryType.IN, name = "id")
    private List<Long> ids;

    public LessonQo(List<Long> ids) {
        this.ids = ids;
    }

    public LessonQo() {
    };

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

}
