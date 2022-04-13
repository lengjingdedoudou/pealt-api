package com.peas.xinrui.api.schadmin.controller;

import com.peas.xinrui.api.schadmin.entity.SchAdminPermission;
import com.peas.xinrui.api.schadmin.model.SchAdmin;
import com.peas.xinrui.api.schadmin.model.SchRole;
import com.peas.xinrui.api.schadmin.qo.SchAdminSessionQo;
import com.peas.xinrui.api.schadmin.service.SchAdminService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSchoolPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sch/admin")
@RequiredSession(SessionType.SCHADMIN)
public class SchAdminController extends BaseController {
    @Autowired
    private SchAdminService schAdminService;

    @RequestMapping("/save_role")
    @RequiredSchoolPermission({ SchAdminPermission.ROLE_EDIT })
    public ModelAndView save_role(String roleJson) {
        schAdminService.saveRole(parseModel(roleJson, new SchRole()));
        return feedBack(null);
    }

    @RequestMapping("/remove_role")
    @RequiredSchoolPermission({ SchAdminPermission.ROLE_EDIT })
    public ModelAndView remove_role(Integer roleId) {
        schAdminService.removeRole(roleId);
        return feedBack(null);
    }

    @RequestMapping("/role")
    @RequiredSchoolPermission({ SchAdminPermission.ROLE_LIST, SchAdminPermission.ROLE_EDIT })
    public ModelAndView role(Integer roleId) {
        return feedBack(schAdminService.getRoleById(roleId));
    }

    @RequestMapping("/roles")
    @RequiredSchoolPermission({ SchAdminPermission.ROLE_LIST, SchAdminPermission.ROLE_EDIT })
    public ModelAndView roles() {
        return feedBack(schAdminService.roles());
    }

    @RequestMapping("/permissions")
    @RequiredSchoolPermission({ SchAdminPermission.ROLE_LIST })
    public ModelAndView permissions() {
        return feedBack(schAdminService.permissions());
    }

    @RequestMapping("/admin")
    @RequiredSchoolPermission({ SchAdminPermission.ADMIN_EDIT, SchAdminPermission.ADMIN_EDIT })
    public ModelAndView admin(Integer id) {
        return feedBack(schAdminService.admin(id));
    }

    @RequestMapping("/admins")
    @RequiredSchoolPermission({ SchAdminPermission.ADMIN_LIST })
    public ModelAndView admins() {
        return feedBack(schAdminService.admins());
    }

    @RequestMapping("/save_sch_admin")
    @RequiredSchoolPermission({ SchAdminPermission.ADMIN_EDIT })
    public ModelAndView saveSchAdmin(String schAdminJson) {
        schAdminService.saveSchAdmin(parseModel(schAdminJson, new SchAdmin()));
        return feedBack(null);
    }

    @RequestMapping("/remove_admin")
    @RequiredSchoolPermission({ SchAdminPermission.ADMIN_EDIT })
    public ModelAndView removeAdmin(Integer id) {
        schAdminService.remove(id);
        return feedBack(null);
    }

    @RequestMapping("/status")
    @RequiredSchoolPermission({ SchAdminPermission.ADMIN_EDIT })
    public ModelAndView status(Integer id, Byte status) {
        schAdminService.status(id, status);
        return feedBack(null);
    }

    @RequestMapping("/signin")
    @RequiredSchoolPermission({ SchAdminPermission.NONE })
    @RequiredSession(SessionType.NONE)
    public ModelAndView signin(String schAdminJson) {
        return feedBack(schAdminService.signin(parseModel(schAdminJson, new SchAdmin())));
    }

    @RequestMapping("/profile")
    @RequiredSchoolPermission({ SchAdminPermission.NONE })
    public ModelAndView profile() {
        return feedBack(schAdminService.profile());
    }

    @RequestMapping("/update_password")
    @RequiredSchoolPermission({ SchAdminPermission.NONE })
    public ModelAndView updatePassword(String newPassword, String oldPassword) {
        schAdminService.updatePassword(newPassword, oldPassword);
        return feedBack(null);
    }

    @RequestMapping("/reset_psw")
    @RequiredSchoolPermission({ SchAdminPermission.NONE })
    public ModelAndView resetPsw(Integer id, String newPassword) {
        schAdminService.resetPsw(id, newPassword);
        return feedBack(null);
    }

    @RequestMapping("/admin_sessions")
    @RequiredSchoolPermission({ SchAdminPermission.NONE })
    public ModelAndView adminSessions(String adminSessionQo) {
        return feedBack(schAdminService.adminSessions(parseModel(adminSessionQo, new SchAdminSessionQo())));
    }
}