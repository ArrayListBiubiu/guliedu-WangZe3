package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2019-12-27
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    /*
        添加课程信息：
            注：前端在点击保存时，会将课程信息以json形式传递过来
                所以直接用“@RequestBody CourseInfoForm courseInfoForm”接收对应的json信息
        传递过来的信息是需要往2张表中添加
            表：edu_course、edu_course_description
     */
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoForm courseInfoForm) {
        String courseId = courseService.addCourseInfo(courseInfoForm);
        return R.ok().data("courseId", courseId);
    }

    /*
        根据课程id查询课程信息
     */
    @GetMapping("{courseId}")
    public R getCourseInfoId(@PathVariable String courseId) {
        CourseInfoForm courseInfoForm = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoForm",courseInfoForm);
    }


    /*
        修改课程信息
     */
    @PostMapping("updateCourse")
    public R updateCourse(@RequestBody CourseInfoForm courseInfoForm) {
        courseService.updateCourse(courseInfoForm);
        return R.ok();
    }

}

