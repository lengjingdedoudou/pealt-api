package com.peas.xinrui.api.admin.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.peas.xinrui.api.admin.model.AdminSession;
import com.peas.xinrui.common.repository.BaseRepository;

@Repository
public interface AdminSessionRepository extends BaseRepository<AdminSession, Long> {
    AdminSession findByToken(String token);

    @Modifying
    @Query("delete from AdminSession where adminId = :adminId")
    void deleteAllByAdminId(Long adminId);

    @Query("select id from AdminSession where adminId = :adminId")
    Set<Long> findIdsByAdminId(Long adminId);
}