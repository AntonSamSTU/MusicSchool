package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Specialization;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private static final Logger logger = Logger.getLogger(RegistrationController.class);

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
            logger.info("USER with username '"+username +"' has successfully  registered");
            return "redirect:/login";
        } else {
            model.addAttribute("message", "User already exist!");
            return "registration";
        }

    }
}
