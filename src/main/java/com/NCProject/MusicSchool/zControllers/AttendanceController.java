package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Lesson;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.LessonRepository;
import com.NCProject.MusicSchool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

//TODO система посещений
@Controller
public class AttendanceController {

    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    UserService userService;

    @GetMapping("attendance/{lessonId}") //take user's request
    public String teacherLessonAttendance(
            @PathVariable("lessonId") Long lessonId,
            Model model) { //returns someone template for  U request

        Lesson lessonFromDB = lessonRepository.getById(lessonId);
        //TODO поставить проверку, прошел ли урок к настоящему моменту, а после отметки удалить урок


        model.addAttribute("lesson", lessonFromDB);

        model.addAttribute("students", lessonFromDB.getUsers());

        return "attendance";
    }

    @PostMapping("attendance/{lessonId}") //take user's request
    public String teacherLessonAttendanceSave(@AuthenticationPrincipal User teacher, @PathVariable("lessonId") Lesson lessonId,
                                              @RequestParam Map<String, String> form,
                                              @RequestParam String [] studentId,
                                              Model model) { //returns someone template for  U request

        //TODO затуп с помощью таймлифа и дефолтных параметров решить.

        boolean j = false;
        //итерация по студентам
        for (User student :
                lessonId.getUsers()) {
            //итерация по форме
            for (String value : form.values()) {
                if (value.contains("yes")) {
                    student.stagePlusPlus();
                }
            }
        }

        userService.saveAllUsers(lessonId.getUsers());

        return "redirect:/teacher";
    }
}
