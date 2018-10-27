package com.springmvc.service.impl;

import com.springmvc.common.util.Pager;
import com.springmvc.common.util.Pages;
import com.springmvc.entity.Student;
import com.springmvc.mapper.StudentMapper;
import com.springmvc.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyuguang on 2018/10/17.
 */
@Service
public class StudentServiceImpl implements StudentService {

     @Autowired
     private StudentMapper studentMapper;


    @Override
    public int insertStudent(Student student) {
        return studentMapper.insert(student);
    }

    @Override
    public List<Student> selectStudent(Pages pager) {
        return studentMapper.selectStudentByPage(pager);
    }
}
