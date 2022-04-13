package com.peas.xinrui.api.bill.controller;

import com.peas.xinrui.api.admin.entity.AdminPermission;
import com.peas.xinrui.api.bill.qo.BillQo;
import com.peas.xinrui.api.bill.service.BillService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/adm/bill")
@RequiredSession(SessionType.ADMIN)
public class BillController extends BaseController {
    @Autowired
    private BillService billService;

    @RequestMapping("/bills")
    @RequiredPermission({ AdminPermission.SCHOOL_BILL })
    public ModelAndView bills(String billQoJsonStr) {
        return feedBack(billService.bills(parseModel(billQoJsonStr, new BillQo())));
    }

}