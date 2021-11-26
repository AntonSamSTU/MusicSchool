package com.NCProject.MusicSchool.service;

import com.NCProject.MusicSchool.models.Role;
import com.NCProject.MusicSchool.models.Specialization;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.LessonRepository;
import com.NCProject.MusicSchool.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LessonRepository lessonRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User doesnt Exist!");
        } else {
            return user;
        }
    }

    public User findUser(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUser(Long userID) {
        return userRepository.findById(userID).orElse(null);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findBySpecialization(Specialization specialization) {
        return userRepository.findBySpecialization(specialization);
    }

    public boolean saveUser(User user, String username, String password, String name, String surname) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB == null) {
            user.setUsername(username);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setName(name);
            user.setSurname(surname);
            user.setRoles(Set.of(Role.USER, Role.STUDENT));
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean saveUser(User user) {
        userRepository.save(user);
        return userRepository.existsById(user.getId());
    }

}
