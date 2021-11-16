package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GreetingController {
    @GetMapping
    public String main(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("about", "Это лендинг страница");

        if (user != null) {
            return "redirect:/main";
        } else {
            return "greeting";
        }
    }
}
