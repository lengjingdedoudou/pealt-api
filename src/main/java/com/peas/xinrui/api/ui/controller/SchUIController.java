package com.peas.xinrui.api.ui.controller;

import com.peas.xinrui.api.schadmin.entity.SchAdminPermission;
import com.peas.xinrui.api.ui.model.UI;
import com.peas.xinrui.api.ui.qo.UIQo;
import com.peas.xinrui.api.ui.service.UIService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSchoolPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;
import com.peas.xinrui.common.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/sch/ui")
@RequiredSession(SessionType.SCHADMIN)
public class SchUIController extends BaseController {

    @Autowired
    private UIService uiService;

    @RequestMapping(value = "/save")
    @RequiredSchoolPermission(SchAdminPermission.UI_EDIT)
    public ModelAndView save(String ui) {
        uiService.save(parseModel(ui, new UI()));
        return feedBack(null);
    }

    @RequestMapping(value = "/compvos")
    @RequiredSchoolPermission(SchAdminPermission.UI_EDIT)
    public ModelAndView compvos() {
        return feedBack(uiService.components());
    }

    @RequestMapping(value = "/uis")
    @RequiredSchoolPermission(SchAdminPermission.UI_EDIT)
    public ModelAndView uis(String uiQo) {
        return feedBack(uiService.uis(parseModel(uiQo, new UIQo())));
    }

    @RequestMapping(value = "/ui")
    @RequiredSchoolPermission(SchAdminPermission.UI_EDIT)
    public ModelAndView ui(Integer id) {
        return feedBack(uiService.ui(id));
    }

    @RequestMapping(value = "/sync_data")
    @RequiredSchoolPermission(SchAdminPermission.UI_EDIT)
    public ModelAndView syncData(Byte type) {
        uiService.syncData(type);
        return feedBack(null);
    }

    @RequestMapping(value = "/set_default")
    @RequiredSchoolPermission(SchAdminPermission.UI_EDIT)
    public ModelAndView setDefault(Integer id, Byte type) throws ServiceException {
        uiService.setDefault(id, type);
        return feedBack(null);
    }

    @RequestMapping(value = "/remove")
    @RequiredSchoolPermission(SchAdminPermission.UI_EDIT)
    public ModelAndView remove(Integer id) throws ServiceException {
        uiService.remove(id);
        return feedBack(null);
    }

}
