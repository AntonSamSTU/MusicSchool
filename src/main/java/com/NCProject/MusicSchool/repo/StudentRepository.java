package com.NCProject.MusicSchool.repo;

import com.NCProject.MusicSchool.Models.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
}
