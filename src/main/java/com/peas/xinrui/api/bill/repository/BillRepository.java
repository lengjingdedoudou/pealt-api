package com.peas.xinrui.api.bill.repository;

import java.util.List;

import com.peas.xinrui.api.bill.model.Bill;
import com.peas.xinrui.common.repository.BaseRepository;

public interface BillRepository extends BaseRepository<Bill, Integer> {
    Bill findByPayNumber(String payNumber);

    Bill findByRenferId(Integer renferId);

    List<Bill> findByPayNumberIn(List<String> payNumbers);
}