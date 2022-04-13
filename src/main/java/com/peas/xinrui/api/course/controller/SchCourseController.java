package com.peas.xinrui.api.course.controller;

import com.peas.xinrui.api.course.entity.BatchStatusDTO;
import com.peas.xinrui.api.course.entity.CourseWO;
import com.peas.xinrui.api.course.model.Chapter;
import com.peas.xinrui.api.course.model.Course;
import com.peas.xinrui.api.course.model.CoursePkg;
import com.peas.xinrui.api.course.model.CourseSchool;
import com.peas.xinrui.api.course.model.Lesson;
import com.peas.xinrui.api.course.qo.ChapterQo;
import com.peas.xinrui.api.course.qo.CourseSchoolQo;
import com.peas.xinrui.api.course.qo.LessonQo;
import com.peas.xinrui.api.course.service.CourseService;
import com.peas.xinrui.api.schadmin.entity.SchAdminPermission;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSchoolPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/sch/course")
@RequiredSession(SessionType.SCHADMIN)
public class SchCourseController extends BaseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping("/official_courses")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView officialCourses(String courseQoJson) {
        return feedBack(courseService.schOfficialCourses());
    }

    @RequestMapping("/course_apply")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView courseApply(Long courseId) {
        courseService.courseApply(courseId);
        return feedBack(null);
    }

    @RequestMapping("/save_course")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView saveCourse(String courseJson, String courseSchoolJson) {
        return feedBack(courseService.saveCourse(parseModel(courseJson, new Course()),
                parseModel(courseSchoolJson, new CourseSchool())));
    }

    @RequestMapping("/save_official_course")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView saveOfficialCourse(String courseSchoolJson) {
        courseService.saveCourseSchool(parseModel(courseSchoolJson, new CourseSchool()));
        return feedBack(null);
    }

    @RequestMapping("/save_course_pkg")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView saveCoursePkg(String coursePkgJson) {
        courseService.saveCoursePkg(parseModel(coursePkgJson, new CoursePkg()));
        return feedBack(null);
    }

    @RequestMapping("/item")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView course(Long id) {
        return feedBack(courseService.course(id));
    }

    @RequestMapping("/courses")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView courses(String courseQoJson) {
        return feedBack(courseService.courses(parseModel(courseQoJson, new CourseSchoolQo()),
                CourseWO.getAllInstance().setList(true), SessionType.SCHADMIN));
    }

    @RequestMapping("/struct_courses")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView structCourses(String courseQoJson) {
        return feedBack(courseService.courses(parseModel(courseQoJson, new CourseSchoolQo()),
                CourseWO.getAllInstance().setList(false), SessionType.SCHADMIN));
    }

    @RequestMapping("/course_school")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView courseSchool(Long courseId) {
        return feedBack(
                courseService.structCourse(courseId, CourseWO.getCoursePkgListInstance(), SessionType.SCHADMIN));
    }

    @RequestMapping("/course_pkgs")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView coursePkgs(Long courseId) {
        return feedBack(courseService.coursePkgs(courseId));
    }

    @RequestMapping("/course_status")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView courseStatus(Long id, Byte status) {
        courseService.courseStatus(id, status);
        return feedBack(null);
    }

    @RequestMapping("/course_pkg_status")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView schoolStatus(Long id, Byte status) {
        courseService.coursePkgStatus(id, status);
        return feedBack(null);
    }

    @RequestMapping("/course_pkg_remove")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView removeCoursePkg(Long id) {
        courseService.removePkg(id);
        return feedBack(null);
    }

    @RequestMapping("/course_remove")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView removeCourse(Long id) {
        courseService.removeCourse(id);
        return feedBack(null);
    }

    @RequestMapping("/struct_course")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView structCourse(Long id) {
        return feedBack(courseService.structCourse(id));
    }

    @RequestMapping("/chapter_save")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView saveChapter(String chapterJson) {
        courseService.createChapter(parseModel(chapterJson, new Chapter()));
        return feedBack(null);
    }

    @RequestMapping("/lesson_save")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView saveLesson(String lessonJson) {
        courseService.createLesson(parseModel(lessonJson, new Lesson()));
        return feedBack(null);
    }

    @RequestMapping("/chapters")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView chapters(String chapterQoJson) {
        return feedBack(courseService.chapters(parseModel(chapterQoJson, new ChapterQo())));
    }

    @RequestMapping("/lessons")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView lessons(String lessonQoJson) {
        return feedBack(courseService.lessons(parseModel(lessonQoJson, new LessonQo())));
    }

    @RequestMapping("/chapter_status")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView chapterStatus(String batchStatusDtoJson) {
        courseService.batchChapterStatus(parseModel(batchStatusDtoJson, new BatchStatusDTO()));
        return feedBack(null);
    }

    @RequestMapping("/lesson_status")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView lessonStatus(String batchStatusDtoJson) {
        courseService.batchLessonStatus(parseModel(batchStatusDtoJson, new BatchStatusDTO()));
        return feedBack(null);
    }

    @RequestMapping("/chapter_remove")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView chapterRemove(Long id) {
        courseService.removeChapter(id);
        return feedBack();
    }

    @RequestMapping("/lesson_remove")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_EDIT })
    public ModelAndView lessonRemove(Long id) {
        courseService.removeLesson(id);
        return feedBack();
    }

    @RequestMapping("/chapter")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView chapter(Long id) {
        return feedBack(courseService.chapter(id));
    }

    @RequestMapping("/lesson")
    @RequiredSchoolPermission({ SchAdminPermission.COURSE_LIST })
    public ModelAndView lesson(Long id) {
        return feedBack(courseService.lesson(id));
    }
}