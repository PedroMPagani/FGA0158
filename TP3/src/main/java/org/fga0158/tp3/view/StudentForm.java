package org.fga0158.tp3.view;

import org.fga0158.tp3.controller.StudentController;
import org.fga0158.tp3.model.entity.Student;

import java.util.Scanner;

public class StudentForm {

    private void startProcess(){
        StudentController controller = new StudentController();
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        if(option == 1){
            Student student = controller.registerStudent(this);
            System.out.println(student);
        }
        if(option == 2){
            Student student = controller.getStudent();
            System.out.println(student);
        }
    }

    public static void main(String[] args){
        Thread thread = new Thread(()->{
            StudentForm studentForm = new StudentForm();
            studentForm.startProcess();
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}