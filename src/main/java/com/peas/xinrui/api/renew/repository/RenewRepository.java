package com.peas.xinrui.api.renew.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.peas.xinrui.api.renew.model.Renew;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Query;

public interface RenewRepository extends BaseRepository<Renew, Long> {
    Renew findByPayNumber(String payNumber);

    List<Renew> findBySchoolId(Integer schoolId);

    Set<Renew> findByIdIn(Collection<Long> ids);

    @Query("select schoolId from Renew")
    Set<Integer> findSchoolId();
}