package com.peas.xinrui.api.user.service;

import java.util.Collection;
import java.util.Map;

import com.peas.xinrui.api.course.service.CourseService;
import com.peas.xinrui.api.user.entity.UserContexts;
import com.peas.xinrui.api.user.model.UserCourseCollect;
import com.peas.xinrui.api.user.qo.UserCourseCollectQo;
import com.peas.xinrui.api.user.repository.UserCourseCollectRepository;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.model.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UserCollectServiceImpl implements UserCollectService {

    @Autowired
    private UserCourseCollectRepository collectRepository;

    @Autowired
    private CourseService courseService;

    @Override
    public void collect(Long courseId) {
        if (courseId == null) {
            throw new ArgumentServiceException("courseId");
        }
        UserCourseCollect exist = collectRepository.findByUserIdAndCourseId(UserContexts.getUserId(), courseId);
        int num = 0;
        if (exist != null) {
            collectRepository.deleteByUserIdAndCourseId(UserContexts.getUserId(), courseId);
            num--;
        } else {
            collectRepository.save(new UserCourseCollect(UserContexts.getUserId(), courseId));
            num++;
        }

        courseService.updateCollectNum(courseId, num);
    }

    @Override
    public Map<Long, UserCourseCollect> findByCollectIdIn(Long userId, Collection<Long> ids) {

        return null;
    }

    @Override
    public void removeMyCollect(int id) {

    }

    @Override
    public Page<UserCourseCollect> userCollects(UserCourseCollectQo qo) {
        // TODO Auto-generated method stub
        return null;
    }

    private UserCourseCollect writeableUserCourseCollect(Long id) {
        UserCourseCollect collect = getById(id);
        Long requestId = UserContexts.getUserId();
        if (collect.getUserId() != requestId) {
            throw new ServiceException(ErrorCode.ERR_PERMISSION_DENIED);
        }

        return collect;
    }

    private UserCourseCollect findById(Long id) {
        if (id == null || id == 0) {
            throw new ArgumentServiceException("id");
        }
        return collectRepository.findById(id).orElse(null);
    }

    private UserCourseCollect getById(Long id) {
        UserCourseCollect userCollect = findById(id);
        if (userCollect == null) {
            throw new ServiceException(ErrorCode.ERR_DATA_NOT_FOUND);
        }
        return userCollect;
    }

    @Override
    public Integer userCollectNum(Long userId, Long courseId) {
        // TODO Auto-generated method stub
        return null;
    }

}