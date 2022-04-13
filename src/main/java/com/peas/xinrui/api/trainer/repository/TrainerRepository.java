package com.peas.xinrui.api.trainer.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.peas.xinrui.api.trainer.model.Trainer;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TrainerRepository extends BaseRepository<Trainer, Long> {
    Trainer findByNameAndSchoolId(String name, Integer schoolId);

    List<Trainer> findBySchoolId(Integer schoolId);

    Trainer findByMobileAndSchoolId(String mobile, Integer schoolId);

    @Transactional
    @Modifying
    @Query(value = "update trainer set fans = :fansNum where id = :id", nativeQuery = true)
    void updateFansById(Long id, Integer fansNum);
}