package com.NCProject.MusicSchool.repo;

import com.NCProject.MusicSchool.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Lesson findByExecution(LocalDateTime execution);
}
