package com.NCProject.MusicSchool.repo;

import com.NCProject.MusicSchool.models.Message;
import com.NCProject.MusicSchool.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySender(User sender);
}
