package com.peas.xinrui.api.course.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.peas.xinrui.api.category.model.Category;
import com.peas.xinrui.api.category.service.CategoryService;
import com.peas.xinrui.api.course.entity.AuthCourse;
import com.peas.xinrui.api.course.entity.BatchStatusDTO;
import com.peas.xinrui.api.course.entity.CourseTypeEnum;
import com.peas.xinrui.api.course.entity.CourseWO;
import com.peas.xinrui.api.course.model.Chapter;
import com.peas.xinrui.api.course.model.Course;
import com.peas.xinrui.api.course.model.CoursePkg;
import com.peas.xinrui.api.course.model.CourseSchool;
import com.peas.xinrui.api.course.model.Lesson;
import com.peas.xinrui.api.course.qo.ChapterQo;
import com.peas.xinrui.api.course.qo.CourseQo;
import com.peas.xinrui.api.course.qo.CourseSchoolQo;
import com.peas.xinrui.api.course.qo.LessonQo;
import com.peas.xinrui.api.course.repository.ChapterRepository;
import com.peas.xinrui.api.course.repository.CoursePkgRepository;
import com.peas.xinrui.api.course.repository.CourseRepository;
import com.peas.xinrui.api.course.repository.CourseSchoolRepository;
import com.peas.xinrui.api.course.repository.LessonRepository;
import com.peas.xinrui.api.schadmin.entity.SchAdminContexts;
import com.peas.xinrui.api.school.service.SchoolService;
import com.peas.xinrui.api.trainer.model.Trainer;
import com.peas.xinrui.api.trainer.service.TrainerService;
import com.peas.xinrui.api.user.entity.UserContexts;
import com.peas.xinrui.api.user.model.UserCourseCollect;
import com.peas.xinrui.api.user.repository.UserCourseCollectRepository;
import com.peas.xinrui.common.cache.CacheOptions;
import com.peas.xinrui.common.cache.KvCacheFactory;
import com.peas.xinrui.common.controller.auth.SessionType;
import com.peas.xinrui.common.exception.ArgumentServiceException;
import com.peas.xinrui.common.exception.DataNotFoundServiceException;
import com.peas.xinrui.common.exception.DetailedServiceException;
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
public class CourseServiceImpl implements CourseService {

    @Autowired
    private KvCacheFactory kvCacheFactory;

    private KvCache<Long, Course> courseCache;

    private KvCache<Long, CoursePkg> coursePkgCache;

    private KvCache<Long, Lesson> lessonCache;

    private KvCache<Long, Chapter> chapterCache;

    private KvCache<Long, CourseSchool> courseSchoolCache;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CoursePkgRepository coursePkgRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CourseSchoolRepository courseSchoolRepository;

    @Autowired
    private UserCourseCollectRepository collectRepository;

    @PostConstruct
    private void init() {
        courseCache = kvCacheFactory.create(new CacheOptions("course", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, Course>() {
                    @Override
                    public Course findByKey(Long key) throws Exception {
                        return courseRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Long, Course> findByKeys(Collection<Long> keys) throws Exception {
                        Map<Long, Course> map = new HashMap<>();
                        for (Long key : keys) {
                            map.put(key, findByKey(key));
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(Course.class));
        coursePkgCache = kvCacheFactory.create(new CacheOptions("course-pkg", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, CoursePkg>() {
                    @Override
                    public CoursePkg findByKey(Long key) throws Exception {
                        return coursePkgRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Long, CoursePkg> findByKeys(Collection<Long> keys) throws Exception {
                        Map<Long, CoursePkg> map = new HashMap<>();
                        for (Long key : keys) {
                            map.put(key, findByKey(key));
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(CoursePkg.class));
        chapterCache = kvCacheFactory.create(new CacheOptions("course-chapter", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, Chapter>() {
                    @Override
                    public Chapter findByKey(Long key) throws Exception {
                        return chapterRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Long, Chapter> findByKeys(Collection<Long> keys) throws Exception {
                        Map<Long, Chapter> map = new HashMap<>();
                        for (Long key : keys) {
                            map.put(key, findByKey(key));
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(Chapter.class));
        lessonCache = kvCacheFactory.create(new CacheOptions("course-lesson", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, Lesson>() {
                    @Override
                    public Lesson findByKey(Long key) throws Exception {
                        return lessonRepository.findById(key).orElse(null);
                    }

                    @Override
                    public Map<Long, Lesson> findByKeys(Collection<Long> keys) throws Exception {
                        Map<Long, Lesson> map = new HashMap<>();
                        for (Long key : keys) {
                            map.put(key, findByKey(key));
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(Lesson.class));
        courseSchoolCache = kvCacheFactory.create(new CacheOptions("course-school", 1, DateUtils.SECOND_PER_DAY),
                new RepositoryProvider<Long, CourseSchool>() {
                    @Override
                    public CourseSchool findByKey(Long key) throws Exception {
                        return courseSchoolRepository.findByCourseId(key);
                    }

                    @Override
                    public Map<Long, CourseSchool> findByKeys(Collection<Long> keys) throws Exception {
                        Map<Long, CourseSchool> map = new HashMap<>();
                        for (Long key : keys) {
                            map.put(key, findByKey(key));
                        }
                        return map;
                    }
                }, new BeanModelConverter<>(CourseSchool.class));
    }

    @Override
    public Course saveOfficialCourse(Course course) {
        if (course == null) {
            throw new DetailedServiceException("课程信息为空");
        }
        if (StringUtils.isEmpty(course.getName())) {
            throw new ArgumentServiceException("name");
        }
        if (StringUtils.isEmpty(course.getDescr())) {
            throw new ArgumentServiceException("descr");
        }
        if (StringUtils.isEmpty(course.getContent())) {
            throw new ArgumentServiceException("content");
        }
        if (course.getChapterNum() == 0) {
            throw new ArgumentServiceException("chapterNum");
        }
        if (course.getPriority() == 0) {
            throw new ArgumentServiceException("priority");
        }
        if (course.getSectionNum() == 0) {
            throw new ArgumentServiceException("sectionNum");
        }
        if (course.getClassHour() == 0) {
            throw new ArgumentServiceException("classHour");
        }

        if (course.getId() == null) {
            course.setCreatedAt(System.currentTimeMillis());
            course.setStatus(ByteUtils.BYTE_0);
            course.setType(CourseTypeEnum.Official.getVal());
        }

        Course sCourse = courseRepository.saveAndFlush(course);
        courseCache.remove(course.getId());

        return sCourse;
    }

    @Override
    public List<Course> courses(CourseQo qo) {
        return courseRepository.findAllForList(qo);
    }

    @Override
    public Map<String, Object> saveCourse(Course course, CourseSchool courseSchool) {
        Integer schoolId = SchAdminContexts.getSchoolId();
        if (course == null) {
            throw new DetailedServiceException("课程信息为空");
        }
        if (StringUtils.isEmpty(course.getName())) {
            throw new ArgumentServiceException("name");
        }
        if (StringUtils.isEmpty(course.getDescr())) {
            throw new ArgumentServiceException("descr");
        }
        if (StringUtils.isEmpty(course.getContent())) {
            throw new ArgumentServiceException("content");
        }
        if (course.getChapterNum() == 0) {
            throw new ArgumentServiceException("chapterNum");
        }
        if (course.getPriority() == 0) {
            throw new ArgumentServiceException("priority");
        }
        if (course.getSectionNum() == 0) {
            throw new ArgumentServiceException("sectionNum");
        }
        if (course.getClassHour() == 0) {
            throw new ArgumentServiceException("classHour");
        }

        if (course.getId() == null) {

            course.setCreatedAt(System.currentTimeMillis());
            course.setStatus(ByteUtils.BYTE_0);
            course.setType(CourseTypeEnum.Common.getVal());
        }
        Course sCourse = courseRepository.saveAndFlush(course);
        courseCache.remove(course.getId());

        courseSchool.setCourseId(sCourse.getId());
        CourseSchool sCourseSchool = courseSchoolRepository.saveAndFlush(courseSchool);
        courseSchoolCache.remove(sCourseSchool.getCourseId());

        Map<String, Object> map = new HashMap<>();
        map.put("course", sCourse);
        map.put("courseSchool", sCourseSchool);
        return map;
    }

    @Override
    public Map<Long, Course> coursesForMap(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }

        return courseCache.findByKeys(ids);
    }

    @Override
    public Page<Course> officialCourses(CourseQo courseQo, CourseWO courseWO) {
        courseQo.setType(CourseTypeEnum.Official.getVal());
        Page<Course> page = courseRepository.findAll(courseQo);

        if (courseWO.isWithCoursePkg()) {
            wrapCourseWithCoursePkg(page.getContent(), SessionType.ADMIN);
        }

        return page;
    }

    @Override
    public Page<Course> schOfficialCourses() {
        Integer schoolId = SchAdminContexts.getSchoolId();

        List<Long> courseIds = schoolService.authCourseIds(schoolId);
        if (CollectionUtils.isEmpty(courseIds)) {
            return null;
        }
        CourseQo cQo = new CourseQo();
        cQo.setIds(courseIds);

        Page<Course> page = courseRepository.findAll(cQo);
        List<Long> applyIds = courseSchoolRepository.findCourseIdsBySchoolId(schoolId);

        page.getContent().stream().forEach((cs) -> {
            if (applyIds.contains(cs.getId()))
                cs.setCanApply(ByteUtils.BYTE_1);
        });

        return page;
    }

    @Override
    public Page<CourseSchool> courses(CourseSchoolQo cQo, CourseWO courseWO, SessionType sessionType) {
        Page<CourseSchool> page = courseSchoolRepository.findAll(cQo);
        wrapCourse(page.getContent(), courseWO, sessionType);
        return page;
    }

    @Override
    public List<CourseSchool> coursesForList(CourseSchoolQo cQo, CourseWO courseWO, SessionType sessionType) {
        List<CourseSchool> cList = courseSchoolRepository.findAllForList(cQo);
        wrapCourse(cList, courseWO, sessionType);
        return cList;
    }

    @Override
    public CourseSchool structCourse(Long id, CourseWO wo, SessionType sessionType) {
        CourseSchool cs = courseSchoolCache.findByKey(id);
        List<CourseSchool> cList = Collections.singletonList(cs);
        wrapCourse(cList, wo, sessionType);
        return cList.get(0);
    }

    private void wrapCourse(List<CourseSchool> courseSchools, CourseWO courseWO, SessionType sessionType) {
        if (!CollectionUtils.isEmpty(courseSchools) && courseWO.isWithCourse()) {
            List<Long> coursesIds = courseSchools.stream().map((courseSchool) -> courseSchool.getCourseId())
                    .collect(Collectors.toList());
            Map<Long, Course> cMap = courseCache.findByKeys(coursesIds);

            if (courseWO.isWithCoursePkg()) {
                wrapCourseWithCoursePkg(cMap.values(), sessionType);
                if (courseWO.isList()) {
                    Map<Long, Integer> lpMap = new HashMap<>();

                    cMap.values().forEach((course) -> {
                        List<CoursePkg> pkgs = course.getCoursePkgs();

                        if (CollectionUtils.isNotEmpty(pkgs)) {
                            int lowPrice = Integer.MAX_VALUE;
                            for (CoursePkg pkg : pkgs) {
                                lowPrice = Math.min(lowPrice, pkg.getLowPrice());
                            }
                            lpMap.put(course.getId(), lowPrice);
                        }
                    });

                    courseSchools.stream().forEach(cs -> {
                        long cid = cs.getCourseId();
                        cs.setLowPrice(lpMap.get(cid));

                        Course course = cMap.get(cid);
                        if (CollectionUtils.isNotEmpty(course.getImgs())) {
                            cs.setCover(course.getImgs().get(0));
                        }
                    });

                    cMap.values().forEach((course) -> cMap.put(course.getId(), course.baseCourse()));
                }
                if (courseWO.isDetail()) {
                    ChapterQo chapterQo = new ChapterQo();
                    chapterQo.setCourseIds(coursesIds);

                    List<Chapter> chapters = chapters(chapterQo);

                    Map<Long, List<Chapter>> chapterMap = chapters.stream()
                            .collect(Collectors.groupingBy((Chapter::getCourseId)));
                    cMap.values().forEach((course) -> {
                        course.setChapters(chapterMap.get(course.getId()));
                    });
                    if (SessionType.USER.name() == sessionType.name()) {
                        List<UserCourseCollect> collects = collectRepository.findByUserId(UserContexts.getUserId());
                        Map<Long, UserCourseCollect> collectMap = collects.stream()
                                .collect(Collectors.toMap(UserCourseCollect::getCourseId, c -> c));
                        if (CollectionUtils.isNotEmpty(collects)) {
                            cMap.values().forEach((course) -> {
                                course.setChapters(chapterMap.get(course.getId()));
                                UserCourseCollect exist = collectMap.get(course.getId());
                                if (exist != null) {
                                    course.setIsCollected(true);
                                }
                            });
                        }
                    }
                }
                courseSchools.stream().forEach((cs) -> cs.setCourse(cMap.get(cs.getCourseId())));
            }

            if (courseWO.isWithTrainer()) {
                List<Long> tIds = courseSchools.stream().map(c -> c.getTrainerId()).collect(Collectors.toList());
                Map<Long, Trainer> tMap = trainerService.trainersForMap(tIds);

                if (courseWO.isList()) {
                    for (Long id : tMap.keySet()) {
                        Trainer newTrainer = new Trainer();
                        newTrainer.setName(tMap.get(id).getName());

                        tMap.put(id, newTrainer);
                    }
                }
                courseSchools.stream().forEach((cs) -> cs.setTrainer(tMap.get(cs.getTrainerId())));
            }

            if (courseWO.isWithCategory()) {
                Set<Long> cIds = courseSchools.stream().map(c -> c.getCategoryId()).collect(Collectors.toSet());
                Map<Long, Category> cgMap = categoryService.categorysForMap(cIds);

                if (courseWO.isList()) {
                    for (Long id : cgMap.keySet()) {
                        cgMap.put(id, cgMap.get(id).baseCategory());
                    }
                }
                courseSchools.stream().forEach((cs) -> cs.setCategory(cgMap.get(cs.getCategoryId())));
            }
        }

    }

    private void wrapCourseWithCoursePkg(Collection<Course> courses, SessionType sessionType) {
        List<Long> ids = courses.stream().map(course -> course.getId()).collect(Collectors.toList());

        Map<Long, List<CoursePkg>> cpMap = coursePkgRepository.findByCourseIdIn(ids).stream()
                .collect(Collectors.groupingBy(CoursePkg::getCourseId));

        if (cpMap.size() > 0) {
            if (sessionType != SessionType.ADMIN) {
                Integer schoolId = null;
                if (UserContexts.user() != null)
                    schoolId = UserContexts.user().getSchoolId();

                if (SchAdminContexts.adminSessionWrapper() != null)
                    schoolId = SchAdminContexts.getSchoolId();

                List<AuthCourse> authCourses = schoolService.school(schoolId).getAuthCourses();
                if (CollectionUtils.isNotEmpty(authCourses)) {
                    for (AuthCourse authCourse : authCourses) {
                        List<CoursePkg> cPkgs = cpMap.get(authCourse.getCourseId());
                        if (CollectionUtils.isNotEmpty(cPkgs)) {
                            cpMap.put(authCourse.getCourseId(),
                                    cPkgs.stream().filter((pkg) -> authCourse.getPkgIds().contains(pkg.getId()))
                                            .collect(Collectors.toList()));
                        }
                    }
                }
            }

            courses.stream().forEach(course -> course.setCoursePkgs(cpMap.get(course.getId())));
        }
    }

    @Override
    public List<CourseSchool> courseSchoolsByTrainerIds(List<Long> trainerIds, CourseWO wo, SessionType type) {
        List<CourseSchool> courseSchools = null;
        if (CollectionUtils.isNotEmpty(trainerIds)) {
            courseSchools = courseSchoolRepository.findByTrainerIdIn(trainerIds);
            wrapCourse(courseSchools, wo, type);
        }

        return courseSchools;
    }

    @Transactional
    @Override
    public void courseApply(Long courseId) {
        if (courseId == null) {
            throw new ArgumentServiceException("courseId");
        }
        Integer schoolId = SchAdminContexts.getSchoolId();

        Course course = course(courseId);
        if (course.getType() != CourseTypeEnum.Official.getVal()) {
            throw new DetailedServiceException("官方课程不存在");
        }

        List<Long> courseIds = schoolService.authCourseIds(schoolId);

        if (!courseIds.contains(courseId)) {
            throw new DetailedServiceException("该课程未被授权");
        }

        courseSchoolRepository.save(new CourseSchool(schoolId, courseId, course.getName(), course.getPriority(),
                course.getStatus(), course.getFree(), course.getType(), 0L, 0L));
    }

    @Override
    public Course course(Long id) {
        Course course = findById(id);
        if (course == null) {
            throw new DataNotFoundServiceException("course");
        }
        return course;
    }

    private Course findById(Long id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        return courseCache.findByKey(id);
    }

    @Override
    public CoursePkg coursePkg(Long id) {
        CoursePkg coursePkg = findPkgById(id);
        if (coursePkg == null) {
            throw new DataNotFoundServiceException("course-pkg");
        }
        return coursePkg;
    }

    private CoursePkg findPkgById(Long id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        return coursePkgCache.findByKey(id);
    }

    @Transactional
    @Override
    public void courseStatus(Long id, Byte status) {
        if (id == null) {
            throw new ArgumentServiceException("courseId");
        }
        if (status == null) {
            throw new ArgumentServiceException("status");
        }
        Course course = course(id);
        List<CoursePkg> exists = coursePkgRepository.findByCourseId(id);
        if (CollectionUtils.isEmpty(exists) && course.getFree() == ByteUtils.BYTE_0) {
            throw new DetailedServiceException("请设定套餐");
        }
        courseRepository.updateStatus(id, status);
        courseSchoolRepository.updateStatus(id, status);

        courseCache.remove(id);
        courseSchoolCache.remove(id);
    }

    @Override
    public Course structCourse(Long id) {
        Course course = course(id);

        ChapterQo chapterQo = new ChapterQo();
        chapterQo.setCourseId(course.getId());

        List<Chapter> chapters = chapters(chapterQo);
        chapters.stream().forEach((chapter) -> {
            LessonQo lessonQo = new LessonQo();
            lessonQo.setChapterId(chapter.getId());
            chapter.setLessons(lessons(lessonQo));
        });

        course.setChapters(chapters);
        return course;
    }

    @Transactional
    @Override
    public void saveCoursePkg(CoursePkg coursePkg) {
        if (coursePkg == null) {
            throw new DetailedServiceException("课程套餐信息为空");
        }
        if (StringUtils.isEmpty(coursePkg.getName())) {
            throw new ArgumentServiceException("name");
        }
        if (StringUtils.isEmpty(coursePkg.getDuration())) {
            throw new ArgumentServiceException("duration");
        }
        int price = coursePkg.getPrice();
        if (price == 0) {
            throw new ArgumentServiceException("price");
        }

        if (coursePkg.getId() == null) {
            coursePkg.setCreatedAt(System.currentTimeMillis());
            coursePkg.setStatus(ByteUtils.BYTE_0);
            coursePkg.setCourseType(ByteUtils.BYTE_1);
            coursePkgRepository.save(coursePkg);
        } else {
            CoursePkg exist = coursePkg(coursePkg.getId());
            coursePkg.setCreatedAt(exist.getCreatedAt());
            coursePkg.setStatus(exist.getStatus());
            coursePkg.setCourseType(exist.getCourseType());
            coursePkg.setCourseId(exist.getCourseId());
            coursePkgRepository.save(coursePkg);
            coursePkgCache.remove(coursePkg.getId());
        }

    }

    @Override
    public List<CoursePkg> coursePkgs(Long courseId) {
        if (courseId == null) {
            throw new ArgumentServiceException("courseId");
        }
        return coursePkgRepository.findByCourseId(courseId);
    }

    @Override
    public Map<Long, CoursePkg> coursePkgsForMap(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        return coursePkgCache.findByKeys(ids);
    }

    @Transactional
    @Override
    public void coursePkgStatus(Long id, Byte status) {
        coursePkgRepository.updateStatus(id, status);
        coursePkgCache.remove(id);
    }

    @Override
    public void removeCourse(Long id) {
        course(id);
        courseRepository.deleteById(id);
        courseSchoolRepository.deleteByCourseId(id);

        courseCache.remove(id);
        courseSchoolCache.remove(id);

    }

    @Override
    public void removePkg(Long id) {
        coursePkg(id);
        coursePkgRepository.deleteById(id);
        coursePkgCache.remove(id);
    }

    @Transactional
    @Override
    public void batchChapterStatus(BatchStatusDTO dto) {
        List<Long> ids = dto.getIds();
        if (!CollectionUtils.isEmpty(ids)) {
            chapterRepository.updateStatusIn(ids, dto.getStatus());
            ids.stream().forEach((id) -> chapterCache.remove(id));

            List<Long> lessonIds = lessonRepository.findIdsByChapterId(ids);
            dto.setIds(lessonIds);
            batchLessonStatus(dto);
        }
    }

    @Override
    public List<Chapter> chapters(ChapterQo chapterQo) {
        return chapterRepository.findAllForList(chapterQo);
    }

    @Override
    public void createChapter(Chapter chapter) {
        if (chapter.getClassHour() == null) {
            throw new ArgumentServiceException("classHour");
        }
        if (chapter.getLessonNum() == null) {
            throw new ArgumentServiceException("lessonsNum");
        }
        if (chapter.getPriority() == null) {
            throw new ArgumentServiceException("priority");
        }
        if (chapter.getCourseId() == null) {
            throw new ArgumentServiceException("courseId");
        }

        if (chapter.getId() == null) {
            chapter.setStatus(ByteUtils.BYTE_0);
            chapter.setCreatedAt(System.currentTimeMillis());
        }

        chapterRepository.save(chapter);
    }

    @Transactional
    @Override
    public void createLesson(Lesson lesson) {
        if (lesson.getClassHour() == null) {
            throw new ArgumentServiceException("classHour");
        }
        if (lesson.getPriority() == null) {
            throw new ArgumentServiceException("priority");
        }
        if (lesson.getCourseId() == null) {
            throw new ArgumentServiceException("courseId");
        }
        if (lesson.getChapterId() == null) {
            throw new ArgumentServiceException("chapterId");
        }
        if (lesson.getMedia() == null) {
            throw new ArgumentServiceException("media");
        }
        if (lesson.getId() == null) {
            lesson.setStatus(ByteUtils.BYTE_0);
            lesson.setCreatedAt(System.currentTimeMillis());
        } else {
            lessonCache.remove(lesson.getId());
        }

        lessonRepository.save(lesson);
    }

    @Override
    public void batchLessonStatus(BatchStatusDTO dto) {
        List<Long> ids = dto.getIds();
        if (!CollectionUtils.isEmpty(ids)) {
            lessonRepository.updateStatusIn(dto.getIds(), dto.getStatus());
            dto.getIds().stream().forEach((id) -> lessonCache.remove(id));
        }
    }

    @Override
    public List<Lesson> lessons(LessonQo lessonQo) {
        return lessonRepository.findAllForList(lessonQo).stream().map(lesson -> lesson.baseLesson())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void removeChapter(Long id) {
        chapter(id);
        chapterRepository.deleteById(id);
        chapterCache.remove(id);
    }

    @Override
    public void removeLesson(Long id) {
        lesson(id);
        lessonRepository.deleteById(id);
        lessonCache.remove(id);
    }

    @Override
    public CourseSchool courseSchool(Long courseId) {
        Integer schoolId = SchAdminContexts.getSchoolId();

        return courseSchoolRepository.findBySchoolIdAndCourseId(schoolId, courseId);
    }

    @Override
    public void saveCourseSchool(CourseSchool courseSchool) {
        if (courseSchool.getCategoryId() == 0) {
            throw new ArgumentServiceException("categoryId");
        }
        if (courseSchool.getTrainerId() == 0) {
            throw new ArgumentServiceException("trainerId");
        }
        courseSchoolRepository.save(courseSchool);
        courseSchoolCache.remove(courseSchool.getCourseId());
    }

    @Override
    public Chapter chapter(Long id) {
        Chapter chapter = findChapterById(id);
        if (chapter == null) {
            throw new ArgumentServiceException("chapter");
        }
        chapter.setCourse(course(chapter.getCourseId()));
        return chapter;
    }

    private Chapter findChapterById(Long id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        return chapterCache.findByKey(id);
    }

    @Override
    public Lesson lesson(Long id) {
        Lesson lesson = findLessonById(id);
        if (lesson == null) {
            throw new ArgumentServiceException("lesson");
        }
        return lesson;
    }

    @Override
    public String lessonMediaUrl(Long id) {
        return lesson(id).getMedia().getVideo().getUrl();
    }

    private Lesson findLessonById(Long id) {
        if (id == null) {
            throw new ArgumentServiceException("id");
        }
        return lessonCache.findByKey(id);
    }

    @Transactional
    @Override
    public void authCourse(List<AuthCourse> authCourses, Integer schoolId) {
        schoolService.updateAuthCourses(authCourses, schoolId);
    }

    @Override
    public void collect(Long courseId) {
        if (courseId == null) {
            throw new ArgumentServiceException("courseId");
        }
        UserCourseCollect exist = collectRepository.findByUserIdAndCourseId(UserContexts.getUserId(), courseId);
        if (exist != null) {
            collectRepository.deleteByUserIdAndCourseId(UserContexts.getUserId(), courseId);
        } else {
            collectRepository.save(new UserCourseCollect(UserContexts.getUserId(), courseId));
        }
    }

    @Override
    public List<CourseSchool> collectCourses() {
        CourseSchoolQo cQo = new CourseSchoolQo();
        cQo.setSchoolId(UserContexts.getUser().getSchoolId());
        Page<CourseSchool> page = courses(cQo, CourseWO.getAllInstance().setList(true), SessionType.USER);

        List<UserCourseCollect> collects = collectRepository.findByUserId(UserContexts.getUserId());
        Map<Long, UserCourseCollect> collectMap = collects.stream()
                .collect(Collectors.toMap(UserCourseCollect::getCourseId, c -> c));
        List<CourseSchool> csList = page.getContent().stream().filter(cs -> collectMap.containsKey(cs.getCourseId()))
                .collect(Collectors.toList());
        return csList;
    }

    @Transactional
    @Override
    public void updateCollectNum(Long courseId, Integer num) {
        if (courseId == null) {
            throw new ArgumentServiceException("courseId");
        }

        if (num == null) {
            throw new ArgumentServiceException("num");
        }

        courseSchoolRepository.updateStarNum(courseId, num);
        courseSchoolCache.remove(courseId);
    }
}