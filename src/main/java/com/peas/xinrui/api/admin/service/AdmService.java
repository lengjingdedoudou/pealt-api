package com.peas.xinrui.api.admin.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.peas.xinrui.api.admin.entity.AdminSessionWrapper;
import com.peas.xinrui.api.admin.model.Admin;
import com.peas.xinrui.api.admin.model.AdminSession;
import com.peas.xinrui.api.admin.model.Role;
import com.peas.xinrui.api.admin.qo.SessionQo;
import com.peas.xinrui.api.user.model.User;
import com.peas.xinrui.api.user.qo.UserQo;
import com.peas.xinrui.common.model.Permission;

import org.springframework.data.domain.Page;

public interface AdmService {
    void save(Admin admin);

    Admin getById(Long id);

    List<Admin> adminsByIds(Set<Long> ids);

    Map<Long, Admin> adminsByIdsForMap(Collection<Long> ids);

    AdminSessionWrapper signin(Admin admin);

    Admin profile();

    void updatePassword(String newPassword, String oldPassword);

    void remove(Long id);

    Page<Admin> admins();

    Admin admin(Long id);

    void status(Long id, Byte state);

    void saveRole(Role role);

    Role getRoleById(Integer id);

    void removeRole(Integer id);

    List<Role> roles();

    List<Permission> permissions();

    AdminSessionWrapper findByToken(String token);

    Page<AdminSession> sessions(SessionQo qo);

    Page<User> users(UserQo qo);

    User user(Long userId);

    void stateUser(Long userId, Byte state);

    Map<Long, Admin> likeForAdmin(String pattern);

    Map<Object, String> globalLike(String pattern);
}