package com.peas.xinrui.api.coupon.repository;

import com.peas.xinrui.api.coupon.model.UserCoupon;
import com.peas.xinrui.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Create by 李振威
 * 2021/12/23 16:48
 */
public interface UserCouponRepository extends BaseRepository<UserCoupon, Integer> {

    @Transactional(readOnly = false)
    @Modifying
    @Query(value = "update user_coupon set status= :status where id = :id", nativeQuery = true)
    void updateStatus(Integer id, Byte status);
}
