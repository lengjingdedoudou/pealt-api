package com.peas.xinrui.api.common.controller;

import javax.servlet.http.HttpServletResponse;

import com.peas.xinrui.api.common.service.CommonService;
import com.peas.xinrui.api.sms.model.ValCode;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;
import com.peas.xinrui.common.utils.RandomValidateCodeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {
    @Autowired
    private CommonService commonService;

    @RequestMapping("/gen_valCode_signin")
    public void sendCode(Long key, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 100);
            String randomString = new RandomValidateCodeUtil().getRandcode(response, key);// 输出验证码图片方法
            commonService.saveValCode(key, new ValCode(randomString));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "send_valcode")
    public ModelAndView send_valcode(String valCode) throws Exception {
        commonService.sendValCode(parseModel(valCode, new ValCode()));
        return feedBack();
    }

    @RequestMapping(value = "/geocoder")
    public ModelAndView geocoder(String lat, String lng, String key) {
        return feedBack(commonService.geocoder(lat, lng, key));
    }

    @RequestMapping(value = "/oem")
    public ModelAndView oem(Integer schoolId) throws Exception {
        return feedBack(commonService.getOem(schoolId));
    }

    // 身份证上传
    @RequestMapping("/upload_card")
    @RequiredSession({ SessionType.USER, SessionType.SCHADMIN })
    public ModelAndView read_card(String imgUrl) {
        return feedBack(commonService.readIdCard(imgUrl));
    }

    // 支付凭证上传
    @RequestMapping("/read_voucher")
    @RequiredSession({ SessionType.NONE })
    public ModelAndView read_voucher(String imgUrl) {
        return feedBack(commonService.readVoucherInfo(imgUrl));
    }
}