package com.peas.xinrui.api.admin.repository;

import com.peas.xinrui.api.admin.model.Role;
import com.peas.xinrui.common.repository.BaseRepository;

public interface RoleRepository extends BaseRepository<Role, Integer> {
    Role findByName(String name);
}