package com.peas.xinrui.api.renew.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.peas.xinrui.api.admin.entity.AdminContexts;
import com.peas.xinrui.api.admin.entity.AdminPermission;
import com.peas.xinrui.api.admin.model.Admin;
import com.peas.xinrui.api.admin.model.Role;
import com.peas.xinrui.api.admin.service.AdmService;
import com.peas.xinrui.api.bill.model.Bill;
import com.peas.xinrui.api.bill.service.BillService;
import com.peas.xinrui.api.renew.entity.RenewConstants;
import com.peas.xinrui.api.renew.model.Renew;
import com.peas.xinrui.api.renew.qo.RenewQo;
import com.peas.xinrui.api.renew.repository.RenewRepository;
import com.peas.xinrui.api.school.model.School;
import com.peas.xinrui.api.school.service.SchoolService;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.DetailedServiceException;
import com.peas.xinrui.common.kvCache.KvCache;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.model.Constants;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.DateUtils;
import com.peas.xinrui.common.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class RenewServiceImpl implements RenewService {
    @Autowired
    private RenewRepository renewRepository;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private AdmService admService;

    @Autowired
    private BillService billService;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCache<Long, Renew> renewCache;

    @PostConstruct
    private void init() {
        renewCache = kvCacheFactory.create(new CacheOptions("renew", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, Renew>() {
                    @Override
                    public Renew findByKey(Long key) throws Exception {
                        return renewRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Long, Renew> findByKeys(Collection<Long> keys) throws Exception {
                        Map<Long, Renew> map = new HashMap<>();
                        for (Long key : keys) {
                            map.put(key, findById(key));
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(Renew.class));
    }

    @Override
    public void saveRenew(Renew renew) {
        Long adminId = AdminContexts.getAdminId();
        if (renew.getAmount() == null && renew.getAmount() <= 0) {
            throw new ArgumentServiceException("amount");
        }
        if (renew.getDuration() == null) {
            throw new ArgumentServiceException("duration");
        }

        if (renew.getSchoolId() == null) {
            throw new ArgumentServiceException("auditId");
        }
        byte free = renew.getFree();
        if (free == RenewConstants.FREE_TYPE_GIVE) {
            renew.setPayType(null);
        }
        if (free == RenewConstants.FREE_TYPE_PAY) {
            if (renew.getPayType() == null) {
                throw new ArgumentServiceException("pay-type");
            }
            if (renew.getPayNumber() == null) {
                throw new ArgumentServiceException("pay-number");
            }
            if (renew.getVoucher() == null) {
                throw new ArgumentServiceException("voucher");
            }
            Renew exist = renewRepository.findByPayNumber(renew.getPayNumber());
            if (exist != null) {
                throw new DetailedServiceException("流水号重复");
            }
        }

        List<Renew> schRenew = renewRepository.findBySchoolId(renew.getSchoolId());

        if (!CollectionUtils.isEmpty(schRenew)) {
            schRenew.forEach((r) -> {
                if (r.getStatus() == RenewConstants.WAIT)
                    throw new DetailedServiceException("正在审核中");
            });
        }

        schoolService.school(renew.getSchoolId());

        renew.setSalemanId(adminId);
        renew.setStatus(RenewConstants.WAIT);
        renew.setCreatedAt(System.currentTimeMillis());
        renewRepository.save(renew);
    }

    @Transactional
    @Override
    public void auditRenew(Long renewId, Byte status, String rejectReason) {
        Renew renew = renew(renewId);
        Role role = AdminContexts.adminSessionWrapper().getRole();
        long adminId = AdminContexts.getAdminId();
        boolean allowFinancial = false;
        boolean allowRenew = false;
        for (String permission : role.getPermissions()) {
            if (permission.equals(AdminPermission.FINANCIAL_AUDIT.name())) {
                allowFinancial = true;
            }
            if (permission.equals(AdminPermission.SCHOOL_RENEW.name())) {
                allowRenew = true;
            }
        }
        if (renew.getStatus() == RenewConstants.WAIT) {
            if (!allowRenew) {
                throw new DetailedServiceException("没有账单管理权限");
            }
            if (renew.getFree() == RenewConstants.FREE_TYPE_PAY && !allowFinancial) {
                throw new DetailedServiceException("没有财务审核权限");
            }
            renew.setStatus(status);
        } else {
            throw new DetailedServiceException("不属于待审核订单");
        }
        long curTime = System.currentTimeMillis();
        if (status == RenewConstants.FAIL) {
            if (StringUtils.isEmpty(rejectReason)) {
                throw new DetailedServiceException("错误信息为空！");
            }
            renew.setRejectReason(rejectReason);
        }
        if (status == RenewConstants.SUCC) {
            schoolService.renewSchool(renew.getSchoolId(), renew.getDuration());

            if (renew.getFree() == RenewConstants.FREE_TYPE_PAY) {
                Bill bill = new Bill(renew.getAmount(), renew.getPayType(), renew.getPayNumber(),
                        Constants.BILL_TYPE_RENEW, renew.getId(), curTime);
                billService.saveBill(bill);
            }
        }

        renew.setAuditAt(curTime);
        renew.setAuditId(adminId);
        renewRepository.save(renew);
    }

    @Override
    public Renew renew(Long id) {
        Renew renew = findById(id);
        if (renew == null) {
            throw new ArgumentServiceException("renew");
        }
        return renew;
    }

    @Override
    public Map<Long, Renew> renewsForIds(Set<Long> ids) {
        return renewCache.findByKeys(ids);
    }

    @Override
    public Page<Renew> renews(RenewQo renewQo, String keyWord) {
        if (keyWord != null) {
            Set<Integer> schoolIds = renewRepository.findSchoolId();

            List<Integer> sIds = schoolService.schoolsSearch(keyWord, schoolIds);
            if (CollectionUtils.isEmpty(sIds)) {
                return null;
            }

            renewQo.setSchoolIds(sIds);
        }
        Page<Renew> page = renewRepository.findAll(renewQo);
        wrapRenew(page.getContent());
        return page;
    }

    private void wrapRenew(List<Renew> renews) {
        Set<Long> adminIds = new HashSet<>();
        List<Integer> schoolIds = new ArrayList<>();
        renews.stream().forEach(s -> {
            schoolIds.add(s.getSchoolId());
            adminIds.add(s.getSalemanId());
        });
        Map<Integer, School> sMap = schoolService.schools(schoolIds);
        Map<Long, Admin> aMap = admService.adminsByIdsForMap(adminIds);
        renews.stream().forEach(renew -> {
            School school = sMap.get(renew.getSchoolId());
            Admin saleman = aMap.get(renew.getSalemanId());
            school.setOem(null);
            school.setLogo(null);
            school.setLocation(null);
            renew.setSchool(school);
            renew.setSaleman(saleman);
        });

    }

    private Renew findById(Long id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        return renewCache.findByKey(id);
    }

}