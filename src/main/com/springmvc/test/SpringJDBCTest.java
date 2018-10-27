package com.springmvc.test;

import com.springmvc.common.util.Pager;
import com.springmvc.common.util.Pages;
import com.springmvc.entity.Student;
import com.springmvc.service.StudentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

/**
 * Created by zhangyuguang on 2018/10/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-config.xml")
public class SpringJDBCTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private StudentService studentService;

    Connection conn = null;

    @Test
    public void JDBCTest(){

       try {
           conn =  dataSource.getConnection();
           System.out.println("数据库已经正确连接："+conn);
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    @Test
    public void inserInto(){
        Student student = new Student();
        student.setAge(12);
        student.setClassid(2);
        student.setName("李三");
        student.setPhone("110");
        student.setSex(0);
        Integer insert = studentService.insertStudent(student);
        System.out.println(insert);
    }

    @Test
    public void testSelectPager(){
        Pages page = new Pages(1,1);
        List<Student> list = studentService.selectStudent(page);
        page.setEntityList(list);
        System.out.println(page);
    }


}
