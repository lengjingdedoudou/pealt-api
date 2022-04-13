package com.peas.xinrui.api.bill.service;

import java.util.List;

import com.peas.xinrui.api.bill.model.Bill;
import com.peas.xinrui.api.bill.qo.BillQo;

import org.springframework.data.domain.Page;

public interface BillService {
    void saveBill(Bill bill);

    void saveBills(List<Bill> bills);

    Page<Bill> bills(BillQo billQo);
}