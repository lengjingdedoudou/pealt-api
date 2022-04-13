package com.peas.xinrui.api.course.controller;

import com.peas.xinrui.api.admin.entity.AdminPermission;
import com.peas.xinrui.api.course.entity.AuthCourse;
import com.peas.xinrui.api.course.entity.BatchStatusDTO;
import com.peas.xinrui.api.course.entity.CourseWO;
import com.peas.xinrui.api.course.model.Chapter;
import com.peas.xinrui.api.course.model.Course;
import com.peas.xinrui.api.course.model.CoursePkg;
import com.peas.xinrui.api.course.model.Lesson;
import com.peas.xinrui.api.course.qo.ChapterQo;
import com.peas.xinrui.api.course.qo.CourseQo;
import com.peas.xinrui.api.course.qo.LessonQo;
import com.peas.xinrui.api.course.service.CourseService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredPermission;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/adm/course")
@RequiredSession(SessionType.ADMIN)
public class AdmCourseController extends BaseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping("/save_course")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView saveCourse(String courseJson) {
        return feedBack(courseService.saveOfficialCourse(parseModel(courseJson, new Course())));
    }

    @RequestMapping("/save_course_pkg")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView saveCoursePkg(String coursePkgJson) {
        courseService.saveCoursePkg(parseModel(coursePkgJson, new CoursePkg()));
        return feedBack(null);
    }

    @RequestMapping("/item")
    @RequiredPermission({ AdminPermission.COURSE_LIST })
    public ModelAndView course(Long id) {
        return feedBack(courseService.course(id));
    }

    @RequestMapping("/official_courses")
    @RequiredPermission({ AdminPermission.COURSE_LIST })
    public ModelAndView courses(String courseQoJson) {
        CourseWO courseWO = CourseWO.getCoursePkgListInstance();
        return feedBack(courseService.officialCourses(parseModel(courseQoJson, new CourseQo()), courseWO));
    }

    @RequestMapping("/auth_course")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView authCourse(String authCourses, Integer schoolId) {
        courseService.authCourse(parseList(authCourses, AuthCourse.class), schoolId);
        return feedBack(null);
    }

    @RequestMapping("/course_pkgs")
    @RequiredPermission({ AdminPermission.COURSE_LIST })
    public ModelAndView coursePkgs(Long courseId) {
        return feedBack(courseService.coursePkgs(courseId));
    }

    @RequestMapping("/course_status")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView courseStatus(Long id, Byte status) {
        courseService.courseStatus(id, status);
        return feedBack(null);
    }

    @RequestMapping("/course_pkg_status")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView schoolStatus(Long id, Byte status) {
        courseService.coursePkgStatus(id, status);
        return feedBack(null);
    }

    @RequestMapping("/course_pkg_remove")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView removeCoursePkg(Long id) {
        courseService.removePkg(id);
        return feedBack(null);
    }

    @RequestMapping("/course_remove")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView removeCourse(Long id) {
        courseService.removeCourse(id);
        return feedBack(null);
    }

    @RequestMapping("/struct_course")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView structCourse(Long id) {
        return feedBack(courseService.structCourse(id));
    }

    @RequestMapping("/chapter_save")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView saveChapter(String chapterJson) {
        courseService.createChapter(parseModel(chapterJson, new Chapter()));
        return feedBack(null);
    }

    @RequestMapping("/lesson_save")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView saveLesson(String lessonJson) {
        courseService.createLesson(parseModel(lessonJson, new Lesson()));
        return feedBack(null);
    }

    @RequestMapping("/chapters")
    @RequiredPermission({ AdminPermission.COURSE_LIST })
    public ModelAndView chapters(String chapterQoJson) {
        return feedBack(courseService.chapters(parseModel(chapterQoJson, new ChapterQo())));
    }

    @RequestMapping("/lessons")
    @RequiredPermission({ AdminPermission.COURSE_LIST })
    public ModelAndView lessons(String lessonQoJson) {
        return feedBack(courseService.lessons(parseModel(lessonQoJson, new LessonQo())));
    }

    @RequestMapping("/chapter_status")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView chapterStatus(String batchStatusDtoJson) {
        courseService.batchChapterStatus(parseModel(batchStatusDtoJson, new BatchStatusDTO()));
        return feedBack(null);
    }

    @RequestMapping("/lesson_status")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView lessonStatus(String batchStatusDtoJson) {
        courseService.batchLessonStatus(parseModel(batchStatusDtoJson, new BatchStatusDTO()));
        return feedBack(null);
    }

    @RequestMapping("/chapter_remove")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView chapterRemove(Long id) {
        courseService.removeChapter(id);
        return feedBack();
    }

    @RequestMapping("/lesson_remove")
    @RequiredPermission({ AdminPermission.COURSE_EDIT })
    public ModelAndView lessonRemove(Long id) {
        courseService.removeLesson(id);
        return feedBack();
    }

    @RequestMapping("/chapter")
    @RequiredPermission({ AdminPermission.COURSE_LIST })
    public ModelAndView chapter(Long id) {
        return feedBack(courseService.chapter(id));
    }

    @RequestMapping("/lesson")
    @RequiredPermission({ AdminPermission.COURSE_LIST })
    public ModelAndView lesson(Long id) {
        return feedBack(courseService.lesson(id));
    }
}