package com.peas.xinrui.api.coupon.controller;


import com.peas.xinrui.api.coupon.model.UserCoupon;
import com.peas.xinrui.api.coupon.qo.UserCouponQo;
import com.peas.xinrui.api.coupon.service.CouponService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/usr/user_coupon")
@RequiredSession(SessionType.USER)
public class UsrUserCouponController extends BaseController {

    @Autowired
    private CouponService couponService;

    @RequestMapping("/save")
    public ModelAndView save(String userCouponJsonStr){
        couponService.saveUserCoupon(parseModel(userCouponJsonStr, new UserCoupon()));
        return feedBack();
    }

    @RequestMapping("/remove")
    public ModelAndView remove(Integer id){
        couponService.removeUserCoupon(id);
        return feedBack();
    }

    @RequestMapping("/user_coupon")
    public ModelAndView userCoupon(Integer id){
        return feedBack(couponService.getUserCouponById(id));
    }

    @RequestMapping("/update_status")
    public ModelAndView updateStatus(Integer id, Byte status){
        couponService.updateUserCouponStatus(id, status);
        return feedBack();
    }

    @RequestMapping("/user_coupons")
    public ModelAndView userCoupons(String userCouponQo){
        return feedBack(couponService.userCoupons(parseModel(userCouponQo, new UserCouponQo())));
    }
}
