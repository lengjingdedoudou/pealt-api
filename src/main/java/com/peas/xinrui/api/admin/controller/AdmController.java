package com.peas.xinrui.api.admin.controller;

import com.peas.xinrui.api.admin.entity.AdminPermission;
import com.peas.xinrui.api.admin.model.Admin;
import com.peas.xinrui.api.admin.model.Role;
import com.peas.xinrui.api.admin.qo.SessionQo;
import com.peas.xinrui.api.admin.service.AdmService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/adm/admin")
@RequiredSession(SessionType.ADMIN)
public class AdmController extends BaseController {
    @Autowired
    private AdmService admService;

    @RequestMapping("/save_role")
    @RequiredPermission({ AdminPermission.ROLE_EDIT })
    public ModelAndView save_role(String roleJson) {
        admService.saveRole(parseModel(roleJson, new Role()));
        return feedBack(null);
    }

    @RequestMapping("/remove_role")
    @RequiredPermission({ AdminPermission.ROLE_EDIT })
    public ModelAndView remove_role(Integer roleId) {
        admService.removeRole(roleId);
        return feedBack(null);
    }

    @RequestMapping("/roles")
    @RequiredPermission({ AdminPermission.ROLE_LIST, AdminPermission.ROLE_EDIT })
    public ModelAndView roles() {
        return feedBack(admService.roles());
    }

    @RequestMapping("/role")
    @RequiredPermission({ AdminPermission.ROLE_LIST, AdminPermission.ROLE_EDIT })
    public ModelAndView role(Integer roleId) {
        return feedBack(admService.getRoleById(roleId));
    }

    @RequestMapping("/permissions")
    @RequiredPermission({ AdminPermission.ROLE_LIST })
    public ModelAndView permissions() {
        return feedBack(admService.permissions());
    }

    @RequestMapping("/signin")
    @RequiredPermission({ AdminPermission.NONE })
    @RequiredSession(SessionType.NONE)
    public ModelAndView signin(String adminJson) {
        return feedBack(admService.signin(parseModel(adminJson, new Admin())));
    }

    @RequestMapping("/profile")
    @RequiredPermission({ AdminPermission.NONE })
    public ModelAndView profile() {
        return feedBack(admService.profile());
    }

    @RequestMapping("/update_password")
    @RequiredPermission({ AdminPermission.NONE })
    public ModelAndView updatePassword(String newPassword, String oldPassword) {
        admService.updatePassword(newPassword, oldPassword);
        return feedBack(null);
    }

    @RequestMapping("/save_admin")
    @RequiredPermission({ AdminPermission.ADMIN_EDIT })
    public ModelAndView save(String admJsonStr) {
        admService.save(parseModel(admJsonStr, new Admin()));
        return feedBack(null);
    }

    @RequestMapping("/remove_admin")
    @RequiredPermission({ AdminPermission.ADMIN_EDIT })
    public ModelAndView removeAdmin(Long id) {
        admService.remove(id);
        return feedBack(null);
    }

    @RequestMapping("/admins")
    @RequiredPermission({ AdminPermission.ADMIN_LIST, AdminPermission.ADMIN_EDIT })
    public ModelAndView admins() {
        return feedBack(admService.admins());
    }

    @RequestMapping("/admin")
    @RequiredPermission({ AdminPermission.ADMIN_LIST, AdminPermission.ADMIN_EDIT })
    public ModelAndView admin(Long id) {
        return feedBack(admService.admin(id));
    }

    @RequestMapping("/status")
    @RequiredPermission({ AdminPermission.ADMIN_EDIT })
    public ModelAndView status(Long id, Byte status) {
        admService.status(id, status);
        return feedBack(null);
    }

    @RequestMapping("/admin_sessions")
    @RequiredPermission({ AdminPermission.ADMIN_LIST })
    public ModelAndView sessions(String adminSessionQo) {
        return feedBack(admService.sessions(parseModel(adminSessionQo, new SessionQo())));
    }
}