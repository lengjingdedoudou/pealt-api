package com.peas.xinrui.api.renew.service;

import java.util.Map;
import java.util.Set;

import com.peas.xinrui.api.renew.model.Renew;
import com.peas.xinrui.api.renew.qo.RenewQo;

import org.springframework.data.domain.Page;

public interface RenewService {
    Renew renew(Long id);

    Page<Renew> renews(RenewQo renewQo, String keyWord);

    Map<Long, Renew> renewsForIds(Set<Long> ids);

    void saveRenew(Renew renew);

    void auditRenew(Long renewId, Byte status, String rejectReason);
}