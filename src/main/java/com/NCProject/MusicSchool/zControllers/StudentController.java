package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Lesson;
import com.NCProject.MusicSchool.models.Role;
import com.NCProject.MusicSchool.models.Specialization;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Collections;

@Controller
public class StudentController {

    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    UserService userService;

    @GetMapping("/student") //take user's request
    public String student(@AuthenticationPrincipal User student, Model model) { //returns someone template for  U request

        model.addAttribute("message", "Hello student " + student.getUsername());

        //ищем уроки, в у которых специализация юзера

        List<Lesson> lessonsFromDB = lessonRepository.findBySpecialization(student.getSpecialization());

        //Создание и заполнение листа индивидуальных занятий
        ArrayList<Lesson> lessonsFromDBIndividual = new ArrayList<>();

        for (Lesson value :
                lessonsFromDB) {
            if (value.isIndividual()) {
                //Добавили в лист индивидуальных занятий
                lessonsFromDBIndividual.add(value);
            }
        }

        //Оставили в списке индивидуальных только те, у которых соответсвующий студент
        lessonsFromDBIndividual.removeIf(value -> ! value.getUsers().contains(student));

        //удалили из листа общих занятий индивидуальные.
        lessonsFromDB.removeIf(Lesson::isIndividual);

        //отсортировали списки по времени
        lessonsFromDB.sort(Comparator.comparing(Lesson::getExecution));

        lessonsFromDBIndividual.sort(Comparator.comparing(Lesson::getExecution));



        //Добавили в модель НЕ индивидуальные уроки
        model.addAttribute("lessons", lessonsFromDB);

        //Добавили в модель ИНДИВИДУАЛЬНЫЕ уроки
        model.addAttribute("lessonsIndividual", lessonsFromDBIndividual);


        //Поиск преподов по специальности студента
        List<User> usersFromDBwCurrentSpecialization = userService.findBySpecialization(student.getSpecialization());
        //Если нет роли препода то убираем из листа
        usersFromDBwCurrentSpecialization.removeIf(value -> !value.getRoles().contains(Role.TEACHER));
        //мастерское (нет) избегание NPE
        model.addAttribute("teacher", new ArrayList<>());
        //Добавляем в модель учителей со специальностью студня
        model.addAttribute("teachers", usersFromDBwCurrentSpecialization);

        return "student";
    }

    @PostMapping("/student/delete/{lessonId}")
    public String deleteStudentFromLesson(@AuthenticationPrincipal User student, @PathVariable("lessonId") Long lessonId,
                                          @RequestParam(required = true, defaultValue = "") String action,
                                          Model model) {
        if (action.equals("delete")) {
            Lesson lessonFromDB = lessonRepository.getById(lessonId);

            lessonFromDB.getUsers().remove(student);

            lessonRepository.save(lessonFromDB);
        }
        if(action.equals("deleteLesson")){
            lessonRepository.deleteById(lessonId);
        }
        return "redirect:/student";
//        return student(student, model);
    }

    @PostMapping("/student/add/{lessonId}")
    public String addStudentToLesson(@AuthenticationPrincipal User student, @PathVariable("lessonId") Long lessonId,
                                     @RequestParam(required = true, defaultValue = "") String action,
                                     Model model) {
        if (action.equals("add")) {
            Lesson lessonFromDB = lessonRepository.getById(lessonId);


            lessonFromDB.getUsers().add(student);

            lessonRepository.save(lessonFromDB);
        }
        return "redirect:/student";
//        return student(student, model);
    }

    //TODO добавление в базу персонального урока.
    @PostMapping("/student/addLesson")
    public String addPersonalLesson(@AuthenticationPrincipal User student, @RequestParam String execution, @RequestParam String teacher, Model model) {

        try {

            LocalDateTime executionLDT = LocalDateTime.parse(execution, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            User teacherFromDB = userService.findUser(teacher);

            Set<User> oneStudent = Collections.singleton(student);

            Specialization specialization = student.getSpecialization();

            Lesson lesson = new Lesson(executionLDT, specialization, teacherFromDB, oneStudent, true);

            lessonRepository.save(lesson);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/student";
    }


}
