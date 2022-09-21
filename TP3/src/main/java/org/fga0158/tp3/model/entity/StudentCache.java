package org.fga0158.tp3.model.entity;

import java.util.ArrayList;
import java.util.List;

public class StudentCache {

    protected final List<Student> students = new ArrayList<>();

    public void registerStudent(Student student){
        this.students.add(student);
    }

    public Student getStudent(String fullName){
        for (Student student : students){
            if(student.getFullname().equals(fullName)){
                return student;
            }
        }
        return null;
    }

}