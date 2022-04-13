package com.peas.xinrui.api.school.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import com.peas.xinrui.api.school.model.School;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SchoolRepository extends BaseRepository<School, Integer> {
    List<School> findByIdIn(Collection<Integer> ids);

    @Transactional
    @Query("update School set status = :status where id = :id")
    @Modifying
    void updateStatus(Integer id, byte status);

    @Transactional
    @Query("update School set validThru = :validThru where id = :id")
    @Modifying
    void updateValidThru(Integer id, Long validThru);

    @Query("select count(*) from School where validThru < :time")
    int countByValidThru(Long time);

    @Query(value = "select id from school where id in :ids and name like CONCAT('%',:keyWord,'%')", nativeQuery = true)
    List<Integer> findIdsByKeyWordAndIds(String keyWord, Set<Integer> ids);
}