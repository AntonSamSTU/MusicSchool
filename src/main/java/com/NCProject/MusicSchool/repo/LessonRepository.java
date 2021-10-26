package com.NCProject.MusicSchool.repo;

import com.NCProject.MusicSchool.Models.Lesson;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface LessonRepository extends CrudRepository<Lesson, Long> {
    Lesson findByExecution(LocalDateTime execution);
}
