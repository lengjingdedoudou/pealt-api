package com.peas.xinrui.api.course.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.peas.xinrui.api.course.model.Lesson;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LessonRepository extends BaseRepository<Lesson, Long> {

    @Transactional
    @Query("update Lesson set status = :status where id in :ids")
    @Modifying
    void updateStatusIn(List<Long> ids, Byte status);

    List<Lesson> findByCourseId(Long courseId);

    List<Lesson> findByChapterId(Long chapterId);

    @Query("select id from Lesson where chapterId in :chapterIds")
    List<Long> findIdsByChapterId(List<Long> chapterIds);
}