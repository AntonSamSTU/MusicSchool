package com.NCProject.MusicSchool.service;

import com.NCProject.MusicSchool.models.Lesson;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

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

    public List<User> findBySpecialization(Specialization specialization)
    {
        return userRepository.findBySpecialization(specialization);
    }

    public boolean saveUser(User user, String username, String password, String name, String surname) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB == null) {
            user.setUsername(username);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setName(name);
            user.setSurname(surname);
            user.setRoles(Set.of(Role.USER, Role.STUDENT )); //TODO
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean saveUser(User user){
        userRepository.save(user);
        return userRepository.existsById(user.getId());
    }

    public void saveAllUsers(Iterable<User> users){
        userRepository.saveAll(users);
    }
//
//    public boolean deleteUser(User user) {
//        userRepository.delete(user);
//        return userRepository.findByUsername(user.getUsername()) == null;
//    }

    public boolean deleteUser(Long userID) {

        User userFromDB = findUser(userID);
        if (userFromDB == null || userFromDB.getRoles().contains(Role.ADMIN)) {
            return false;
        }

        //  entityManager.createQuery("DELETE from users WHERE id = " +"'"+ userID +"'", User.class);

        if(userFromDB.getRoles().contains(Role.TEACHER)){

            lessonRepository.deleteAll(lessonRepository.findByTeacher(userFromDB));
        }

        //TODO удалить ссылки в таблице отношений для студента. КАК?
        userRepository.deleteById(userID);

        return !userRepository.existsById(userID);


    }
//    public boolean deleteUser(String username) {
//        User userFromBd = userRepository.findByUsername(username);
//        if (userFromBd == null) {
//            return false;
//        } else {
//            userRepository.delete(userFromBd);
//            return true;
//        }
//    }

}
