package com.NCProject.MusicSchool.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime execution;

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    @ManyToOne(fetch = FetchType.EAGER) //одному тичеру соответскует множество уроков
    @JoinColumn(name ="user_id")
    User teacher;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<User> users;

    public Lesson() {
    }

    public Lesson(LocalDateTime execution, Specialization specialization, User teacher) {
        this.execution = execution;
        this.specialization = specialization;
        this.teacher = teacher;
    }

    public Lesson(LocalDateTime execution, Specialization specialization, User teacher, Set<User> users) {
        this.execution = execution;
        this.specialization = specialization;
        this.teacher = teacher;
        this.users = users;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getExecution() {
        return execution;
    }

    public void setExecution(LocalDateTime execution) {
        this.execution = execution;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
