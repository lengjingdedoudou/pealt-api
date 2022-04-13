package com.peas.xinrui.api.user.controller;

import com.peas.xinrui.api.sms.model.ValCode;
import com.peas.xinrui.api.user.model.User;
import com.peas.xinrui.api.user.qo.UserQo;
import com.peas.xinrui.api.user.service.UserService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;
import com.peas.xinrui.common.controller.auth.Touchable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/usr/user")
@RequiredSession(SessionType.USER)
public class UserController extends BaseController {
    @Autowired
    private UserService service;

    @RequestMapping("/signin")
    @RequiredSession(SessionType.NONE)
    public ModelAndView signin(String userJsonStr, String valCode) {
        return feedBack(service.signin(parseModel(userJsonStr, new User()), parseModel(valCode, new ValCode())));
    }

    @RequestMapping("/profile")
    @Touchable(true)
    public ModelAndView profile() {
        return feedBack(service.profile());
    }

    @RequestMapping("/save_profile")
    public ModelAndView saveProfile(String userJsonStr) {
        service.saveProfile(parseModel(userJsonStr, new User()));
        return feedBack();
    }

    @RequestMapping("/valid_mobile")
    public ModelAndView validMobile(String userJsonStr, String valCode) {
        service.validMobile(parseModel(userJsonStr, new User()), parseModel(valCode, new ValCode()));
        return feedBack();
    }

    @RequestMapping("/mod_profile_mobile")
    public ModelAndView modProfileMobile(String userJsonStr, String valCode) {
        service.modProfileMobile(parseModel(userJsonStr, new User()), parseModel(valCode, new ValCode()));
        return feedBack();
    }

    @RequestMapping("/export_excel")
    public ModelAndView exportUserExcel(String userQoJsonStr) {
        return feedBack(service.exportUsers(parseModel(userQoJsonStr, new UserQo())));
    }

    @RequestMapping("/auth")
    public ModelAndView auth(String userJsonStr) {
        service.auth(parseModel(userJsonStr, new User()));
        return feedBack();
    }
}