package com.peas.xinrui.api.user.repository;

import java.util.List;

import com.peas.xinrui.api.user.model.UserFollow;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowRepository extends BaseRepository<UserFollow, Long> {
    UserFollow findByUserIdAndReferId(Long userId, Long referId);

    List<UserFollow> findByReferId(Long referId);

    List<UserFollow> findByReferIdIn(List<Long> referIds);
}