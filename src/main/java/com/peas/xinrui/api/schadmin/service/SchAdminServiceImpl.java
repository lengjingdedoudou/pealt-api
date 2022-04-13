package com.peas.xinrui.api.schadmin.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.peas.xinrui.api.admin.entity.AdminStatus;
import com.peas.xinrui.api.schadmin.entity.SchAdminContexts;
import com.peas.xinrui.api.schadmin.entity.SchAdminPermission;
import com.peas.xinrui.api.schadmin.entity.SchAdminSessionWrapper;
import com.peas.xinrui.api.schadmin.model.SchAdmin;
import com.peas.xinrui.api.schadmin.model.SchAdminSession;
import com.peas.xinrui.api.schadmin.model.SchRole;
import com.peas.xinrui.api.schadmin.qo.SchAdminQo;
import com.peas.xinrui.api.schadmin.qo.SchAdminSessionQo;
import com.peas.xinrui.api.schadmin.repository.SchAdminRepository;
import com.peas.xinrui.api.schadmin.repository.SchAdminSessionRepository;
import com.peas.xinrui.api.schadmin.repository.SchRoleRepository;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.cache.SingleRepositoryProvider;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.DetailedServiceException;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.exception.SessionServiceException;
import com.peas.xinrui.common.kvCache.KvCache;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.model.ErrorCode;
import com.peas.xinrui.common.model.Permission;
import com.peas.xinrui.common.utils.ByteUtils;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.DateUtils;
import com.peas.xinrui.common.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class SchAdminServiceImpl implements SchAdminService, ErrorCode {

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCache<Integer, SchAdmin> schAdminCache;
    private KvCache<String, SchAdminSession> sessionCache;
    private KvCache<Integer, SchRole> roleCache;
    @Autowired
    private SchAdminRepository schAdminRepository;
    @Autowired
    private SchRoleRepository schRoleRepository;
    @Value("${admin.salt}")
    private String salt;

    @Value("${admin.tokenHour}")
    private Integer tokenHours;
    @Autowired
    private SchAdminSessionRepository sessionRepository;

    @PostConstruct
    private void init() {
        schAdminCache = kvCacheFactory.create(new CacheOptions("sch-admin", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Integer, SchAdmin>() {
                    @Override
                    public SchAdmin findByKey(Integer key) throws Exception {
                        return schAdminRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Integer, SchAdmin> findByKeys(Collection<Integer> keys) throws Exception {
                        return schAdminRepository.findByIdIn(keys).stream()
                                .collect(Collectors.toMap(SchAdmin::getId, o -> o));
                    }
                }, new BeanModelConverter<>(SchAdmin.class));
        sessionCache = kvCacheFactory.create(new CacheOptions("sch-admin-session", 1, DateUtils.SECOND_PER_DAY),
                new SingleRepositoryProvider<String, SchAdminSession>() {
                    @Override
                    public SchAdminSession findByKey(String token) throws Exception {
                        return sessionRepository.findByToken(token);
                    }
                }, new BeanModelConverter<>(SchAdminSession.class));
        roleCache = kvCacheFactory.create(new CacheOptions("sch-role", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Integer, SchRole>() {
                    @Override
                    public SchRole findByKey(Integer key) throws Exception {
                        return schRoleRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Integer, SchRole> findByKeys(Collection<Integer> keys) throws Exception {
                        Map<Integer, SchRole> map = new HashMap<>();
                        for (Integer key : keys) {
                            map.put(key, findByKey(key));
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(SchRole.class));
    }

    @Transactional
    @Override
    public void saveSchAdmin(SchAdmin admin) {
        if (StringUtils.isEmpty(admin.getName())) {
            throw new ArgumentServiceException("name");
        }
        if (StringUtils.isNotChinaMobile(admin.getMobile())) {
            throw new ServiceException(ERR_MOBILE_INVALID);
        }
        if (!schRoleRepository.existsById(admin.getRoleId())) {
            throw new ArgumentServiceException("roleId");
        }

        SchAdmin exist = schAdminRepository.findByMobileAndSchoolId(admin.getMobile(), admin.getSchoolId());
        if (admin.getId() != null && admin.getId() > 0) {
            if (StringUtils.isNull(exist)) {
                throw new ServiceException(ERR_MOBILE_NOT_FOUNT);
            }
            exist.setName(admin.getName());
            exist.setRoleId(admin.getRoleId());
            exist.setEmail(admin.getEmail());
            if (StringUtils.isNotEmpty(admin.getPassword())) {
                if (StringUtils.validateStrongPassword(admin.getPassword()) == null) {
                    throw new ServiceException(ERR_PASSWORD_VALID_DENIED);
                }
                exist.setPassword(StringUtils.encryptPassword(admin.getPassword(), salt));
            }
            schAdminRepository.save(exist);
        } else {
            if (!StringUtils.isNull(exist)) {
                throw new ServiceException(ERR_MOBILE_EXIST);
            }
            if (StringUtils.validateStrongPassword(admin.getPassword()) == null) {
                throw new ServiceException(ERR_PASSWORD_VALID_DENIED);
            }
            admin.setPassword(StringUtils.encryptPassword(admin.getPassword(), salt));
            admin.setStatus(ByteUtils.BYTE_1);
            admin.setCreatedAt(System.currentTimeMillis());
            schAdminRepository.save(admin);
        }
    }

    @Override
    public List<SchAdmin> admins(SchAdminQo qo) {
        return schAdminRepository.findAllForList(qo);
    }

    @Override
    public Map<Integer, SchAdmin> findByIdIn(Set<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        return schAdminCache.findByKeys(ids);
    }

    @Override
    public void resetPsw(Integer id, String newPassword) {
        if (StringUtils.validatePassword(newPassword) == null) {
            throw new ArgumentServiceException("password");
        }
        getById(id);

        String strongPsw = StringUtils.encryptPassword(newPassword, salt);
        schAdminRepository.updatePassword(id, strongPsw);
    }

    @Override
    public SchAdmin profile() {
        return SchAdminContexts.getAdmin();
    }

    @Override
    public SchAdminSessionWrapper findByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new ArgumentServiceException("token");
        }

        SchAdminSession session = sessionCache.findByKey(token);
        if (session == null || session.getExpireAt() < System.currentTimeMillis()) {
            throw new SessionServiceException();
        }

        SchAdmin admin = getById(session.getSchAdminId());
        SchRole role = getRoleById(admin.getRoleId());
        return new SchAdminSessionWrapper(session, admin, role);
    }

    @Override
    public SchAdmin getById(Integer id) throws ServiceException {
        SchAdmin admin = findById(id);
        if (admin == null) {
            throw new ServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        return admin;
    }

    private SchAdmin findById(Integer id) {
        if (id == null) {
            throw new ArgumentServiceException(ERR_ID_NULL);
        }

        return schAdminCache.findByKey(id);
    }

    @Override
    public SchRole getRoleById(Integer id) {
        SchRole role = findRoleById(id);
        if (role == null) {
            throw new ServiceException(ERR_DATA_NOT_FOUND);
        }

        return role;
    }

    private SchRole findRoleById(Integer id) {
        if (id == null) {
            throw new ArgumentServiceException(ERR_ID_NULL);
        }

        return roleCache.findByKey(id);
    }

    @Transactional
    @Override
    public SchAdminSessionWrapper signin(SchAdmin schAdmin) {
        if (schAdmin == null) {
            throw new ArgumentServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        String mobile = schAdmin.getMobile();
        if (StringUtils.isNotChinaMobile(mobile)) {
            throw new ArgumentServiceException(ERR_MOBILE_INVALID);
        }

        SchAdmin newAdmin = schAdminRepository.findByMobileAndSchoolId(mobile, schAdmin.getSchoolId());
        if (newAdmin == null) {
            throw new ArgumentServiceException(ERR_MOBILE_NOT_FOUNT);
        }

        String password = schAdmin.getPassword();
        String strongPassword = StringUtils.validatePassword(password);
        if (StringUtils.isEmpty(strongPassword) || !strongPassword.equals(password)) {
            throw new ArgumentServiceException(ERR_PASSWORD_INVALID);
        }

        String valPassword = StringUtils.encryptPassword(schAdmin.getPassword(), salt);
        String curPassword = newAdmin.getPassword();
        if (StringUtils.isEmpty(curPassword) || !curPassword.equals(valPassword)) {
            throw new ArgumentServiceException(ERR_PASSWORD_INVALID);
        }

        Byte state = newAdmin.getStatus();
        if (state == null || state == AdminStatus.Disable.getState()) {
            throw new ServiceException(ERR_ACCOUNT_FORBID);
        }

        SchAdminSession adminSession = new SchAdminSession();
        String token = StringUtils.randomAlphanumeric(64);
        Long curTime = System.currentTimeMillis();
        Long expireTime = curTime + tokenHours * 3600 * 1000;
        adminSession.setToken(token);
        adminSession.setExpireAt(expireTime);
        adminSession.setSchAdminId(newAdmin.getId());
        adminSession.setSigninAt(curTime);

        sessionRepository.save(adminSession);
        SchRole role = getRoleById(newAdmin.getRoleId());
        SchAdminSessionWrapper adminSessionWrapper = new SchAdminSessionWrapper(adminSession, newAdmin, role);

        return adminSessionWrapper;
    }

    @Transactional
    @Override
    public void saveRole(SchRole role) {
        if (role == null) {
            throw new ArgumentServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        if (role.getSchoolId() == null) {
            role.setSchoolId(SchAdminContexts.getSchoolId());
        }

        String name = role.getName();
        if (StringUtils.isEmpty(name)) {
            throw new ArgumentServiceException(ERR_ILLEGAL_ARGUMENT);
        }
        SchRole exist = schRoleRepository.findByNameAndSchoolId(role.getName(), role.getSchoolId());

        if (exist != null) {
            if (role.getId() == null || (role.getId() != null && exist.getId() != role.getId())) {
                throw new DetailedServiceException("角色名字重复");
            }
        }

        if (CollectionUtils.isEmpty(role.getPermissions())) {
            throw new ArgumentServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        schRoleRepository.save(role);
        roleCache.remove(role.getId());
    }

    @Transactional
    @Override
    public void removeRole(Integer id) {
        if (id == null) {
            throw new ArgumentServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        schRoleRepository.deleteById(id);
        roleCache.remove(id);
    }

    @Override
    public List<SchRole> roles() {
        Integer schoolId = SchAdminContexts.getSchoolId();
        List<SchRole> roles = schRoleRepository.findBySchoolId(schoolId);
        wrapRoles(roles);
        return roles;
    }

    @Override
    public List<SchRole> roles(Integer schoolId) {
        List<SchRole> roles = schRoleRepository.findBySchoolId(schoolId);
        wrapRoles(roles);
        return roles;
    }

    private void wrapRoles(Collection<SchRole> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return;
        }

        SchAdminPermission[] adminPermissionArr = SchAdminPermission.values();

        List<Permission> detailPermissions;
        List<String> permissionList;
        for (SchRole role : roles) {
            permissionList = role.getPermissions();
            detailPermissions = new ArrayList<>();
            for (SchAdminPermission p : adminPermissionArr) {
                String curPermissionName = p.name();
                if (permissionList.contains(curPermissionName)) {
                    Permission permission = new Permission(curPermissionName, p.getVal(), p.getLevel());
                    detailPermissions.add(permission);
                }
            }

            role.setDetailPermissions(detailPermissions);
        }
    }

    @Override
    public List<Permission> permissions() {
        SchAdminPermission[] adminPermissionArr = SchAdminPermission.values();

        List<Permission> permissions = new ArrayList<>();
        for (SchAdminPermission adminPermission : adminPermissionArr) {
            permissions
                    .add(new Permission(adminPermission.name(), adminPermission.getVal(), adminPermission.getLevel()));
        }

        return permissions;
    }

    @Override
    public SchAdmin admin(Integer id) {
        SchAdmin admin = getById(id);
        admin.setPassword(null);
        return admin;
    }

    @Override
    public void remove(Integer id) {
        if (id == null) {
            throw new ArgumentServiceException(ERR_ID_NULL);
        }

        schAdminRepository.deleteById(id);
        schAdminCache.remove(id);
        sessionRepository.deleteAllByAdminId(id);
    }

    @Override
    public void updatePassword(String newPassword, String oldPassword) {

    }

    @Override
    public Page<SchAdminSession> adminSessions(SchAdminSessionQo qo) {
        return sessionRepository.findAll(qo);
    }

    @Override
    public Page<SchAdmin> admins() {
        SchAdminQo qo = new SchAdminQo();
        qo.setSchoolId(SchAdminContexts.getSchoolId());
        Page<SchAdmin> page = schAdminRepository.findAll(qo);
        wrapAdmins(page.getContent());
        return page;
    }

    private void wrapAdmins(List<SchAdmin> admins) {
        if (!CollectionUtils.isEmpty(admins)) {
            Set<Integer> roleIds = admins.stream().map(admin -> admin.getRoleId()).collect(Collectors.toSet());
            Map<Integer, SchRole> rMap = roleCache.findByKeys(roleIds);
            admins.stream().forEach((admin) -> {
                SchRole role = rMap.get(admin.getRoleId());
                admin.setRole(role);
            });
        }
    }

    @Override
    public void status(Integer id, Byte state) {
        SchAdmin admin = getById(id);
        admin.setStatus(state);

        schAdminRepository.save(admin);
    }
}