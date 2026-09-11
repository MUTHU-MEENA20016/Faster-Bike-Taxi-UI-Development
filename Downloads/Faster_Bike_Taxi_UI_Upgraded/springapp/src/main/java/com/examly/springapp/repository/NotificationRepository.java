package com.examly.springapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByIsRead(Boolean isRead);

    List<Notification> findByType(Notification.NotificationType type);

    List<Notification> findByUserUsernameOrderByCreatedDateDesc(String username);
}



