package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.*;
import com.NCProject.MusicSchool.repo.LessonRepository;
import com.NCProject.MusicSchool.repo.MessageRepository;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Collections;

@Controller
public class StudentController {

    private static final Logger logger = Logger.getLogger(StudentController.class);

    @Autowired
    MessageRepository messageRepository;

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
        lessonsFromDBIndividual.removeIf(value -> !value.getUsers().contains(student));

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
        Lesson lessonFromDB = lessonRepository.getById(lessonId);
        if (action.equals("delete")) {

            lessonFromDB.getUsers().remove(student);

            //Нотификация для учителя, у которого студень удалился с урока.
            Message notificationToTeacher = new Message(student, Set.of(lessonFromDB.getTeacher()), "из Вашего  урока на время " + lessonFromDB.getExecution()
                    + " Удалился студент " + student);

            messageRepository.save(notificationToTeacher);

            lessonRepository.save(lessonFromDB);
        }
        if (action.equals("deleteLesson")) {
            lessonRepository.deleteById(lessonId);
            logger.info("Personal Lesson with TIME '" + lessonFromDB.getExecution() + "' has deleted by STUDENT with USERNAME '" + student.getUsername()
                    + "' and with ID ' " + student.getId() + "'");

            //Нотификация для учителя, у которого студень удалил персональный урок.
            Message notificationToTeacher = new Message(student, Set.of(lessonFromDB.getTeacher()), "Персональный урок на время " + lessonFromDB.getExecution()
                    + " Удалил студент " + student);

            messageRepository.save(notificationToTeacher);
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

            //Нотификация для учителя, у которого добавился в урок студень.

            Message notificationToTeacher = new Message(student, Set.of(lessonFromDB.getTeacher()), "В Ваш урок на время " + lessonFromDB.getExecution()
                    + " Записался студент " + student);

            messageRepository.save(notificationToTeacher);


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

            //Нотификация для учителя, у которого появился персональный урок.
            Message notificationToTeacher = new Message(student, Set.of(teacherFromDB), "У вас новый персональный урок на время " + executionLDT
                    + " Со студентом " + student);

            messageRepository.save(notificationToTeacher);

            lessonRepository.save(lesson);

            logger.info("Personal Lesson with TIME '" + lesson.getExecution() + "' has created by STUDENT with USERNAME '" + student.getUsername()
                    + "' and with ID ' " + student.getId() + "'");
        } catch (Exception e) {
            logger.warn("EXCEPTION in Teacher Controller", e);
        }
        return "redirect:/student";
    }


}
