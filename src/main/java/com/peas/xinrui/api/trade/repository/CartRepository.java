package com.peas.xinrui.api.trade.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.peas.xinrui.api.trade.model.Cart;
import com.peas.xinrui.common.repository.BaseRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends BaseRepository<Cart, Long> {

    Cart findByCourseIdAndSchoolIdAndUserId(Long courseId, Integer schoolId, Long userId);

    @Transactional
    @Query("delete from Cart where id in :ids")
    @Modifying
    void deleteByIds(List<Long> ids);
}