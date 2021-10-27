package com.NCProject.MusicSchool.repo;

import com.NCProject.MusicSchool.Models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
