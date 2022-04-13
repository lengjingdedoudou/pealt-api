package com.peas.xinrui.api.course.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.peas.xinrui.api.course.entity.AuthCourse;
import com.peas.xinrui.api.course.entity.BatchStatusDTO;
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
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.data.domain.Page;

public interface CourseService {
    /* offical course */
    Course course(Long id);

    List<Course> courses(CourseQo qo);

    Course structCourse(Long id);

    CourseSchool structCourse(Long id, CourseWO wo, SessionType sessionType);

    Map<Long, Course> coursesForMap(Set<Long> ids);

    Page<Course> officialCourses(CourseQo courseQo, CourseWO courseWO);

    /* common course */
    CourseSchool courseSchool(Long courseId);

    void saveCourseSchool(CourseSchool courseSchool);

    Page<Course> schOfficialCourses();

    Page<CourseSchool> courses(CourseSchoolQo cQo, CourseWO courseWO, SessionType sessionType);

    List<CourseSchool> coursesForList(CourseSchoolQo cQo, CourseWO courseWO, SessionType sessionType);

    List<CourseSchool> courseSchoolsByTrainerIds(List<Long> trainerId, CourseWO wo, SessionType type);

    void courseApply(Long courseId);

    Course saveOfficialCourse(Course course);

    Map<String, Object> saveCourse(Course course, CourseSchool courseSchool);

    void courseStatus(Long id, Byte status);

    void removeCourse(Long id);

    void saveCoursePkg(CoursePkg coursePkg);

    CoursePkg coursePkg(Long id);

    List<CoursePkg> coursePkgs(Long courseId);

    Map<Long, CoursePkg> coursePkgsForMap(Set<Long> ids);

    void coursePkgStatus(Long id, Byte status);

    void removePkg(Long id);

    void createChapter(Chapter chapter);

    void removeChapter(Long id);

    void batchChapterStatus(BatchStatusDTO dto);

    List<Chapter> chapters(ChapterQo chapterQo);

    Chapter chapter(Long id);

    Lesson lesson(Long id);

    String lessonMediaUrl(Long id);

    void createLesson(Lesson lesson);

    void batchLessonStatus(BatchStatusDTO dto);

    void removeLesson(Long id);

    List<Lesson> lessons(LessonQo courseQo);

    void authCourse(List<AuthCourse> authCourses, Integer schoolId);

    void collect(Long courseId);

    List<CourseSchool> collectCourses();

    void updateCollectNum(Long courseId, Integer num);
}