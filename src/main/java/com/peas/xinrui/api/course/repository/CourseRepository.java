package com.peas.xinrui.api.course.repository;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import com.peas.xinrui.api.course.model.Course;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends BaseRepository<Course, Long> {
    List<Course> findByIdIn(Collection<Long> ids);

    @Transactional
    @Query(value = "update course set status = :status where id = :id", nativeQuery = true)
    @Modifying
    void updateStatus(Long id, byte status);

}