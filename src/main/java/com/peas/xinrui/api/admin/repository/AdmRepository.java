package com.peas.xinrui.api.admin.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.peas.xinrui.api.admin.model.Admin;
import com.peas.xinrui.common.repository.BaseRepository;

@Repository
public interface AdmRepository extends BaseRepository<Admin, Long> {
    public List<Admin> findByIdIn(Collection<Long> ids);

    public Admin findByMobile(String mobile);
}