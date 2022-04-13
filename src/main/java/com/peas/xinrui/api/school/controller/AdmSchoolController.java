package com.peas.xinrui.api.school.controller;

import com.peas.xinrui.api.admin.entity.AdminPermission;
import com.peas.xinrui.api.school.model.School;
import com.peas.xinrui.api.school.qo.SchoolQo;
import com.peas.xinrui.api.school.service.SchoolService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/adm/school")
@RequiredSession(SessionType.ADMIN)
public class AdmSchoolController extends BaseController {
    @Autowired
    private SchoolService schoolService;

    @RequestMapping("/save_school")
    @RequiredPermission({ AdminPermission.SCHOOL_EDIT })
    public ModelAndView saveSchool(String schoolJson) {
        schoolService.saveSchool(parseModel(schoolJson, new School()));
        return feedBack(null);
    }

    @RequestMapping("/struct_school")
    @RequiredPermission({ AdminPermission.SCHOOL_LIST })
    public ModelAndView structItem(Integer id) {
        return feedBack(schoolService.item(id));
    }

    @RequestMapping("/item")
    @RequiredPermission({ AdminPermission.SCHOOL_LIST })
    public ModelAndView school(Integer id) {
        return feedBack(schoolService.school(id));
    }

    @RequestMapping("/schools")
    @RequiredPermission({ AdminPermission.SCHOOL_LIST })
    public ModelAndView schools(String schoolQoJson) {
        return feedBack(schoolService.schools(parseModel(schoolQoJson, new SchoolQo())));
    }

    @RequestMapping("/school_status")
    @RequiredPermission({ AdminPermission.SCHOOL_EDIT })
    public ModelAndView schoolStatus(Integer id, Byte status) {
        schoolService.schoolStatus(id, status);
        return feedBack(null);
    }

    @RequestMapping("/expire_count")
    @RequiredPermission({ AdminPermission.SCHOOL_LIST })
    public ModelAndView expireCount(Long time) {
        return feedBack(schoolService.expireCount(time));
    }
}