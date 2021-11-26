package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.MusicSchoolApplication;
import com.NCProject.MusicSchool.models.Message;
import com.NCProject.MusicSchool.models.Role;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.MessageRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.util.List;

@Controller
public class MainController {
    private static final Logger logger = Logger.getLogger(MainController.class);

    @Autowired
    MessageRepository messageRepository;


    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user, Model model) {

        checkMessages();
        model.addAttribute("about", "Это главная страница, выберите одно из действий или ознакомьтесь с уведомлениями!");

        //Нашли все мсг
        List<Message> notifications = messageRepository.findAll();
        //Оставили только нотификации
        notifications.removeIf(value -> !value.isNotification());
        //Оставили только нотификации для юзера, который зашел на страницу
        notifications.removeIf(value -> !value.getRecipients().contains(user));
        //Добавили в модель
        model.addAttribute("notifications", notifications);

        return "main";
    }

    //Удалить нотификацию
    @PostMapping("/main/delete/{messageID}")
    public String deleteMsg(@PathVariable("messageID") Message messageID, //спринг сразу обращается к репозирию и ищет сообщение
                            @RequestParam(required = true, defaultValue = "") String action) {

        if (action.equals("delete")) {

            messageRepository.delete(messageID);
        }
        return "redirect:/main";
    }

    @PostMapping("/main")
    public String redirect(@AuthenticationPrincipal User user) {

        if (user.getRoles().contains(Role.STUDENT)) {
            logger.info("STUDENT with username '" + user.getUsername() + "' and with id '" + user.getId() + "' has successfully logged into App ");
            return "redirect:/student";
        }

        if (user.getRoles().contains(Role.TEACHER)) {
            logger.info("TEACHER with username '" + user.getUsername() + "' and with id '" + user.getId() + "' has successfully logged into App ");
            return "redirect:/teacher";
        }

        if (user.getRoles().contains(Role.ADMIN)) {
            logger.info("ADMIN with username '" + user.getUsername() + "' and with id '" + user.getId() + "' has successfully logged into App ");
            return "redirect:/admin";
        }
        return null;
    }

    public void checkMessages(){
        List<Message> messagesFromDB = messageRepository.findAll();
        for (Message value:
             messagesFromDB) {
            if(value.getRecipients().size() == 0){
                messageRepository.delete(value);
            }
        }
    }


//    public static void main(String[] args) {
//        MainController mainController = new MainController();
//        Iterable<Lesson> lessons = mainController.lessonRepository.findAll();
//
//        for (Lesson value : lessons) {
//            System.out.println(value.getExecution());
//            System.out.println(value.getSpecialization());
//        }
//    }

}