package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Lesson;
import com.NCProject.MusicSchool.models.Role;
import com.NCProject.MusicSchool.models.Specialization;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.LessonRepository;
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
public class MainController {

    //take user's repo


    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("about", "Это главная страница, выберите одно из действий");
        return "main";
    }

    @PostMapping("/main")
    public String addLesson(@AuthenticationPrincipal User user) {

        if(user.getRoles().contains(Role.STUDENT)){
           return "redirect:/student";
        }

        if(user.getRoles().contains(Role.TEACHER)){
            return "redirect:/teacher";
        }

        if(user.getRoles().contains(Role.ADMIN)){
            return "redirect:/admin";
        }
      return null;
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