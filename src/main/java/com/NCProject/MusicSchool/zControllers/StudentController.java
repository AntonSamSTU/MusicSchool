package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Lesson;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.LessonRepository;
import org.hibernate.engine.internal.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Controller
public class StudentController {

    @Autowired
    LessonRepository lessonRepository;

    @GetMapping("/student") //take user's request
    public String student(@AuthenticationPrincipal User student, Model model) { //returns someone template for  U request

        model.addAttribute("message", "Hello student " + student.getUsername());

        //ищем уроки, в у которых специализация юзера
        List<Lesson> lessonsFromDB = lessonRepository.findBySpecialization(student.getSpecialization());

        lessonsFromDB.sort(Comparator.comparing(Lesson::getExecution));

        List<Lesson> lessonsWithStudent = lessonsFromDB;
        //если студента нет в уроке, то удаляем.
        // lessonsWithStudent.removeIf(value -> !value.getUsers().contains(student));

        model.addAttribute("lessons", lessonsWithStudent);

        return "student";
    }

    @PostMapping("/student/delete/{lessonId}")
    public String deleteStudentFromLesson(@AuthenticationPrincipal User student, @PathVariable("lessonId") Long lessonId,
                                          @RequestParam(required = true, defaultValue = "") String action,
                                          Model model) {
        if (action.equals("delete")) {
            Lesson lessonFromDB = lessonRepository.getById(lessonId);

            lessonRepository.delete(lessonFromDB);

            lessonFromDB.removeUserByUsername(student.getUsername());

            lessonRepository.save(lessonFromDB);
        }
        return "redirect:/student";
//        return student(student, model);
    }
    @PostMapping("/student/add/{lessonId}")
    public String addStudentToLesson(@AuthenticationPrincipal User student, @PathVariable("lessonId") Long lessonId,
                                          @RequestParam(required = true, defaultValue = "") String action,
                                          Model model) {
        if (action.equals("add")) {
            Lesson lessonFromDB = lessonRepository.getById(lessonId);

            lessonRepository.delete(lessonFromDB);

            lessonFromDB.addUser(student);

            lessonRepository.save(lessonFromDB);
        }
        return "redirect:/student";
//        return student(student, model);
    }


}
