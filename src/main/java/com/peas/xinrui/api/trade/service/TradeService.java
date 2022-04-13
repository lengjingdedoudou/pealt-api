package com.peas.xinrui.api.trade.service;

import java.util.List;

import com.peas.xinrui.api.heavywork.model.HeavyWork;
import com.peas.xinrui.api.trade.entity.CartWO;
import com.peas.xinrui.api.trade.entity.CourseBillAuditDTO;
import com.peas.xinrui.api.trade.entity.TradeAuditDTO;
import com.peas.xinrui.api.trade.model.Cart;
import com.peas.xinrui.api.trade.model.CollectionSettings;
import com.peas.xinrui.api.trade.model.CourseBill;
import com.peas.xinrui.api.trade.model.Trade;
import com.peas.xinrui.api.trade.qo.CartQo;
import com.peas.xinrui.api.trade.qo.CourseBillQo;
import com.peas.xinrui.api.trade.qo.TradeQo;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.data.domain.Page;

public interface TradeService {
   void addCart(Cart cart);

   Page<Cart> carts(CartQo qo, CartWO wo);

   List<Cart> cartForList(CartQo qo, CartWO wo);

   Trade trade(Long id);

   Trade tradeByTradeNumber(String tradeNumber);

   Page<Trade> trades(TradeQo qo);

   Long submitTrade(Trade trade);

   void payTrade(Long tradeId, List<CourseBill> courseBills);

   void auditTrade(TradeAuditDTO dto);

   void cancelTrade(Long id);

   void saveCollectionSettings(CollectionSettings settings);

   CollectionSettings collectionSettings(SessionType sessionType);

   List<CourseBill> courseBills(String tradeNumber);

   Page<CourseBill> courseBills(CourseBillQo qo);

   void auditCourseBill(CourseBillAuditDTO dto);

   HeavyWork exportBill(CourseBillQo billQo);

   HeavyWork exportBillMsg(Integer id, String secret);
}