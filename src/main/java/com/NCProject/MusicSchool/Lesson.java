package com.NCProject.MusicSchool;

import java.time.LocalDateTime;
import java.util.List;

public class Lesson {
    private Specialization specialization;
    private LocalDateTime execution;
    private Teacher teacher;
    private List<Student> students;
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

    public void setStudents(List<Student> students) {
        this.students = students;
    }


}
