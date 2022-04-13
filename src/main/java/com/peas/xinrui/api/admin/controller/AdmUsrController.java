package com.peas.xinrui.api.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.peas.xinrui.api.admin.service.AdmService;
import com.peas.xinrui.api.user.qo.UserQo;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

@Controller("/adm/usr")
@RequiredSession(SessionType.ADMIN)
public class AdmUsrController extends BaseController {

    @Autowired
    private AdmService service;

    @RequestMapping("/users")
    public ModelAndView users(String userQoJson) {
        return feedBack(service.users(parseModel(userQoJson, new UserQo())));
    }

    @RequestMapping("/user")
    public ModelAndView user(Long userId) {
        return feedBack(service.user(userId));
    }

}