package com.NCProject.MusicSchool.controllers;

import com.NCProject.MusicSchool.Models.Lesson;
import com.NCProject.MusicSchool.repo.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping("/")
    public String home(Model model) {
        Iterable<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("title", "Главная страница");
        model.addAttribute("lessons", lessons);

        return "home";
    }

}