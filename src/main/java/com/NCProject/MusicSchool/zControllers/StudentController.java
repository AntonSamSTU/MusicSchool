package com.NCProject.MusicSchool.zControllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentController {

    @GetMapping("/student") //take user's request
    public String home(Model model) { //returns someone template for  U request
        //   Iterable<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("about", "Это страница студента, на которой" +
                " ему надо будет записаться на курс из предложенных");
        //   model.addAttribute("lessons", lessons);

        return "student";
    }
}
