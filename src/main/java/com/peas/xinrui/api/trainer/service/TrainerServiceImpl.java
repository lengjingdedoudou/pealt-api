package com.peas.xinrui.api.trainer.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.peas.xinrui.api.category.entity.CategoryTypeEnum;
import com.peas.xinrui.api.category.model.Category;
import com.peas.xinrui.api.category.service.CategoryService;
import com.peas.xinrui.api.course.entity.CourseWO;
import com.peas.xinrui.api.course.model.CourseSchool;
import com.peas.xinrui.api.course.service.CourseService;
import com.peas.xinrui.api.schadmin.entity.SchAdminContexts;
import com.peas.xinrui.api.trainer.entity.TrainerWO;
import com.peas.xinrui.api.trainer.model.Trainer;
import com.peas.xinrui.api.trainer.model.UserTrainerFollow;
import com.peas.xinrui.api.trainer.qo.TrainerQo;
import com.peas.xinrui.api.trainer.repository.TrainerRepository;
import com.peas.xinrui.api.trainer.repository.UserTrainerFollowRepository;
import com.peas.xinrui.api.user.entity.UserContexts;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.controller.auth.SessionType;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.exception.SessionServiceException;
import com.peas.xinrui.common.kvCache.KvCache;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.model.ByteUtils;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.DateUtils;
import com.peas.xinrui.common.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserTrainerFollowRepository userFollowRepository;

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCache<Long, Trainer> trainerCache;

    @PostConstruct
    private void init() {
        trainerCache = kvCacheFactory.create(new CacheOptions("trainer", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, Trainer>() {
                    @Override
                    public Trainer findByKey(Long key) throws Exception {
                        return trainerRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Long, Trainer> findByKeys(Collection<Long> keys) throws Exception {
                        keys = keys.stream().filter((key) -> key != 0).collect(Collectors.toList());
                        Map<Long, Trainer> map = new HashMap<>();
                        for (Long key : keys) {
                            map.put(key, findByKey(key));
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(Trainer.class));
    }

    @Override
    public void saveTrainer(Trainer trainer) {
        Integer schoolId = SchAdminContexts.getSchoolId();
        if (trainer == null) {
            throw new ServiceException(1);
        }
        if (StringUtils.isEmpty(trainer.getName()) || trainer.getName().length() > 10) {
            throw new ArgumentServiceException("name");
        }
        if (StringUtils.isNotChinaMobile(trainer.getMobile())) {
            throw new ArgumentServiceException("mobile");
        }

        Category category = categoryService.category(trainer.getCategoryId());
        if (category == null || category.getSchoolId() != schoolId
                || category.getType() != CategoryTypeEnum.Trainer.getVal()) {
            throw new ArgumentServiceException("categoryId");
        }

        if (trainer.getId() == null) {
            trainer.setStatus(ByteUtils.BYTE_1);
            trainer.setCreatedAt(System.currentTimeMillis());
            trainer.setSchoolId(schoolId);
        } else {
            Trainer exist = getById(trainer.getId());
            if (exist == null) {
                throw new ServiceException(1);
            }
            if (exist.getSchoolId() != schoolId) {
                throw new ServiceException(1);
            }
        }

        trainerRepository.save(trainer);
    }

    @Override
    public Page<Trainer> trainers(TrainerQo qo, TrainerWO wo, SessionType type) {
        Page<Trainer> page = trainerRepository.findAll(qo);
        wrapTrainers(page.getContent(), wo, type);

        return page;
    }

    @Override
    public Trainer trainer(Long id, TrainerWO wo, SessionType type) {
        Trainer trainer = getById(id);
        List<Trainer> trainers = Collections.singletonList(trainer);
        wrapTrainers(trainers, wo, type);
        return trainers.get(0);
    }

    private void wrapTrainers(Collection<Trainer> trainers, TrainerWO wo, SessionType type) {
        if (CollectionUtils.isNotEmpty(trainers)) {
            List<Long> ids = trainers.stream().map((t) -> t.getId()).collect(Collectors.toList());

            if (wo.isWithCategory()) {
                Set<Long> cIds = trainers.stream().map((c) -> c.getCategoryId()).collect(Collectors.toSet());
                Map<Long, Category> cMap = categoryService.categorysForMap(cIds);
                trainers.stream().forEach(t -> t.setCategory(cMap.get(t.getCategoryId()).baseCategory()));
            }

            if (wo.isWithCourseList()) {
                List<CourseSchool> courseSchools = courseService.courseSchoolsByTrainerIds(ids,
                        CourseWO.getAllInstance().setList(true), SessionType.USER);
                Map<Long, List<CourseSchool>> ctMap = courseSchools.stream()
                        .collect(Collectors.groupingBy(CourseSchool::getTrainerId));

                trainers.stream().forEach(t -> t.setCourseSchools(ctMap.get(t.getId())));
            }

            if (type == SessionType.USER) {
                trainers.stream().forEach(t -> {
                    UserTrainerFollow follow = userFollowRepository.findByUserIdAndTrainerId(UserContexts.getUserId(),
                            t.getId());

                    if (follow != null) {
                        t.setIsFollow(true);
                    } else {
                        t.setIsFollow(false);
                    }
                });
            }

        }
    }

    @Override
    public Map<Long, Trainer> trainersForMap(List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            Map<Long, Trainer> map = trainerCache.findByKeys(ids);
            wrapTrainers(map.values(), TrainerWO.getCategoryInstance(), SessionType.SCHADMIN);
            return map;
        }
        return null;
    }

    private Trainer findById(Long id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        return trainerRepository.findById(id).orElse(null);
    }

    private Trainer getById(Long id) {
        Trainer trainer = findById(id);
        if (trainer == null) {
            throw new ArgumentServiceException("trainer");
        }
        return trainer;
    }

    @Transactional
    @Override
    public void follow(Long trainerId, SessionType type) {
        long id = 0;
        if (type == SessionType.USER) {
            id = UserContexts.getUserId();
        }
        if (type == SessionType.TRAINER) {
            // TODO
        }
        if (id == 0) {
            throw new SessionServiceException();
        }
        Trainer trainer = getById(trainerId);

        UserTrainerFollow follow = userFollowRepository.findByUserIdAndTrainerId(id, trainerId);
        int fans = trainer.getFans();
        if (follow == null) {
            userFollowRepository.save(new UserTrainerFollow(id, trainerId, System.currentTimeMillis()));
            trainerRepository.updateFansById(trainerId, ++fans);
        } else {
            userFollowRepository.deleteById(follow.getId());
            if (fans <= 0) {
                throw new ServiceException(1);
            }
            trainerRepository.updateFansById(trainerId, --fans);
        }

        trainerCache.remove(trainerId);
    }

}