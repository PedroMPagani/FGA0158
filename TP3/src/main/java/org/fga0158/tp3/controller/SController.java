package org.fga0158.tp3.controller;

import org.fga0158.tp3.model.entity.Student;
import org.fga0158.tp3.view.StudentForm;

public interface SController {

    Student registerStudent(StudentForm studentForm);
    Student getStudent();

}