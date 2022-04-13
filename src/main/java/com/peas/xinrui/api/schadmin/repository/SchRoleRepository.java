package com.peas.xinrui.api.schadmin.repository;

import java.util.List;

import com.peas.xinrui.api.schadmin.model.SchRole;
import com.peas.xinrui.common.repository.BaseRepository;

public interface SchRoleRepository extends BaseRepository<SchRole, Integer> {
    SchRole findByName(String name);

    List<SchRole> findBySchoolId(Integer schoolId);

    SchRole findByNameAndSchoolId(String name, Integer schoolId);
}