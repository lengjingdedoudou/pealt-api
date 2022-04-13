package com.peas.xinrui.api.course.controller;

import com.alibaba.fastjson.JSONArray;
import com.peas.xinrui.api.course.entity.CourseWO;
import com.peas.xinrui.api.course.qo.CourseQo;
import com.peas.xinrui.api.course.qo.CourseSchoolQo;
import com.peas.xinrui.api.course.qo.LessonQo;
import com.peas.xinrui.api.course.service.CourseService;
import com.peas.xinrui.common.controller.BaseController;
import com.peas.xinrui.common.controller.auth.RequiredSession;
import com.peas.xinrui.common.controller.auth.SessionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/usr/course")
@RequiredSession(SessionType.USER)
public class UsrCourseController extends BaseController {

    @Autowired
    private CourseService courseService;

    @RequestMapping("/courses")
    public ModelAndView courses(String courseQoJson) {
        return feedBack(courseService.courses(parseModel(courseQoJson, new CourseSchoolQo()), CourseWO.getNonInstance(),
                SessionType.USER));
    }

    @RequestMapping("/base_courses")
    public ModelAndView baseCourses(String courseQoJson) {
        return feedBack(courseService.courses(parseModel(courseQoJson, new CourseQo())));
    }

    @RequestMapping("/item")
    public ModelAndView item(Long id) {
        return feedBack(courseService.structCourse(id));
    }

    @RequestMapping("/struct_courses")
    public ModelAndView structCourses(String courseQoJson) {
        return feedBack(courseService.courses(parseModel(courseQoJson, new CourseSchoolQo()),
                CourseWO.getAllInstance().setList(true).setWithChapter(false), SessionType.USER));
    }

    @RequestMapping("/struct_course")
    public ModelAndView structCourse(Long id) {
        return feedBack(courseService.structCourse(id, CourseWO.getAllInstance().setDetail(true), SessionType.USER));
    }

    @RequestMapping("/lessons")
    public ModelAndView lessons(String ids) {
        return feedBack(courseService.lessons(new LessonQo(JSONArray.parseArray(ids, Long.class))));
    }

    @RequestMapping("/media_url")
    public ModelAndView mediaUrl(Long id) {
        return feedBack(courseService.lessonMediaUrl(id));
    }

    @RequestMapping("/collect")
    public ModelAndView collect(Long courseId) {
        courseService.collect(courseId);
        return feedBack(null);
    }

    @RequestMapping("/collect_courses")
    public ModelAndView collectCourses() {
        return feedBack(courseService.collectCourses());
    }
}