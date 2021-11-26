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

    private boolean individual;

    @ManyToOne(fetch = FetchType.EAGER) //одному тичеру соответскует множество уроков
    @JoinColumn(name = "teacher_id")
    User teacher;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "lessons_users")
    Set<User> users;

    public Lesson() {
    }

    public Lesson(LocalDateTime execution, Specialization specialization, User teacher, boolean individual) {
        this.execution = execution;
        this.specialization = specialization;
        this.teacher = teacher;
        this.individual = individual;
    }

    public Lesson(LocalDateTime execution, Specialization specialization, User teacher, Set<User> users, boolean individual) {
        this.execution = execution;
        this.specialization = specialization;
        this.teacher = teacher;
        this.users = users;
        this.individual = individual;
    }

    public boolean isIndividual() {
        return individual;
    }

    public void setIndividual(boolean individual) {
        this.individual = individual;
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
