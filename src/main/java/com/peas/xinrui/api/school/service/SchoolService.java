package com.peas.xinrui.api.school.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.peas.xinrui.api.admin.entity.AdminSessionWrapper;
import com.peas.xinrui.api.course.entity.AuthCourse;
import com.peas.xinrui.api.school.model.School;
import com.peas.xinrui.api.school.qo.SchoolQo;

import org.springframework.data.domain.Page;

public interface SchoolService {
    void saveSchool(School school);

    School school(Integer id);

    Page<School> schools(SchoolQo qo);

    School item(Integer id);

    List<Integer> schoolsSearch(String keyWord, Set<Integer> ids);

    Map<Integer, School> schools(Collection<Integer> ids);

    AdminSessionWrapper findByToken(String token);

    void schoolStatus(Integer id, Byte status);

    void renewSchool(Integer schoolId, String duration);

    int expireCount(Long time);

    Map<String, Object> oem(Integer schoolId);

    List<Long> authCourseIds(Integer schoolId);

    void updateAuthCourses(List<AuthCourse> authCourses, Integer schoolId);
}