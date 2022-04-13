package com.peas.xinrui.api.heavywork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.peas.xinrui.api.heavywork.model.HeavyWork;

public interface HeavyWorkRepository extends JpaRepository<HeavyWork, Integer> {
    @Query("select progress from HeavyWork where secret = :secret")
    Integer findProgressBySecret(String secret);
}
