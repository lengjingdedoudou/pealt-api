package com.peas.xinrui.api.coupon.controller;

import com.peas.xinrui.api.coupon.model.Coupon;
import com.peas.xinrui.api.coupon.qo.CouponQo;
import com.peas.xinrui.api.coupon.service.CouponService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Create by 李振威 2021/12/23 11:22
 */
@Controller
@RequestMapping("/sch/coupon")
@RequiredSession(SessionType.SCHADMIN)
public class SchCouponController extends BaseController {

    @Autowired
    private CouponService couponService;

    @RequestMapping("/save")
    public ModelAndView save(String couponJsonStr) {
        couponService.save(parseModel(couponJsonStr, new Coupon()));
        return feedBack();
    }

    @RequestMapping("/remove")
    public ModelAndView remove(Integer id) {
        couponService.remove(id);
        return feedBack();
    }

    @RequestMapping("/update_status")
    public ModelAndView updateStatus(Integer id, Byte status) {
        couponService.updateStatus(id, status);
        return feedBack();
    }

    @RequestMapping("/coupons")
    public ModelAndView coupons(String couponQo) {
        return feedBack(couponService.coupons(parseModel(couponQo, new CouponQo())));
    }
}
