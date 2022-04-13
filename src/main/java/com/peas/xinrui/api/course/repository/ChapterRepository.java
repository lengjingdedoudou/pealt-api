package com.peas.xinrui.api.course.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.peas.xinrui.api.course.model.Chapter;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ChapterRepository extends BaseRepository<Chapter, Long> {

    @Transactional
    @Query("update Chapter set status = :status where id in :ids")
    @Modifying
    void updateStatusIn(List<Long> ids, Byte status);

    List<Chapter> findByCourseId(Long courseId);
}