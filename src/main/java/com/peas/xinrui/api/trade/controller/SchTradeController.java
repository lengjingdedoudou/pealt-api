package com.peas.xinrui.api.trade.controller;

import com.peas.xinrui.api.trade.entity.CourseBillAuditDTO;
import com.peas.xinrui.api.trade.entity.TradeAuditDTO;
import com.peas.xinrui.api.trade.model.CollectionSettings;
import com.peas.xinrui.api.trade.qo.CourseBillQo;
import com.peas.xinrui.api.trade.qo.TradeQo;
import com.peas.xinrui.api.trade.service.TradeService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sch/trade")
@RequiredSession(SessionType.SCHADMIN)
public class SchTradeController extends BaseController {
    @Autowired
    private TradeService tradeService;

    @RequestMapping("/trades")
    public ModelAndView trades(String tradeQoJson) {
        return feedBack(tradeService.trades(parseModel(tradeQoJson, new TradeQo())));
    }

    @RequestMapping("/settings")
    public ModelAndView settings() {
        return feedBack(tradeService.collectionSettings(SessionType.SCHADMIN));
    }

    @RequestMapping("/save_settings")
    public ModelAndView saveSettings(String settingsJson) {
        tradeService.saveCollectionSettings(parseModel(settingsJson, new CollectionSettings()));
        return feedBack(null);
    }

    @RequestMapping("/trade_course_bills")
    public ModelAndView tradeCourseBills(String tradeNumber) {
        return feedBack(tradeService.courseBills(tradeNumber));
    }

    @RequestMapping("/trade_audit")
    public ModelAndView tradeAudit(String tradeAuditDTOJson) {
        tradeService.auditTrade(parseModel(tradeAuditDTOJson, new TradeAuditDTO()));
        return feedBack(null);
    }

    @RequestMapping("/course_bills")
    public ModelAndView courseBills(String courseBillJson) {
        return feedBack(tradeService.courseBills(parseModel(courseBillJson, new CourseBillQo())));
    }

    @RequestMapping("/trade")
    public ModelAndView trade(String tradeNumber) {
        return feedBack(tradeService.tradeByTradeNumber(tradeNumber));
    }

    @RequestMapping("/bill_audit")
    public ModelAndView billAudit(String courseBillAuditDTOJson) {
        tradeService.auditCourseBill(parseModel(courseBillAuditDTOJson, new CourseBillAuditDTO()));
        return feedBack(null);
    }

    @RequestMapping("/export_bill")
    public ModelAndView exportBill(String courseBillQoJson) {
        return feedBack(tradeService.exportBill(parseModel(courseBillQoJson, new CourseBillQo())));
    }

    @RequestMapping("/export_bill_msg")
    public ModelAndView exportBillMsg(Integer id, String secret) {
        return feedBack(tradeService.exportBillMsg(id, secret));
    }
}