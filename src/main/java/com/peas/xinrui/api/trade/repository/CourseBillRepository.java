package com.peas.xinrui.api.trade.repository;

import java.util.List;

import com.peas.xinrui.api.trade.model.CourseBill;
import com.peas.xinrui.common.repository.BaseRepository;

public interface CourseBillRepository extends BaseRepository<CourseBill, Long> {

    CourseBill findByPayNumber(String payNumber);

    List<CourseBill> findByPayNumberIn(List<String> payNumbers);

    List<CourseBill> findByTradeNumber(String tradeNumber);
}