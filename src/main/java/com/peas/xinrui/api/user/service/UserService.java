package com.peas.xinrui.api.user.service;

import java.util.Map;
import java.util.Set;

import com.peas.xinrui.api.heavywork.model.HeavyWork;
import com.peas.xinrui.api.sms.model.ValCode;
import com.peas.xinrui.api.user.entity.UserSessionWrapper;
import com.peas.xinrui.api.user.model.User;
import com.peas.xinrui.api.user.qo.UserQo;
import com.peas.xinrui.common.exception.ServiceException;

import org.springframework.data.domain.Page;

public interface UserService {
    UserSessionWrapper signin(User user, ValCode valCode);

    Set<User> findByIdIn(Set<Long> ids);

    Map<Long, User> findByIds(Set<Long> ids);

    Page<User> users(UserQo qo);

    User getById(Long id);

    UserSessionWrapper findByToken(String token);

    User findByIdentity(String identity);

    User profile();

    void saveProfile(User user);

    void validMobile(User user, ValCode valCode);

    void modProfileMobile(User user, ValCode valCode);

    void state(Long id, Byte state);

    HeavyWork exportUsers(UserQo qo) throws ServiceException;

    void status(Long id, Byte state);

    void saveUser(User user);

    void auth(User user);
}