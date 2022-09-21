package org.fga0158.tp3.model.service;

import org.fga0158.tp3.model.entity.Student;

public interface SService {

    Student register(String fullname, String email, String studentID, String course);

    Student getStudent(String fullname);

}