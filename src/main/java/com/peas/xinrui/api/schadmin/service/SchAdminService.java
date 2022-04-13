package com.peas.xinrui.api.schadmin.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.peas.xinrui.api.schadmin.entity.SchAdminSessionWrapper;
import com.peas.xinrui.api.schadmin.model.SchAdmin;
import com.peas.xinrui.api.schadmin.model.SchAdminSession;
import com.peas.xinrui.api.schadmin.model.SchRole;
import com.peas.xinrui.api.schadmin.qo.SchAdminQo;
import com.peas.xinrui.api.schadmin.qo.SchAdminSessionQo;
import com.peas.xinrui.common.model.Permission;

import org.springframework.data.domain.Page;

public interface SchAdminService {

    void saveSchAdmin(SchAdmin admin);

    List<SchAdmin> admins(SchAdminQo qo);

    SchAdminSessionWrapper signin(SchAdmin schAdmin);

    void remove(Integer id);

    SchAdmin getById(Integer id);

    void resetPsw(Integer id, String newPassword);

    void updatePassword(String newPassword, String oldPassword);

    SchAdminSessionWrapper findByToken(String token);

    SchRole getRoleById(Integer id);

    SchAdmin profile();

    Page<SchAdmin> admins();

    Map<Integer, SchAdmin> findByIdIn(Set<Integer> ids);

    SchAdmin admin(Integer id);

    void status(Integer id, Byte state);

    void saveRole(SchRole role);

    void removeRole(Integer id);

    List<SchRole> roles();

    List<SchRole> roles(Integer schoolId);

    List<Permission> permissions();

    Page<SchAdminSession> adminSessions(SchAdminSessionQo qo);
}