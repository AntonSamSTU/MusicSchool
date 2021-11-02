package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Lesson;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @Autowired
    LessonRepository lessonRepository;

    @GetMapping("/student") //take user's request
    public String home(@AuthenticationPrincipal User student, Model model) { //returns someone template for  U request

        model.addAttribute("about", "Это страница студента, на которой" +
                " ему надо будет записаться на курс из предложенных");

        //TODO dodelat
        Iterable<Lesson> lessons = lessonRepository.findAll();
        Iterable<Lesson> lessonsAtt;
        for (Lesson value: lessons) {
            if (value.getSpecialization().equals(student.getSpecialization())){

            }
        }
        //   model.addAttribute("lessons", lessons);

        return "student";
    }
}
