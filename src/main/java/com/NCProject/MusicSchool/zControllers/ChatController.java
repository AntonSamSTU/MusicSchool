package com.NCProject.MusicSchool.zControllers;

import com.NCProject.MusicSchool.models.Message;
import com.NCProject.MusicSchool.models.User;
import com.NCProject.MusicSchool.repo.MessageRepository;
import com.NCProject.MusicSchool.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Controller
public class ChatController {

    private static final Logger logger = Logger.getLogger(ChatController.class);

    private static final double CONVERT_TO_MBS = 0.00000095367432;

    private static final double MAX_FILE_SIZE = 10;

    private static final Set<String> ALLOWED_FORMATS = Set.of("jpeg", "jpg", "png", "mp3", "doc", "docx", "pdf");

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    UserService userService;

    @Autowired
    MessageRepository messageRepository;


    @GetMapping("/chat")
    public String chatGet(@AuthenticationPrincipal User user, Model model) {


        List<Message> messagesToUser = messageRepository.findAll();

        //Если нотификация, то удалили из листа
        messagesToUser.removeIf(Message::isNotification);

        List<User> allUsers = userService.findAllUsers();

        //удалили из всех того, кто хочет отправить сообщение
        allUsers.remove(user);

        //Оставили в сообщениях для пользователя только соответсвующие
        messagesToUser.removeIf(value -> !value.getRecipients().contains(user));

        //Сообщения, которые отправил пользователь
        List<Message> messagesFromUser = messageRepository.findBySender(user);
        //Если нотификация, то удалили из листа
        messagesFromUser.removeIf(Message::isNotification);

        //Добавили все в модель
        model.addAttribute("about", "Hello user " + user.getUsername());

        model.addAttribute("users", allUsers);

        model.addAttribute("messagesToUser", messagesToUser);

        model.addAttribute("messagesFromUser", messagesFromUser);

        return "chat";
    }

    //Добавить новое сообщение
    @PostMapping("/chat")
    public String addMsg(@AuthenticationPrincipal User user, Model model, @RequestParam Long[] selectedusers,
                         @RequestParam(defaultValue = "null") String text, @RequestParam("file") MultipartFile file
    ) throws IOException {


        try {
            if (text.equals("null")) {
                throw new NullPointerException("empty text of message");
            }
            Set<User> recipients = new HashSet<>();

            //Добавили всех получателей, кто в чекбоксе
            for (Long userID :
                    selectedusers) {
                User userFromDB = userService.findUser(userID);
                recipients.add(userFromDB);
            }

            //Создали сообщение и указали время
            Message message = new Message();

            message.setRecipients(recipients);
            //Добавили отправителя
            message.setSender(user);

            //Добавили текст сообщения
            message.setText(text);


            //Добавили файл
            //Проверка файла
            if (file != null && checkFile(file)) {
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
                logger.info("User with username " + user.getUsername() + " uploaded the file to the server with filename " + resultFileName);
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

    //Удалить сообщение
    @PostMapping("/chat/delete/{messageID}")
    public String deleteMsg(@AuthenticationPrincipal User user, @PathVariable("messageID") Message messageID, //спринг сразу обращается к репозирию и ищет сообщение
                            @RequestParam(required = true, defaultValue = "") String action) {

        if (action.equals("delete")) {

            //Удалили из получателей, чтобы не отображалось.
            messageID.getRecipients().remove(user);
            //Если никого нет в получателях, то удаляем файл и удаляем сообщение из БД
            if (messageID.getRecipients().isEmpty()) {
                new File(uploadPath + "/" + messageID.getFileName()).delete();
                messageRepository.delete(messageID);
            } else {
                messageRepository.save(messageID);
            }

        }
        return "redirect:/chat";
    }


    //Скачать файл
    @RequestMapping(value = "/chat/download", method = RequestMethod.GET)
    public ResponseEntity<Object> downloadFile(@Param(value = "fileName") String fileName) throws IOException {


        //  Message messageFromDB = messageRepository.getById(id);

        String fullPath = uploadPath + "/" + fileName;

        File file = new File(fullPath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object>
                responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(
                MediaType.parseMediaType("application/txt")).body(resource);

        return responseEntity;
    }

    //Проверка файла на размер и форматы
    private boolean checkFile(MultipartFile file) {

        boolean result = true;

        //Если пустое название файла
        if (file.getOriginalFilename().isEmpty()) {
            result = false;
        }
        //Если больше разрешенного размера
        if (file.getSize() * CONVERT_TO_MBS > MAX_FILE_SIZE) {
            result = false;
        }

        //Если не разрешенный формат

        String[] splitedFileName = file.getOriginalFilename().split("\\.");

        String fileFormat = splitedFileName[splitedFileName.length - 1];
        if ( !ALLOWED_FORMATS.contains(fileFormat)) {
            result = false;
        }

        return result;
    }


}
