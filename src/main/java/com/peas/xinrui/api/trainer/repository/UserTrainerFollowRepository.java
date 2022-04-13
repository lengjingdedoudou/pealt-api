package com.peas.xinrui.api.trainer.repository;

import java.util.List;

import com.peas.xinrui.api.trainer.model.UserTrainerFollow;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserTrainerFollowRepository extends BaseRepository<UserTrainerFollow, Long> {
    UserTrainerFollow findByUserIdAndTrainerId(Long userId, Long trainerId);

    List<UserTrainerFollow> findByTrainerId(Long trainerId);
}