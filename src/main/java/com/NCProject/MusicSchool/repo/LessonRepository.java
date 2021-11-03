package com.NCProject.MusicSchool.repo;

import com.NCProject.MusicSchool.models.Lesson;
import com.NCProject.MusicSchool.models.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Lesson findByExecution(LocalDateTime execution);
    List<Lesson> findBySpecialization(Specialization specialization);
}
