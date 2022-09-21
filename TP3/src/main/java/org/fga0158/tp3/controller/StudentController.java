package org.fga0158.tp3.controller;

import org.fga0158.tp3.model.entity.Student;
import org.fga0158.tp3.model.service.StudentService;
import org.fga0158.tp3.view.StudentForm;

import java.util.Scanner;

public class StudentController implements SController {

    @Override // StudentFOrm would use jframe but lets use scanner..
    public Student registerStudent(StudentForm studentForm){
        Scanner scanner = new Scanner(System.in);
        String fullname = scanner.nextLine();
        String email = scanner.nextLine();
        String course = scanner.nextLine();
        String studentID = scanner.nextLine();
        StudentService service = new StudentService();
        return service.register(fullname, email, studentID, course);
    }

    @Override
    public Student getStudent(){
        Scanner scanner = new Scanner(System.in);
        String fullname = scanner.nextLine();
        StudentService studentService = new StudentService();
        return studentService.getStudent(fullname);
    }

}