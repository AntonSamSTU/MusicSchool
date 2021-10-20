package com.NCProject.MusicSchool.Models;

import com.NCProject.MusicSchool.Specialization;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Specialization specialization;
    private LocalDateTime execution;
    @Transient
    private Teacher teacher;

    private String teacherLogin;



    @Transient
    private List<Student> students;
    @Transient
    private final int maxStudents = 4;

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public LocalDateTime getExecution() {
        return execution;
    }

    public void setExecution(LocalDateTime execution) {
        this.execution = execution;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Student> getStudents() {
        return students;
    }

    public boolean isActive() {
        return execution.isBefore(LocalDateTime.now());
    }

    public boolean addStudent(Student student) throws Exception {
        if (students.size() <= maxStudents) {
            students.add(student);
            return true;
        } else {
            return false;
           // throw new Exception("the List Of Students is full");
        }
    }

    public boolean removeStudent(Student student) {
        return students.remove(student);
    }


}
