package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Role;
import com.NCProject.MusicSchool.models.Specialization;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.UserRepository;
import com.NCProject.MusicSchool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class RegistrationController {


    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model,
                          @RequestParam String username, @RequestParam String password,
                          @RequestParam String name, @RequestParam String surname, @RequestParam String specialization) {
        user.setSpecialization(Specialization.valueOf(specialization));
        if (userService.saveUser(user, username, password, name, surname)) {
            return "redirect:/login";
        } else {
            model.addAttribute("message", "User already exist!");
            return "registration";
        }

    }
}
