package com.peas.xinrui.api.course.repository;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import com.peas.xinrui.api.course.model.CourseSchool;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourseSchoolRepository extends BaseRepository<CourseSchool, Long> {
    List<CourseSchool> findByIdIn(Collection<Long> ids);

    CourseSchool findBySchoolIdAndCourseId(Integer schoolId, Long CourseId);

    @Query("select courseId from CourseSchool where schoolId = :schoolId")
    List<Long> findCourseIdsBySchoolId(Integer schoolId);

    List<CourseSchool> findByTrainerIdIn(List<Long> trainerId);

    @Transactional
    @Modifying
    @Query("delete from CourseSchool where courseId = :courseId")
    void deleteByCourseId(Long courseId);

    @Transactional
    @Query(value = "update course_school set status = :status where course_id = :courseId", nativeQuery = true)
    @Modifying
    void updateStatus(Long courseId, byte status);

    CourseSchool findByCourseId(Long courseId);

    @Transactional
    @Query(value = "update course_school set star_num = star_num + :num where course_id = :courseId", nativeQuery = true)
    @Modifying
    void updateStarNum(Long courseId, Integer num);
}