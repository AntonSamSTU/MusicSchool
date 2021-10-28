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
public class TeacherController {

    @Autowired
    private LessonRepository lessonRepository;

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
}
