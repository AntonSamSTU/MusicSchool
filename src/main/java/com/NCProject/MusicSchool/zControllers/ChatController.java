package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Message;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.MessageRepository;
import com.NCProject.MusicSchool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class ChatController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    UserService userService;

    @Autowired
    MessageRepository messageRepository;


    @GetMapping("/chat")
    public String chatGet(@AuthenticationPrincipal User user, Model model) {


        List<Message> messagesToUser = messageRepository.findAll();

        List<User> allUsers = userService.findAllUsers();

        //удалили из всех того, кто хочет отправить сообщение
        allUsers.remove(user);

        //Оставили в сообщениях для пользователя только соответсвующие
        messagesToUser.removeIf(value -> !value.getRecipients().contains(user));

        //Сообщения, которые отправил пользователь
        List<Message> messagesFromUser = messageRepository.findBySender(user);

        //Добавили все в модель
        model.addAttribute("about", "Hello user " + user.getUsername());

        model.addAttribute("users", allUsers);

        model.addAttribute("messagesToUser", messagesToUser);

        model.addAttribute("messagesFromUser", messagesFromUser);

        return "chat";
    }

    @PostMapping("/chat")
    public String addMsg(@AuthenticationPrincipal User user, Model model, @RequestParam Long[] selectedusers,
                         @RequestParam(defaultValue = "null") String text, @RequestParam("file") MultipartFile file
    ) throws IOException {


        try {

            if (text.equals("null")) {
                throw new NullPointerException("empty text of message");
            }

            //Создали сообщение и указали время
            Message message = new Message();

            Set<User> recipients = new HashSet<>();

            //Добавили всех получателей, кто в чекбоксе
            for (Long userID :
                    selectedusers) {

                User userFromDB = userService.findUser(userID);
                recipients.add(userFromDB);
            }


            message.setRecipients(recipients);
            //Добавили отправителя
            message.setSender(user);

            //Добавили текст сообщения
            message.setText(text);


            //Добавили файл
            if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                //Папка хранения
                File uploadFolder = new File(uploadPath);
                //Если папки не сущ, то создаем новую
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdir();
                }
                //Создали имя файла с уникальным ID
                String resultFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
                //Пересестили в папку
                file.transferTo(new File(uploadPath + "/" + resultFileName));
                //Засетали в сообщение
                message.setFileName(resultFileName);
            } else {
                message.setFileName("null");
            }

            messageRepository.save(message);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return "redirect:/chat";
    }


    @RequestMapping(value = "/chat/download", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource downloadFile(@Param(value = "id") Long id) {
        Message message = messageRepository.getById(id);
        return new FileSystemResource(new File(uploadPath + "/"+ message.getFileName()));
    }


}
