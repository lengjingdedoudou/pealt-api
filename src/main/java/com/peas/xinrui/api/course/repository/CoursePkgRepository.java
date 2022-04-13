package com.peas.xinrui.api.course.repository;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import com.peas.xinrui.api.course.model.CoursePkg;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CoursePkgRepository extends BaseRepository<CoursePkg, Long> {
    List<CoursePkg> findByIdIn(Collection<Long> ids);

    @Transactional
    @Query("update CoursePkg set status = :status where id = :id")
    @Modifying
    void updateStatus(Long id, Byte status);

    List<CoursePkg> findByCourseId(Long courseId);

    List<CoursePkg> findByCourseIdIn(List<Long> courseIds);
}