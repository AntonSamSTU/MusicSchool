package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Lesson;
import com.NCProject.MusicSchool.models.Specialization;
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


    @GetMapping
    public String main(Model model) {
        model.addAttribute("about", "Это лендинг страница сайта, на" +
                " которой будет ссылка на регистрацию, авторизацию и инфа про курсы");
        return "main";
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