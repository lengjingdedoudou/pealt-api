package com.peas.xinrui.api.user.service;

import java.util.Collection;
import java.util.Map;

import com.peas.xinrui.api.user.model.UserCourseCollect;
import com.peas.xinrui.api.user.qo.UserCourseCollectQo;

import org.springframework.data.domain.Page;

public interface UserCollectService {

    void collect(Long courseId);

    Map<Long, UserCourseCollect> findByCollectIdIn(Long userId, Collection<Long> ids);

    Page<UserCourseCollect> userCollects(UserCourseCollectQo qo);

    void removeMyCollect(int id);

    Integer userCollectNum(Long userId, Long courseId);
}