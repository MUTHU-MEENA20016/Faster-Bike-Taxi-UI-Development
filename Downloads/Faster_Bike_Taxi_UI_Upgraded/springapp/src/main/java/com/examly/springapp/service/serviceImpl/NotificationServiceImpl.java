package com.examly.springapp.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Notification;
import com.examly.springapp.repository.NotificationRepository;
import com.examly.springapp.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

  @Autowired
  private NotificationRepository repository;

  @Override
  public Notification saveNotification(Notification notification) {
    return repository.save(notification);
  }

  @Override
  public List<Notification> getAllNotifications() {
    return repository.findAll();
  }

  @Override
  public Notification getNotificationById(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public Notification updateNotification(Long id, Notification notification) {

    Notification existing = repository.findById(id).orElse(null);

    if(existing != null){
      existing.setMessage(notification.getMessage());
      existing.setType(notification.getType());
      existing.setIsRead(notification.getIsRead());
      existing.setPriority(notification.getPriority());
      existing.setExpiryDate(notification.getExpiryDate());

      return repository.save(existing);
    }

    return null;
  }

  @Override
  public void deleteNotification(Long id) {
    repository.deleteById(id);
  }
}
