package org.fga0158.tp3.model.entity;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.fga0158.tp3.model.dao.GenericStudentDao;
import org.jetbrains.annotations.NotNull;

@Setter @Getter// setters and getters.
public class Student extends GenericStudentDao<Student> {

    private static final Gson gson = new Gson();

    private final @NotNull String fullname;
    private final @NotNull String course;
    private final @NotNull String studentID;
    private final @NotNull String email;

    public Student(@NotNull String fullName, @NotNull String course, @NotNull String studentID, @NotNull String email){
        super();
        this.fullname = fullName;
        this.course = course;
        this.studentID = studentID;
        this.email = email;
    }

    @Override
    public String serialize(Student user){
        return gson.toJson(user);
    }

}