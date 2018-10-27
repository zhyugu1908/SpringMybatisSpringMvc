package com.springmvc.controller;

import com.springmvc.common.util.Pages;
import com.springmvc.entity.Student;
import com.springmvc.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zhangyuguang on 2018/10/17.
 */
@Controller
@RequestMapping("/student")  // /student/studentPage
public class StudentController {
    @Autowired
    private StudentService studentService;


    @RequestMapping("/studentPage")
    public String studentPage(ModelMap modelMap){
        Pages pages = new Pages(1,2);
        List<Student> students = studentService.selectStudent(pages);
        pages.setEntityList(students);
        System.out.println(pages);
        modelMap.put("key","zhang_yu_guang");
        modelMap.put("put","yu_guang");
        modelMap.put("name","陈明川欢迎你");

        return "index"  ;
    }
}
