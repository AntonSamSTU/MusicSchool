package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Lesson;
import com.NCProject.MusicSchool.models.Specialization;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.LessonRepository;
import com.NCProject.MusicSchool.repo.UserRepository;
import com.NCProject.MusicSchool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Controller
public class TeacherController {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/teacher") //take user's request
    public String teacher(Model model) { //returns someone template for  U request
        Iterable<Lesson> lessons = lessonRepository.findAll();

        model.addAttribute("lessons", lessons);

        return "teacher";
    }

    @PostMapping("/teacher")
    public String addLesson(@AuthenticationPrincipal User teacher, @RequestParam String execution, Model model) {
        Iterable<User> usersFromDB = userRepository.findBySpecialization(teacher.getSpecialization());
        Set<User> users = new java.util.HashSet<>(Set.of());
        for (User value :
                usersFromDB) {
            if( !value.getUsername().equals(teacher.getUsername()))  users.add(value);
        }
       // users.remove(teacher);
        try {

//            Lesson lesson = new Lesson(LocalDateTime.parse(execution, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
//                    Specialization.valueOf(specialization.toUpperCase()), teacher);

            Lesson lesson = new Lesson(LocalDateTime.parse(execution, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    teacher.getSpecialization(), teacher);

            if (users.size() > 0) {
                lesson.setUsers(users);
            }

            lessonRepository.save(lesson);
        } catch (Exception e) {

        }
            Iterable<Lesson> lessons = lessonRepository.findAll();
            model.addAttribute("lessons", lessons);

        return "teacher";
    }
}
