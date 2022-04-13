package com.peas.xinrui.api.coupon.repository;

import com.peas.xinrui.api.coupon.model.Coupon;
import com.peas.xinrui.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create by 李振威 2021/12/23 11:20
 */
public interface CouponRepository extends BaseRepository<Coupon, Integer> {

    @Transactional(readOnly = false)
    @Modifying
    @Query(value = "update coupon set status= :status where id = :id", nativeQuery = true)
    void updateStatus(Integer id, Byte status);
}
