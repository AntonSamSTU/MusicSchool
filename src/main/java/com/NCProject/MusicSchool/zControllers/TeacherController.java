package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Lesson;
import com.NCProject.MusicSchool.models.Role;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.LessonRepository;
import com.NCProject.MusicSchool.repo.UserRepository;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class TeacherController {

    private static final Logger logger = Logger.getLogger(TeacherController.class);

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    public Lesson findLesson(Long lessonID) {
        return lessonRepository.findById(lessonID).orElse(null);
    }

    @GetMapping("/teacher") //take user's request
    public String teacher(Model model, @AuthenticationPrincipal User teacher) { //returns someone template for  U request

        //Все общие уроки
        List<Lesson> lessons = lessonRepository.findAll();

        lessons.removeIf(Lesson::isIndividual);
        lessons.sort(Comparator.comparing(Lesson::getExecution));
        model.addAttribute("lessons", lessons);
        model.addAttribute("about", "Hello teacher " + teacher.getUsername());

        //Групповые уроки учителя
        List<Lesson> lessonsTeacherGroup = lessonRepository.findByTeacher(teacher);
        //оставили только групповые учителя
        lessonsTeacherGroup.removeIf(Lesson::isIndividual);
        //отсортировали общие учителя
        lessonsTeacherGroup.sort(Comparator.comparing(Lesson::getExecution));
        //добавили в модель
        model.addAttribute("lessonsGroup", lessonsTeacherGroup);

        //Индивидуальные уроки учителя
        List<Lesson> lessonsTeacherIndividual = lessonRepository.findByTeacher(teacher);
        //оставили только индивидуальные учителя
        lessonsTeacherIndividual.removeIf(value -> !value.isIndividual());
        //отсортировали индивидуальные учителя
        lessonsTeacherIndividual.sort(Comparator.comparing(Lesson::getExecution));
        //добавили в модель
        model.addAttribute("lessonsIndividual", lessonsTeacherIndividual);


        return "teacher";
    }

    @PostMapping("/teacher")
    public String addLesson(@AuthenticationPrincipal User teacher, @RequestParam String execution, Model model) {
        //ищем всех юзеров в БД, у которых такая же специальность, как у учителя
        Iterable<User> usersFromDB = userRepository.findBySpecialization(teacher.getSpecialization());

        Set<User> students = new java.util.HashSet<>(Set.of());
        for (User value :
                usersFromDB) {
            //что бы не добавить в ученики учителей
            if (value.getRoles().contains(Role.STUDENT)) students.add(value);
        }

        try {
            Lesson lesson = new Lesson(LocalDateTime.parse(execution, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    teacher.getSpecialization(), teacher, false);

            if (students.size() > 0) {
                lesson.setUsers(students);
            }

            lessonRepository.save(lesson);

            logger.info("Lesson with TIME '" + lesson.getExecution() + "' has created by TEACHER with USERNAME '" + teacher.getUsername()
                    + "' and with ID ' " + teacher.getId() + "'");
        } catch (Exception e) {
            logger.warn("EXCEPTION in Teacher Controller", e);
        }
        return "redirect:/teacher";
        // return teacher(model, teacher);
    }

    @PostMapping("/teacher/delete/{lessonId}")
    public String deleteLesson(@AuthenticationPrincipal User teacher, @PathVariable("lessonId") Long lessonId,
                               @RequestParam(required = true, defaultValue = "") String action,
                               Model model) {
        if (action.equals("delete")) {
            Lesson lessonFromDB = findLesson(lessonId);
            if (lessonFromDB != null) {
                if (teacher.getId().equals(lessonFromDB.getTeacher().getId())) {
                    lessonRepository.deleteById(lessonId);
                }
            }
        }

        return "redirect:/teacher";
    }

    @PostMapping("/teacher/update/{lessonId}")
    public String updateLesson(@AuthenticationPrincipal User teacher, @PathVariable("lessonId") Long lessonId,
                               @RequestParam(required = true, defaultValue = "") String action,
                               Model model, @RequestParam String execution) {

        Lesson lessonFromDB = lessonRepository.getById(lessonId);
        if (action.equals("update")) {
            if (teacher.getId().equals(lessonFromDB.getTeacher().getId())) {
                try {
                    LocalDateTime newExecution = LocalDateTime.parse(execution, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    //         lessonRepository.deleteById(lessonId);
                    lessonFromDB.setExecution(newExecution);

                    //метод save() работает как update, если в БД есть поле, у которого такое же ID, как у объекта
                    lessonRepository.save(lessonFromDB);
                    logger.info("Lesson with TIME '" + lessonFromDB.getExecution() + "' has changed by TEACHER with USERNAME '" + teacher.getUsername()
                            + "' and with ID ' " + teacher.getId() + "' to NEW TIME '" + lessonFromDB.getExecution() + "'");
                } catch (Exception e) {
                    logger.warn("EXCEPTION in Teacher Controller", e);
                }
            }
        }
        return "redirect:/teacher";
        //    return teacher(model);
    }


}
