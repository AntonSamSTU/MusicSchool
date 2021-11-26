package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Lesson;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.LessonRepository;
import com.NCProject.MusicSchool.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;


@Controller
public class AttendanceController {


    private static final Logger logger = Logger.getLogger(AttendanceController.class);
    @Autowired
    LessonRepository lessonRepository;

    @Autowired
    UserService userService;

    @GetMapping("attendance/{lessonId}") //take user's request
    public String teacherLessonAttendance(
            @PathVariable("lessonId") Long lessonId,
            Model model) { //returns someone template for  U request

        Lesson lessonFromDB = lessonRepository.getById(lessonId);
        //TODO для полноценного бизнес - процесса если время урока > LocalDateTime.now(), то кнопка доступна, и после отмечания урок удаляется

        model.addAttribute("lesson", lessonFromDB);

        model.addAttribute("students", lessonFromDB.getUsers());

        return "attendance";
    }

    @PostMapping("attendance/{lessonId}") //take user's request
    public String teacherLessonAttendanceSave(@AuthenticationPrincipal User teacher, @PathVariable("lessonId") Lesson lessonId,
                                              @RequestParam Long[] action,
                                              Model model) { //returns someone template for  U request
        ArrayList<Long> IDsforLogger = new ArrayList<>();

        for (Long userID :
                action) {
            User studentFromDB = userService.findUser(userID);
            studentFromDB.stagePlusPlus();
            IDsforLogger.add(studentFromDB.getId());
            userService.saveUser(studentFromDB);
        }

        if (IDsforLogger.size() > 0) {
            logger.info("In a LESSON with an ID '" + lessonId.getId() + "' , a Teacher with a USERNAME '" + teacher.getUsername()
                    + "' marked students with an ID's: '" + IDsforLogger + "'");
        }
        return "redirect:/teacher";
    }
}
