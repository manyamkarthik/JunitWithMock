package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentDAO studentDAO;

    public void createStudent(String firstName,String lastName,String emailAddress){
        CollegeStudent collegeStudent=new CollegeStudent(firstName,lastName,emailAddress);
        studentDAO.save(collegeStudent);

    }
    public boolean checkIfStudentIsNull(int id){
        boolean b=false;
        Optional<CollegeStudent> collegeStudent=studentDAO.findById(id);
        b= collegeStudent.isPresent() ? b = true : b;
        return b;
    }

    public boolean deleteStudentById(int id){
        boolean b=false;
        Optional<CollegeStudent> collegeStudent=studentDAO.findById(id);
        b= collegeStudent.isPresent() ? b = true : b;
        if(b){
            studentDAO.deleteById(id);
        }
        return b;
    }

    public List<CollegeStudent> getAllStudents(){
        return (List<CollegeStudent>) studentDAO.findAll();
    }

}
