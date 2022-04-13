package com.peas.xinrui.api.trade.controller;

import com.peas.xinrui.api.trade.entity.CartWO;
import com.peas.xinrui.api.trade.model.Cart;
import com.peas.xinrui.api.trade.model.CourseBill;
import com.peas.xinrui.api.trade.model.Trade;
import com.peas.xinrui.api.trade.qo.CartQo;
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
@RequestMapping("/usr/trade")
@RequiredSession(SessionType.USER)
public class TradeController extends BaseController {
    @Autowired
    private TradeService tradeService;

    @RequestMapping("/add_cart")
    public ModelAndView addCart(String cartJson) {
        tradeService.addCart(parseModel(cartJson, new Cart()));
        return feedBack(null);
    }

    @RequestMapping("/settings")
    public ModelAndView settings() {
        return feedBack(tradeService.collectionSettings(SessionType.USER));
    }

    @RequestMapping("/carts")
    public ModelAndView carts(String cartQoJson) {
        return feedBack(tradeService.carts(parseModel(cartQoJson, new CartQo()), CartWO.getAllInstance()));
    }

    @RequestMapping("/cart_list")
    public ModelAndView cartList(String cartQoJson) {
        return feedBack(tradeService.carts(parseModel(cartQoJson, new CartQo()), CartWO.getAllInstance()));
    }

    @RequestMapping("/submit_trade")
    public ModelAndView submitTrade(String tradeJson) {
        return feedBack(tradeService.submitTrade(parseModel(tradeJson, new Trade())));
    }

    @RequestMapping("/pay_trade")
    public ModelAndView payTrade(Long tradeId, String courseBillsJson) {
        tradeService.payTrade(tradeId, parseList(courseBillsJson, CourseBill.class));
        return feedBack(null);
    }

    @RequestMapping("/cancel_trade")
    public ModelAndView cancelTrade(Long tradeId) {
        tradeService.cancelTrade(tradeId);
        return feedBack(null);
    }

    @RequestMapping("/trade_item")
    public ModelAndView tradeItem(Long id) {
        return feedBack(tradeService.trade(id));
    }

    @RequestMapping("/trades")
    public ModelAndView trades(String tradeQoJson) {
        return feedBack(tradeService.trades(parseModel(tradeQoJson, new TradeQo())));
    }

    @RequestMapping("/trade_cancel")
    public ModelAndView tradeCancel(Long tradeId) {
        tradeService.cancelTrade(tradeId);
        return feedBack(null);
    }
}