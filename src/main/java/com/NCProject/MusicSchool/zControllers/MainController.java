package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.MusicSchoolApplication;
import com.NCProject.MusicSchool.models.Role;
import com.NCProject.MusicSchool.models.User;
import org.apache.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    private static final Logger logger = Logger.getLogger(MainController.class);

    //take user's repo


    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("about", "Это главная страница, выберите одно из действий");
        return "main";
    }

    @PostMapping("/main")
    public String redirect(@AuthenticationPrincipal User user) {

        if (user.getRoles().contains(Role.STUDENT)) {
            logger.info("STUDENT with username '" + user.getUsername() + "' and with id '" + user.getId() + "' has successfully logged into App ");
            return "redirect:/student";
        }

        if (user.getRoles().contains(Role.TEACHER)) {
            logger.info("TEACHER with username '" + user.getUsername() + "' and with id '" + user.getId() + "' has successfully logged into App ");
            return "redirect:/teacher";
        }

        if (user.getRoles().contains(Role.ADMIN)) {
            logger.info("ADMIN with username '" + user.getUsername() + "' and with id '" + user.getId() + "' has successfully logged into App ");
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