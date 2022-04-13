package com.peas.xinrui.api.user.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.peas.xinrui.api.user.model.UserCourseCollect;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserCourseCollectRepository extends BaseRepository<UserCourseCollect, Long> {
    UserCourseCollect findByUserIdAndCourseId(Long userId, Long courseId);

    List<UserCourseCollect> findByUserId(Long userId);

    @Transactional
    @Query("delete from UserCourseCollect where userId = :userId and courseId = :courseId")
    @Modifying
    void deleteByUserIdAndCourseId(Long userId, Long courseId);
}