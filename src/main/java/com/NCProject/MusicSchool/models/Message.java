package com.NCProject.MusicSchool.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) //одному отправителю соответскует множество сообщений
    //  @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToMany( fetch = FetchType.EAGER)
    private Set<User> recipients;

    private String text;

    private LocalDateTime timestamps;

    private String fileName;


    public Message() {
        this.timestamps = LocalDateTime.now();
    }

    public Message(Long id, User sender, Set<User> recipients, String text,  String fileName) {
        this.id = id;
        this.sender = sender;
        this.recipients = recipients;
        this.text = text;
        this.timestamps = LocalDateTime.now();
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Set<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<User> recipients) {
        this.recipients = recipients;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(LocalDateTime timestamps) {
        this.timestamps = timestamps;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
