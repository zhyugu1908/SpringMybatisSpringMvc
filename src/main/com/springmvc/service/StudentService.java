package com.springmvc.service;

import com.springmvc.common.util.Pager;
import com.springmvc.common.util.Pages;
import com.springmvc.entity.Student;

import java.util.List;

/**
 * Created by zhangyuguang on 2018/10/17.
 */
public interface StudentService {

   int  insertStudent(Student student);
   List<Student> selectStudent(Pages pager);
}
