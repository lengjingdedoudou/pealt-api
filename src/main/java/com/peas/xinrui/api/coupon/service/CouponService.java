package com.peas.xinrui.api.coupon.service;

import com.peas.xinrui.api.coupon.model.Coupon;
import com.peas.xinrui.api.coupon.model.UserCoupon;
import com.peas.xinrui.api.coupon.qo.CouponQo;
import com.peas.xinrui.api.coupon.qo.UserCouponQo;
import org.springframework.data.domain.Page;

/**
 * Create by 李振威 2021/12/23 11:21
 */
public interface CouponService {

    // coupon

    void save(Coupon coupon);

    void remove(Integer id);

    void updateStatus(Integer id, Byte status);

    Page<Coupon> coupons(CouponQo qo);


    //user_coupon

    void saveUserCoupon(UserCoupon userCoupon);

    void removeUserCoupon(Integer id);

    UserCoupon getUserCouponById(Integer id);

    void updateUserCouponStatus(Integer id, Byte status);

    Page<UserCoupon> userCoupons(UserCouponQo qo);
}
