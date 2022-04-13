package com.peas.xinrui.api.ui.controller;

import com.peas.xinrui.api.ui.service.UIService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: lilin
 * @Date: 2019-10-19 18:17
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/usr/ui")
@RequiredSession(SessionType.USER)
public class UIUsrController extends BaseController {

    @Autowired
    private UIService uiService;

    @RequestMapping(value = "/ui")
    public ModelAndView ui(Byte type, Integer schoolId) {
        return feedBack(uiService.uiByTypeAndSchoolId(type, schoolId));
    }
}
