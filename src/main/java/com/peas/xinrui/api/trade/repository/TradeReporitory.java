package com.peas.xinrui.api.trade.repository;

import java.util.List;

import com.peas.xinrui.api.trade.model.Trade;
import com.peas.xinrui.common.repository.BaseRepository;

public interface TradeReporitory extends BaseRepository<Trade, Long> {
    Trade findByTradeNumber(String tradeNumber);

    List<Trade> findByUserId(Long userId);
}