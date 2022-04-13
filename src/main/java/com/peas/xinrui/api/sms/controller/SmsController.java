package com.peas.xinrui.api.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.peas.xinrui.api.sms.model.ValCode;
import com.peas.xinrui.api.sms.service.SmsService;
import com.peas.xinrui.common.controller.BaseController;

@Controller
@RequestMapping(value = "/sms")
public class SmsController extends BaseController {

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "send_valcode")
    public ModelAndView send_valcode(String valCode) throws Exception {
        smsService.sendValCode(parseModel(valCode, new ValCode()));
        return feedBack();
    }
}
