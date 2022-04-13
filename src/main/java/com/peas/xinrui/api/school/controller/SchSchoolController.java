package com.peas.xinrui.api.school.controller;

import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sch/school")
@RequiredSession(SessionType.SCHADMIN)
public class SchSchoolController extends BaseController {

}