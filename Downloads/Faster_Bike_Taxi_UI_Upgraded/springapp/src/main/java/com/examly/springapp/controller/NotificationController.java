// package com.examly.springapp.controller;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import com.examly.springapp.model.Notification;
// import com.examly.springapp.service.NotificationService;

// @RestController
// @RequestMapping("/api/notifications")
// @CrossOrigin("*")
// public class NotificationController {

//   @Autowired
//   private NotificationService service;

//   @PostMapping
//   public Notification saveNotification(
//       @RequestBody Notification notification){
//     return service.saveNotification(notification);
//   }

//   @GetMapping
//   public List<Notification> getAllNotifications(){
//     return service.getAllNotifications();
//   }

//   @GetMapping("/{id}")
//   public Notification getNotificationById(
//       @PathVariable Long id){
//     return service.getNotificationById(id);
//   }

//   @PutMapping("/{id}")
//   public Notification updateNotification(
//       @PathVariable Long id,
//       @RequestBody Notification notification){
//     return service.updateNotification(id, notification);
//   }

//   @DeleteMapping("/{id}")
//   public void deleteNotification(@PathVariable Long id){
//     service.deleteNotification(id);
//   }
// }
package com.examly.springapp.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.Notification;
import com.examly.springapp.repository.NotificationRepository;
import com.examly.springapp.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @Autowired
    private NotificationRepository repository;

    @PostMapping
    public ResponseEntity<?> saveNotification(
            @RequestBody Notification notification) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveNotification(notification));
    }

    /*
     * IMPORTANT:
     * Never return all users' notifications.
     */
    @GetMapping
    public ResponseEntity<?> getMyNotifications(
            Principal principal) {

        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        List<Notification> notifications =
                repository.findByUserUsernameOrderByCreatedDateDesc(
                        principal.getName());

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(
            @PathVariable Long id,
            Principal principal) {

        Notification notification =
                repository.findById(id).orElse(null);

        if (notification == null) {
            return ResponseEntity.notFound().build();
        }

        if (principal == null ||
                notification.getUser() == null ||
                !notification.getUser()
                        .getUsername()
                        .equalsIgnoreCase(principal.getName())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You can only view your own notification");
        }

        return ResponseEntity.ok(notification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotification(
            @PathVariable Long id,
            @RequestBody Notification notification,
            Principal principal) {

        Notification existing =
                repository.findById(id).orElse(null);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (principal == null ||
                existing.getUser() == null ||
                !existing.getUser()
                        .getUsername()
                        .equalsIgnoreCase(principal.getName())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You can only update your own notification");
        }

        existing.setIsRead(notification.getIsRead());

        return ResponseEntity.ok(
                repository.save(existing)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(
            @PathVariable Long id,
            Principal principal) {

        Notification existing =
                repository.findById(id).orElse(null);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (principal == null ||
                existing.getUser() == null ||
                !existing.getUser()
                        .getUsername()
                        .equalsIgnoreCase(principal.getName())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You can only delete your own notification");
        }

        repository.delete(existing);

        return ResponseEntity.ok(
                "Notification deleted successfully"
        );
    }
}