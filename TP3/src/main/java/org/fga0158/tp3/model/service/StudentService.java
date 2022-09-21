package org.fga0158.tp3.model.service;

import org.fga0158.tp3.model.entity.Student;
import org.fga0158.tp3.model.entity.StudentCache;

public class StudentService implements SService {

    private static StudentCache studentCache = new StudentCache();

    @Override
    public Student register(String fullname, String email, String studentID, String course){
        Student student = new Student(fullname, course, studentID, email);
        studentCache.registerStudent(student);
        return student;
    }

    @Override
    public Student getStudent(String fullname){
        return studentCache.getStudent(fullname);
    }

}