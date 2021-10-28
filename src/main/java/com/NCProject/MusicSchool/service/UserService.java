package com.NCProject.MusicSchool.service;

import com.NCProject.MusicSchool.models.Role;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User doesnt Exist!");
        } else {
            return user;
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
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

    public boolean deleteUser(User user) {
        userRepository.delete(user);
        return userRepository.findByUsername(user.getUsername()) == null;
    }

    public boolean deleteUser(String username) {
        User userFromBd = userRepository.findByUsername(username);
        if (userFromBd == null) {
            return false;
        } else {
            userRepository.delete(userFromBd);
            return true;
        }
    }
}
