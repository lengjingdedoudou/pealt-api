package com.peas.xinrui.api.user.repository;

import org.springframework.stereotype.Repository;

import com.peas.xinrui.api.user.model.UserSession;
import com.peas.xinrui.common.repository.BaseRepository;

@Repository
public interface UserSessionRepository extends BaseRepository<UserSession, Long> {
    UserSession findByToken(String token);
}