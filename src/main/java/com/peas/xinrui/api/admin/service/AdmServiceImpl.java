package com.peas.xinrui.api.admin.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.peas.xinrui.api.admin.entity.AdmError;
import com.peas.xinrui.api.admin.entity.AdminContexts;
import com.peas.xinrui.api.admin.entity.AdminPermission;
import com.peas.xinrui.api.admin.entity.AdminSessionWrapper;
import com.peas.xinrui.api.admin.entity.AdminStatus;
import com.peas.xinrui.api.admin.model.Admin;
import com.peas.xinrui.api.admin.model.AdminSession;
import com.peas.xinrui.api.admin.model.Role;
import com.peas.xinrui.api.admin.qo.AdminQo;
import com.peas.xinrui.api.admin.qo.SessionQo;
import com.peas.xinrui.api.admin.repository.AdmRepository;
import com.peas.xinrui.api.admin.repository.AdminSessionRepository;
import com.peas.xinrui.api.admin.repository.RoleRepository;
import com.peas.xinrui.api.user.model.User;
import com.peas.xinrui.api.user.qo.UserQo;
import com.peas.xinrui.api.user.service.UserService;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.cache.SingleRepositoryProvider;
import com.peas.xinrui.common.cache.kvGlobal.KvGlobalSearchCache;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.DataNotFoundServiceException;
import com.peas.xinrui.common.exception.DetailedServiceException;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.exception.SessionServiceException;
import com.peas.xinrui.common.kvCache.KvCache;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.model.ByteUtils;
import com.peas.xinrui.common.model.Permission;
import com.peas.xinrui.common.repository.support.DataQueryObjectPage;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.DateUtils;
import com.peas.xinrui.common.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdmServiceImpl implements AdmService, AdmError {
    @Autowired
    private AdmRepository admRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminSessionRepository sessionRepository;

    @Autowired
    private UserService userService;

    @Value("${admin.salt}")
    private String salt;

    @Value("${admin.tokenHour}")
    private Integer tokenHours;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCache<Long, Admin> adminCache;

    private KvCache<String, AdminSession> sessionCache;

    private KvCache<Integer, Role> roleCache;

    private KvGlobalSearchCache<Object, String> globalCache;

    @PostConstruct
    public void init() {
        adminCache = kvCacheFactory.create(new CacheOptions("admin", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, Admin>() {
                    @Override
                    public Admin findByKey(Long key) throws Exception {
                        return admRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Long, Admin> findByKeys(Collection<Long> keys) throws Exception {
                        Map<Long, Admin> map = new HashMap<>();
                        for (Long key : keys) {
                            Admin admin = findById(key);
                            map.put(key, admin);
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(Admin.class));

        sessionCache = kvCacheFactory.create(new CacheOptions("admin-session", 1, DateUtils.SECOND_PER_DAY),
                new SingleRepositoryProvider<String, AdminSession>() {
                    @Override
                    public AdminSession findByKey(String token) throws Exception {
                        return sessionRepository.findByToken(token);
                    }
                }, new BeanModelConverter<>(AdminSession.class));

        roleCache = kvCacheFactory.create(new CacheOptions("role", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Integer, Role>() {
                    @Override
                    public Role findByKey(Integer key) throws Exception {
                        return roleRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Integer, Role> findByKeys(Collection<Integer> keys) throws Exception {
                        return null;
                    }
                }, new BeanModelConverter<>(Role.class));

        globalCache = kvCacheFactory.createGlobalCacheWrap();
    }

    @Transactional
    @Override
    public void save(Admin admin) {
        if (StringUtils.isEmpty(admin.getName())) {
            throw new ArgumentServiceException("name");
        }
        if (StringUtils.isNotChinaMobile(admin.getMobile())) {
            throw new ServiceException(ERR_MOBILE_INVALID);
        }
        if (!roleRepository.existsById(admin.getRoleId())) {
            throw new ArgumentServiceException("roleId");
        }
        Admin exist = admRepository.findByMobile(admin.getMobile());
        if (admin.getId() != null && admin.getId() > 0) {
            if (StringUtils.isNull(exist)) {
                throw new ServiceException(ERR_MOBILE_NOT_FOUNT);
            }
            exist.setName(admin.getName());
            exist.setRoleId(admin.getRoleId());
            exist.setImg(admin.getImg());
            exist.setEmail(admin.getEmail());
            if (StringUtils.isNotEmpty(admin.getPassword())) {
                if (StringUtils.validateStrongPassword(admin.getPassword()) == null) {
                    throw new ServiceException(ERR_PASSWORD_VALID_DENIED);
                }
                exist.setPassword(StringUtils.encryptPassword(admin.getPassword(), salt));
            }
            admRepository.save(exist);
            adminCache.remove(admin.getId());
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
            admRepository.save(admin);
        }
    }

    @Override
    public Admin getById(Long id) throws ServiceException {
        Admin admin = findById(id);
        if (admin == null) {
            throw new ServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        return admin;
    }

    @Override
    public List<Admin> adminsByIds(Set<Long> ids) {
        return admRepository.findByIdIn(ids);
    }

    @Override
    public Map<Long, Admin> adminsByIdsForMap(Collection<Long> ids) {
        return adminCache.findByKeys(ids);
    }

    @Transactional
    @Override
    public void saveRole(Role role) {
        if (role == null) {
            throw new ArgumentServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        String name = role.getName();
        if (StringUtils.isEmpty(name)) {
            throw new ArgumentServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        if (CollectionUtils.isEmpty(role.getPermissions())) {
            throw new ArgumentServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        Role exist = roleRepository.findByName(name);
        if (exist != null) {
            if (role.getId() == null || (role.getId() != null && exist.getId() != role.getId())) {
                throw new DetailedServiceException("角色名字重复");
            }
            roleRepository.save(role);
            roleCache.remove(role.getId());
        }

        roleRepository.save(role);
    }

    @Transactional
    @Override
    public void removeRole(Integer id) {
        if (id == null) {
            throw new ArgumentServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        roleRepository.deleteById(id);
        roleCache.remove(id);
    }

    @Override
    public List<Role> roles() {
        List<Role> roles = roleRepository.findAllForList(new DataQueryObjectPage());
        wrapRoles(roles);
        return roles;
    }

    @Override
    public Role getRoleById(Integer id) {
        Role role = findRoleById(id);
        if (role == null) {
            throw new ServiceException(ERR_DATA_NOT_FOUND);
        }

        return role;
    }

    @Override
    public List<Permission> permissions() {
        AdminPermission[] adminPermissionArr = AdminPermission.values();

        List<Permission> permissions = new ArrayList<>();
        for (AdminPermission adminPermission : adminPermissionArr) {
            permissions
                    .add(new Permission(adminPermission.name(), adminPermission.getVal(), adminPermission.getLevel()));
        }

        return permissions;
    }

    @Transactional
    @Override
    public AdminSessionWrapper signin(Admin admin) {
        if (admin == null) {
            throw new ArgumentServiceException(ERR_ILLEGAL_ARGUMENT);
        }

        String mobile = admin.getMobile();
        if (StringUtils.isNotChinaMobile(mobile)) {
            throw new ArgumentServiceException(ERR_MOBILE_INVALID);
        }

        Admin newAdmin = admRepository.findByMobile(mobile);
        if (newAdmin == null) {
            throw new ArgumentServiceException(ERR_MOBILE_NOT_FOUNT);
        }

        String password = admin.getPassword();
        String strongPassword = StringUtils.validatePassword(password);
        if (StringUtils.isEmpty(strongPassword) || !strongPassword.equals(password)) {
            throw new ArgumentServiceException(ERR_PASSWORD_INVALID);
        }

        String valPassword = StringUtils.encryptPassword(admin.getPassword(), salt);
        String curPassword = newAdmin.getPassword();
        if (StringUtils.isEmpty(curPassword) || !curPassword.equals(valPassword)) {
            throw new ArgumentServiceException(ERR_PASSWORD_INCONSISTENT);
        }

        Byte state = newAdmin.getStatus();
        if (state == null || state == AdminStatus.Disable.getState()) {
            throw new ServiceException(ERR_ACCOUNT_FORBID);
        }

        AdminSession adminSession = new AdminSession();
        String token = StringUtils.randomAlphanumeric(64);
        Long curTime = System.currentTimeMillis();
        Long expireTime = curTime + tokenHours * 3600 * 1000;
        adminSession.setToken(token);
        adminSession.setExpireAt(expireTime);
        adminSession.setAdminId(newAdmin.getId());
        adminSession.setSigninAt(curTime);

        sessionRepository.save(adminSession);
        Role role = getRoleById(newAdmin.getRoleId());
        AdminSessionWrapper adminSessionWrapper = new AdminSessionWrapper(adminSession, newAdmin, role);

        return adminSessionWrapper;
    }

    @Override
    public AdminSessionWrapper findByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new ArgumentServiceException("token");
        }

        AdminSession session = sessionCache.findByKey(token);
        if (session == null || session.getExpireAt() < System.currentTimeMillis()) {
            throw new SessionServiceException();
        }

        Admin admin = getById(session.getAdminId());
        Role role = getRoleById(admin.getRoleId());
        return new AdminSessionWrapper(session, admin, role);
    }

    @Override
    public Admin profile() {
        return AdminContexts.getAdmin();
    }

    @Transactional
    @Override
    public void updatePassword(String newPassword, String oldPassword) {
        String newStrongPassword = StringUtils.validateStrongPassword(newPassword);
        String oldStrongPassword = StringUtils.validatePassword(oldPassword);
        if (StringUtils.isEmpty(newStrongPassword) || StringUtils.isEmpty(oldStrongPassword)) {
            throw new ArgumentServiceException(ERR_PASSWORD_INVALID);
        }

        Admin admin = admRepository.findById(AdminContexts.getAdminId()).orElse(null);
        if (admin == null) {
            throw new DataNotFoundServiceException();
        }

        if (!admin.getPassword().equals(StringUtils.encryptPassword(oldStrongPassword, salt))) {
            throw new ServiceException(ERR_PASSWORD_INVALID);
        }

        newPassword = StringUtils.encryptPassword(newStrongPassword, salt);
        admin.setPassword(newPassword);

        admRepository.save(admin);
        adminCache.remove(admin.getId());
    }

    @Transactional
    @Override
    public void remove(Long id) {
        if (id == null) {
            throw new ArgumentServiceException(ERR_ID_NULL);
        }

        admRepository.deleteById(id);
        adminCache.remove(id);
        sessionRepository.deleteAllByAdminId(id);
    }

    @Override
    public Page<Admin> admins() {
        Page<Admin> page = admRepository.findAll(new AdminQo());

        wrapAdmins(page.getContent());
        return page;
    }

    private void wrapAdmins(List<Admin> admins) {
        if (!CollectionUtils.isEmpty(admins)) {
            Set<Integer> roleIds = admins.stream().map(admin -> admin.getRoleId()).collect(Collectors.toSet());
            Map<Integer, Role> rMap = roleCache.findByKeys(roleIds);
            admins.stream().forEach((admin) -> {
                Role role = rMap.get(admin.getRoleId());
                admin.setRole(role);
            });
        }
    }

    @Override
    public Admin admin(Long id) {
        return getById(id);
    }

    @Override
    public void status(Long id, Byte state) {
        Admin admin = getById(id);
        admin.setStatus(state);

        admRepository.save(admin);
    }

    @Override
    public Page<AdminSession> sessions(SessionQo qo) {
        return sessionRepository.findAll(qo);
    }

    @Override
    public Page<User> users(UserQo qo) {
        return userService.users(qo);
    }

    @Override
    public User user(Long userId) {
        return userService.getById(userId);
    }

    @Override
    public void stateUser(Long userId, Byte state) {
        userService.state(userId, state);
    }

    @Override
    public Map<Long, Admin> likeForAdmin(String pattern) {
        return adminCache.findLike(pattern);
    }

    @Override
    public Map<Object, String> globalLike(String pattern) {
        return globalCache.findLike(pattern);
    }

    private void wrapRoles(Collection<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return;
        }

        AdminPermission[] adminPermissionArr = AdminPermission.values();

        List<Permission> detailPermissions;
        List<String> permissionList;
        for (Role role : roles) {
            permissionList = role.getPermissions();
            if (CollectionUtils.isEmpty(permissionList)) {
                break;
            }
            detailPermissions = new ArrayList<>();
            for (AdminPermission p : adminPermissionArr) {
                String curPermissionName = p.name();
                if (permissionList.contains(curPermissionName)) {
                    Permission permission = new Permission(curPermissionName, p.getVal(), p.getLevel());
                    detailPermissions.add(permission);
                }
            }

            role.setDetailPermissions(detailPermissions);
        }
    }

    private Admin findById(Long id) {
        if (id == null) {
            throw new ArgumentServiceException(ERR_ID_NULL);
        }

        return adminCache.findByKey(id);
    }

    private Role findRoleById(Integer id) {
        if (id == null) {
            throw new ArgumentServiceException(ERR_ID_NULL);
        }

        return roleCache.findByKey(id);
    }
}