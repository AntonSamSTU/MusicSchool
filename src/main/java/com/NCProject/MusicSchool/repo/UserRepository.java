package com.NCProject.MusicSchool.repo;

import com.NCProject.MusicSchool.models.User;
import org.springframework.data.repository.CrudRepository;

//если что переделать на jpa
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
