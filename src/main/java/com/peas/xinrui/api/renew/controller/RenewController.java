package com.peas.xinrui.api.renew.controller;

import com.peas.xinrui.api.admin.entity.AdminPermission;
import com.peas.xinrui.api.renew.model.Renew;
import com.peas.xinrui.api.renew.qo.RenewQo;
import com.peas.xinrui.api.renew.service.RenewService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/adm/renew")
@RequiredSession(SessionType.ADMIN)
public class RenewController extends BaseController {
    @Autowired
    private RenewService renewService;

    @RequestMapping("/save_renew")
    @RequiredPermission({ AdminPermission.SCHOOL_RENEW })
    public ModelAndView renew(String renewJsonStr) {
        renewService.saveRenew(parseModel(renewJsonStr, new Renew()));
        return feedBack(null);
    }

    @RequestMapping("/renews")
    @RequiredPermission({ AdminPermission.SCHOOL_RENEW })
    public ModelAndView renews(String renewJsonQo, String keyWord) {
        return feedBack(renewService.renews(parseModel(renewJsonQo, new RenewQo()), keyWord));
    }

    @RequestMapping("/audit")
    @RequiredPermission({ AdminPermission.FINANCIAL_AUDIT })
    public ModelAndView audit(Long id, Byte status, String rejectReason) {
        renewService.auditRenew(id, status, rejectReason);
        return feedBack(null);
    }

}