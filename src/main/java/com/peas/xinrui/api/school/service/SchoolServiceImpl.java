package com.peas.xinrui.api.school.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.peas.xinrui.api.admin.entity.AdminSessionWrapper;
import com.peas.xinrui.api.course.entity.AuthCourse;
import com.peas.xinrui.api.course.model.Course;
import com.peas.xinrui.api.course.model.CoursePkg;
import com.peas.xinrui.api.course.service.CourseService;
import com.peas.xinrui.api.schadmin.entity.SchAdminPermission;
import com.peas.xinrui.api.schadmin.model.SchAdmin;
import com.peas.xinrui.api.schadmin.model.SchRole;
import com.peas.xinrui.api.schadmin.qo.SchAdminQo;
import com.peas.xinrui.api.schadmin.service.SchAdminService;
import com.peas.xinrui.api.school.model.School;
import com.peas.xinrui.api.school.qo.SchoolQo;
import com.peas.xinrui.api.school.repository.SchoolRepository;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.DataNotFoundServiceException;
import com.peas.xinrui.common.exception.DetailedServiceException;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.kvCache.KvCache;
import com.peas.xinrui.common.kvCache.RepositoryProvider;
import com.peas.xinrui.common.kvCache.converter.BeanModelConverter;
import com.peas.xinrui.common.model.ByteUtils;
import com.peas.xinrui.common.model.Duration;
import com.peas.xinrui.common.utils.CollectionUtils;
import com.peas.xinrui.common.utils.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCache<Integer, School> schoolCache;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SchAdminService schAdminService;

    @Autowired
    private CourseService courseService;

    @PostConstruct
    private void init() {
        schoolCache = kvCacheFactory.create(new CacheOptions("school", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Integer, School>() {
                    @Override
                    public School findByKey(Integer key) throws Exception {
                        return schoolRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Integer, School> findByKeys(Collection<Integer> keys) throws Exception {
                        return schoolRepository.findByIdIn(keys).stream()
                                .collect(Collectors.toMap(School::getId, o -> o));
                    }
                }, new BeanModelConverter<>(School.class));
    }

    @Override
    public AdminSessionWrapper findByToken(String token) {
        return null;
    }

    @Transactional
    @Override
    public void saveSchool(School school) {
        long curTime = System.currentTimeMillis();
        String name = school.getName();
        if (name.length() < 3 || name.length() > 20) {
            throw new ArgumentServiceException("name");
        }
        if (school.getId() == null) {
            school.setStatus(ByteUtils.BYTE_0);
            school.setCreatedAt(curTime);
            School existSchool = schoolRepository.saveAndFlush(school);

            SchRole schRole = new SchRole();
            List<String> permissions = new ArrayList<>();

            permissions.add(SchAdminPermission.ROLE_EDIT.name());
            permissions.add(SchAdminPermission.ROLE_LIST.name());
            permissions.add(SchAdminPermission.ADMIN_EDIT.name());
            permissions.add(SchAdminPermission.ADMIN_LIST.name());

            schRole.setSchoolId(existSchool.getId());
            schRole.setPermissions(permissions);
            schRole.setName("超级管理员");
            schAdminService.saveRole(schRole);
        } else {
            schoolRepository.save(school);
            schoolCache.remove(school.getId());
        }
    }

    @Override
    public Page<School> schools(SchoolQo qo) {
        return schoolRepository.findAll(qo);
    }

    @Override
    public School item(Integer id) {
        School school = school(id);

        SchAdminQo qo = new SchAdminQo();
        qo.setSchoolId(id);
        List<SchAdmin> sAdmins = schAdminService.admins(qo);
        school.setAdmins(sAdmins);

        wrapAuthCourse(school.getAuthCourses());
        return school;
    }

    private void wrapAuthCourse(List<AuthCourse> authCourses) {
        if (!CollectionUtils.isEmpty(authCourses)) {
            Set<Long> cIds = new HashSet<>();
            Set<Long> pIds = new HashSet<>();

            for (AuthCourse authCourse : authCourses) {
                cIds.add(authCourse.getCourseId());
                List<Long> pkgIds = authCourse.getPkgIds();
                pIds.addAll(pkgIds);
            }
            pIds.remove(null);
            Map<Long, Course> cMap = courseService.coursesForMap(cIds);
            Map<Long, CoursePkg> cpMap = courseService.coursePkgsForMap(pIds);

            for (AuthCourse authCourse : authCourses) {
                authCourse.setCourse(cMap.get(authCourse.getCourseId()));
                List<CoursePkg> cpList = new ArrayList<>();
                for (Long cpId : authCourse.getPkgIds()) {
                    cpList.add(cpMap.get(cpId));
                }
                authCourse.setCoursePkgs(cpList);
            }
        }
    }

    @Override
    public Map<Integer, School> schools(Collection<Integer> ids) {
        return schoolCache.findByKeys(ids);
    }

    @Override
    public List<Integer> schoolsSearch(String keyWord, Set<Integer> ids) {
        return schoolRepository.findIdsByKeyWordAndIds(keyWord, ids);
    }

    @Override
    public School school(Integer id) {
        School school = findById(id);
        if (school == null) {
            throw new DataNotFoundServiceException("school");
        }
        return school;
    }

    private School findById(Integer id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        return schoolCache.findByKey(id);
    }

    @Transactional
    @Override
    public void schoolStatus(Integer id, Byte status) {
        schoolRepository.updateStatus(id, status);
        schoolCache.remove(id);
    }

    @Transactional
    @Override
    public void renewSchool(Integer schoolId, String duration) {
        School school = school(schoolId);
        long validThru = parseDuration(school, duration);
        schoolRepository.updateValidThru(school.getId(), validThru);
        schoolCache.remove(school.getId());
    }

    @Override
    public int expireCount(Long time) {
        if (time == null) {
            throw new DetailedServiceException("时间不能为空");
        }
        return schoolRepository.countByValidThru(time);
    }

    private long parseDuration(School school, String duration) throws ServiceException {
        Duration dur = Duration.parse(duration);
        if (dur == null || dur.forever()) {
            throw new ArgumentServiceException("Bad duration");
        }
        long now = System.currentTimeMillis();
        Long validThru = school.getValidThru();
        if (validThru == null) {
            validThru = now;
        }
        return dur.addDate(new Date(validThru > now ? validThru : now)).getTime();
    }

    @Override
    public Map<String, Object> oem(Integer schoolId) {
        if (schoolId == null) {
            throw new ArgumentServiceException("schoolId");
        }

        School school = schoolCache.findByKey(schoolId);
        if (school == null) {
            throw new DetailedServiceException("school not exists");
        }
        return CollectionUtils.arrayAsMap("name", school.getName(), "oem", school.getOem());
    }

    @Transactional
    @Override
    public void updateAuthCourses(List<AuthCourse> authCourses, Integer schoolId) {
        if (CollectionUtils.isEmpty(authCourses)) {
            throw new ArgumentServiceException("authCourses");
        }
        School school = school(schoolId);
        school.setAuthCourses(authCourses);
        schoolRepository.save(school);
        schoolCache.remove(schoolId);
    }

    @Override
    public List<Long> authCourseIds(Integer schoolId) {
        School school = school(schoolId);
        List<AuthCourse> authCourses = school.getAuthCourses();
        List<Long> ids = null;
        if (CollectionUtils.isNotEmpty(authCourses)) {
            ids = authCourses.stream().map((authCourse) -> authCourse.getCourseId()).collect(Collectors.toList());
        }
        return ids;
    }
}