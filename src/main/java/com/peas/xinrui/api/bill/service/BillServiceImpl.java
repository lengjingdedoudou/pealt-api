package com.peas.xinrui.api.bill.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.peas.xinrui.api.bill.model.Bill;
import com.peas.xinrui.api.bill.qo.BillQo;
import com.peas.xinrui.api.bill.repository.BillRepository;
import com.peas.xinrui.api.renew.model.Renew;
import com.peas.xinrui.api.renew.service.RenewService;
import com.peas.xinrui.api.school.model.School;
import com.peas.xinrui.api.school.service.SchoolService;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.DetailedServiceException;
import com.peas.xinrui.common.utils.CollectionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private RenewService renewService;

    @Autowired
    private SchoolService schoolService;

    @Override
    public void saveBill(Bill bill) {
        if (bill.getAmount() == null) {
            throw new ArgumentServiceException("amount");
        }

        if (bill.getRenferId() == null) {
            throw new ArgumentServiceException("RenderId");
        }
        if (bill.getRenferType() == null) {
            throw new ArgumentServiceException("RenferType");
        }
        if (bill.getPayNumber() != null) {
            Bill existBill = billRepository.findByPayNumber(bill.getPayNumber());
            if (existBill != null) {
                throw new DetailedServiceException("流水号已存在！");
            }
        }

        Renew existRenew = renewService.renew(bill.getRenferId());
        if (existRenew == null) {
            throw new DetailedServiceException("账单不存在！");
        }
        billRepository.save(bill);
    }

    @Override
    public void saveBills(List<Bill> bills) {
        if (CollectionUtils.isEmpty(bills)) {
            throw new ArgumentServiceException("bills");
        }

        List<String> payNumbers = new ArrayList<>();
        bills.forEach(bill -> {
            if (bill.getAmount() == null) {
                throw new ArgumentServiceException("amount");
            }
            if (bill.getRenferId() == null) {
                throw new ArgumentServiceException("renferId");
            }
            if (bill.getRenferType() == null) {
                throw new ArgumentServiceException("renferType");
            }
            if (bill.getPayNumber() == null) {
                throw new ArgumentServiceException("payNumvber");
            }
            if (bill.getPayType() == null) {
                throw new ArgumentServiceException("payType");
            }
            bill.setCreatedAt(System.currentTimeMillis());
            payNumbers.add(bill.getPayNumber());
        });

        List<Bill> exists = billRepository.findByPayNumberIn(payNumbers);
        if (CollectionUtils.isNotEmpty(exists)) {
            throw new DetailedServiceException("bill流水号已存在");
        }

        billRepository.saveAll(bills);
    }

    @Override
    public Page<Bill> bills(BillQo billQo) {
        Page<Bill> page = billRepository.findAll(billQo);
        wrapBills(page.getContent());
        return page;
    }

    private void wrapBills(List<Bill> bills) {
        Set<Long> renewIds = bills.stream().map(Bill::getRenferId).collect(Collectors.toSet());
        Map<Long, Renew> renewMap = renewService.renewsForIds(renewIds);

        List<Integer> schoolIds = renewMap.values().stream().map(Renew::getSchoolId).collect(Collectors.toList());
        Map<Integer, School> schools = schoolService.schools(schoolIds);

        bills.stream().forEach((item) -> {
            Renew renew = renewMap.get(item.getRenferId());
            int schoolId = renew.getSchoolId();
            item.setSchool(schools.get(schoolId));
        });
    }
}