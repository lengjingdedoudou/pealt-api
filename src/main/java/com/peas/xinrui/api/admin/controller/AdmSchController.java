package com.peas.xinrui.api.admin.controller;

import com.peas.xinrui.api.admin.entity.AdminPermission;
import com.peas.xinrui.api.schadmin.model.SchAdmin;
import com.peas.xinrui.api.schadmin.service.SchAdminService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/adm/sch")
@RequiredSession(SessionType.ADMIN)
public class AdmSchController extends BaseController {

    @Autowired
    private SchAdminService schAdminService;

    @RequestMapping("/save_sch_admin")
    @RequiredPermission({ AdminPermission.ACCOUNT_EDIT })
    public ModelAndView saveSchAdmin(String schAdminJson) {
        schAdminService.saveSchAdmin(parseModel(schAdminJson, new SchAdmin()));
        return feedBack(null);
    }

    @RequestMapping("/roles")
    @RequiredPermission({ AdminPermission.ROLE_LIST, AdminPermission.ROLE_EDIT })
    public ModelAndView roles(Integer schoolId) {
        return feedBack(schAdminService.roles(schoolId));
    }

    @RequestMapping("/reset_psw")
    @RequiredPermission({ AdminPermission.SCHOOL_EDIT })
    public ModelAndView resetPsw(Integer id, String newPassword) {
        schAdminService.resetPsw(id, newPassword);
        return feedBack(null);
    }
}