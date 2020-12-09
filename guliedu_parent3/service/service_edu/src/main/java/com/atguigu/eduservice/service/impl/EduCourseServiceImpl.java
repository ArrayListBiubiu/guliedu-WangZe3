package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.servicebase.handler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-13
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    EduCourseDescriptionService descriptionService;



    /*
        从前端提交过来的信息“courseInfoForm”中包含2个表的信息，一个edu_course，一个edu_course_description
        需要将信息拆分，并分别添加进这2张表中
     */
    public String addCourseInfo(CourseInfoForm courseInfoForm) {
        //1创建“edu_course”对应的类对象：eduCourse
        EduCourse eduCourse = new EduCourse();
        //2从“CourseInfoForm”抽取所需属性值，并封装进“eduCourse”对象中
        BeanUtils.copyProperties(courseInfoForm, eduCourse);
        //3.插入数据
        int insert = baseMapper.insert(eduCourse);

        if (insert == 0) { throw new GuliException(20001, "添加课程失败"); }  //根据不同的业务需求，只要课程信息成功就可以了

        //1创建“edu_course_description”对应的类对象：eduCourseDescription
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        //2.1从“CourseInfoForm”抽取所需属性值，并封装进“eduCourse”对象中
        BeanUtils.copyProperties(courseInfoForm, eduCourseDescription);
        //2.2获取添加之后的课程id
        String courseId = eduCourse.getId();
        //2.3修改课程描述中的id值为课程id值，使课程表和课程描述表的id值一一对应
        eduCourseDescription.setId(courseId);
        /*
            由于此类是封装的”EduCourse“类相关信息，不能直接调用”baseMapper“对象
            需要注入“EduCourseDescriptionService”，然后调用相关方法
         */
        //3.插入数据
        descriptionService.save(eduCourseDescription);
        return courseId;
    }

    /*
        根据课程id，查询课程基本信息
            注：从前端提交的数据是包含2张表的信息，需要做抽取和分离操作
     */
    @Override
    public CourseInfoForm getCourseInfo(String courseId) {
        //从提交的数据中抽取与“课程表”相关信息，并写入“edu_course”表中
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(eduCourse,courseInfoForm);

        //从提交的数据中抽取与“课程描述表”相关信息，并写入“edu_course_description”表中
        EduCourseDescription eduCourseDescription = descriptionService.getById(courseId);
        courseInfoForm.setDescription(eduCourseDescription.getDescription());

        return courseInfoForm;
    }

    /*
        修改课程信息
            注：从前端提交的数据是包含2张表的信息，需要做抽取和分离操作
     */
    @Override
    public void updateCourse(CourseInfoForm courseInfoForm) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0) {
            throw new GuliException(20001,"修改课程信息失败");
        }

        EduCourseDescription description = new EduCourseDescription();
        description.setDescription(courseInfoForm.getDescription());
        description.setId(courseInfoForm.getId());
        descriptionService.updateById(description);
    }
}
