package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Lesson;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String admin(Model model){

        model.addAttribute("users", userService.findAllUsers());
        return "admin";
    }

    @PostMapping("/admin/delete/{userID}")
    public String deleteUser(@AuthenticationPrincipal User admin, @PathVariable("userID") Long userID,
                               @RequestParam(required = true, defaultValue = "") String action,
                               Model model) {
        if (action.equals("delete")) {
          boolean j = userService.deleteUser(userID);
        }

        return "redirect:/admin";
    }

    @PostMapping("/admin/update/{userID}")
    public String updateUser(@AuthenticationPrincipal User admin, @PathVariable("userID") Long userID,
                               @RequestParam(required = true, defaultValue = "") String action,
                               Model model) {
        if (action.equals("update")) {
         //TODO update user role
        }

        return "redirect:/admin";
    }
}
