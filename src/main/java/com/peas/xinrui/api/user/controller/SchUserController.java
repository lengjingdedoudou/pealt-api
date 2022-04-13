package com.peas.xinrui.api.user.controller;

import com.peas.xinrui.api.schadmin.entity.SchAdminPermission;
import com.peas.xinrui.api.user.model.User;
import com.peas.xinrui.api.user.qo.UserQo;
import com.peas.xinrui.api.user.service.UserService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSchoolPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sch/usr")
@RequiredSession(SessionType.SCHADMIN)
public class SchUserController extends BaseController {
    @Autowired
    private UserService service;

    @RequestMapping("/user")
    @RequiredSchoolPermission({ SchAdminPermission.USER_LIST })
    public ModelAndView user(Long id) {
        return feedBack(service.getById(id));
    }

    @RequestMapping("/users")
    @RequiredSchoolPermission({ SchAdminPermission.USER_LIST })
    public ModelAndView users(String userQoJson) {
        return feedBack(service.users(parseModel(userQoJson, new UserQo())));
    }

    @RequestMapping("/save")
    @RequiredSchoolPermission({ SchAdminPermission.USER_EDIT })
    public ModelAndView saveUser(String userJsonStr) {
        service.saveUser(parseModel(userJsonStr, new User()));
        return feedBack(null);
    }

    @RequestMapping("/status")
    @RequiredSchoolPermission({ SchAdminPermission.USER_EDIT })
    public ModelAndView status(Long id, Byte status) {
        service.status(id, status);
        return feedBack(null);
    }

}