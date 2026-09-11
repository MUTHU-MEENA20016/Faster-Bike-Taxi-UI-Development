package com.examly.springapp.service;

import java.util.List;
import com.examly.springapp.model.Notification;

public interface NotificationService {

  Notification saveNotification(
      Notification notification);

  List<Notification> getAllNotifications();

  Notification getNotificationById(Long id);

  Notification updateNotification(
      Long id,
      Notification notification);

  void deleteNotification(Long id);
}



