package com.peas.xinrui.api.schadmin.repository;

import java.util.Set;

import com.peas.xinrui.api.schadmin.model.SchAdminSession;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SchAdminSessionRepository extends BaseRepository<SchAdminSession, Long> {
    SchAdminSession findByToken(String token);

    @Modifying
    @Query("delete from SchAdminSession where schAdminId = :adminId")
    void deleteAllBySchAdminId(Long adminId);

    @Query("select id from SchAdminSession where schAdminId = :adminId")
    Set<Long> findIdsBySchAdminId(Long adminId);

    @Modifying
    @Query("delete from SchAdminSession where schAdminId = :schAdminId")
    void deleteAllByAdminId(Integer schAdminId);

}