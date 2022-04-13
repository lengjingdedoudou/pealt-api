package com.peas.xinrui.api.schadmin.repository;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import com.peas.xinrui.api.schadmin.model.SchAdmin;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SchAdminRepository extends BaseRepository<SchAdmin, Integer> {

    List<SchAdmin> findByIdIn(Collection<Integer> ids);

    List<SchAdmin> findBySchoolId(Integer schoolId);

    @Transactional
    @Query("update SchAdmin set password = :newPassword where id = :id")
    @Modifying
    void updatePassword(Integer id, String newPassword);

    SchAdmin findByMobileAndSchoolId(String mobile, Integer schoolId);

}