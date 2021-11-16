package com.NCProject.MusicSchool.repo;

import com.NCProject.MusicSchool.models.Specialization;
import com.NCProject.MusicSchool.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findBySpecialization(Specialization specialization);
}
