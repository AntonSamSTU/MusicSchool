package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.Models.Lesson;
import com.NCProject.MusicSchool.Models.Specialization;
import com.NCProject.MusicSchool.repo.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class MainController {

    //take user's repo

    @Autowired
    private LessonRepository lessonRepository;


    @GetMapping
    public String main(Model model) {
        model.addAttribute("about", "Это лендинг страница сайта, на" +
                " которой будет ссылка на регистрацию, авторизацию и инфа про курсы");
        return "main";
    }

    @GetMapping("/student") //take user's request
    public String home(Model model) { //returns someone template for  U request
        //   Iterable<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("about", "Это страница студента, на которой" +
                " ему надо будет записаться на курс из предложенных");
        //   model.addAttribute("lessons", lessons);

        return "student";
    }

    @GetMapping("/teacher") //take user's request
    public String teacher(Model model) { //returns someone template for  U request
        Iterable<Lesson> lessons = lessonRepository.findAll();

        model.addAttribute("lessons", lessons);

        return "teacher";
    }

    @PostMapping("/teacher")
    public String addLesson(@RequestParam String execution, @RequestParam String specialization, Model model) {
        try {
            Lesson lesson = new Lesson(LocalDateTime.parse(execution, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    Specialization.valueOf(specialization.toUpperCase()));
            lessonRepository.save(lesson);
            Iterable<Lesson> lessons = lessonRepository.findAll();
            model.addAttribute("lessons", lessons);
        } catch (Exception e) {

        }
        return "teacher";
    }

//    public static void main(String[] args) {
//        MainController mainController = new MainController();
//        Iterable<Lesson> lessons = mainController.lessonRepository.findAll();
//
//        for (Lesson value : lessons) {
//            System.out.println(value.getExecution());
//            System.out.println(value.getSpecialization());
//        }
//    }

}