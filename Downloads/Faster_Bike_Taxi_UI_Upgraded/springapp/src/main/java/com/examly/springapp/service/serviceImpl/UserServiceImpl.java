// package com.examly.springapp.service.serviceImpl;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.examly.springapp.model.User;
// import com.examly.springapp.repository.UserRepository;
// import com.examly.springapp.service.UserService;

// @Service
// public class UserServiceImpl implements UserService {

//   @Autowired
//   private UserRepository userRepository;

//   @Override
//   public User saveUser(User user) {
//     return userRepository.save(user);
//   }

//   @Override
//   public List<User> getAllUsers() {
//     return userRepository.findAll();
//   }

//   @Override
//   public User getUserById(Long id) {
//     return userRepository.findById(id).orElse(null);
//   }

//   @Override
//   public User updateUser(Long id, User user) {

//     User existingUser = userRepository.findById(id).orElse(null);

//     if (existingUser != null) {
//       existingUser.setUsername(user.getUsername());
//       existingUser.setEmail(user.getEmail());
//       existingUser.setPasswordHash(user.getPasswordHash());
//       existingUser.setPhoneNumber(user.getPhoneNumber());
//       existingUser.setRole(user.getRole());
//       existingUser.setIsActive(user.getIsActive());
//       existingUser.setProfileComplete(user.getProfileComplete());

//       return userRepository.save(existingUser);
//     }

//     return null;
//   }

//   @Override
//   public void deleteUser(Long id) {
//     userRepository.deleteById(id);
//   }
// }
package com.examly.springapp.service.serviceImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Document;
import com.examly.springapp.model.DriverApplication;
import com.examly.springapp.model.Notification;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.DocumentRepository;
import com.examly.springapp.repository.DriverApplicationRepository;
import com.examly.springapp.repository.NotificationRepository;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverApplicationRepository applicationRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(Long id, User user) {

        User existingUser =
                userRepository.findById(id).orElse(null);

        if (existingUser != null) {

            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPasswordHash(user.getPasswordHash());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setRole(user.getRole());
            existingUser.setIsActive(user.getIsActive());
            existingUser.setProfileComplete(
                    user.getProfileComplete()
            );

            return userRepository.save(existingUser);
        }

        return null;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        /*
         * Admin is allowed to remove managers through the
         * Admin User Management page.
         */
        if (user.getRole() != User.Role.MANAGER) {
            throw new RuntimeException(
                    "Only managers can be removed"
            );
        }

        /*
         * Remove this manager as the assigned reviewer from
         * driver applications.
         *
         * The applications themselves are NOT deleted.
         */
        List<DriverApplication> applications =
                applicationRepository.findAll();

        for (DriverApplication application : applications) {

            if (application.getAssignedReviewer() != null
                    && application.getAssignedReviewer()
                        .getId()
                        .equals(user.getId())) {

                application.setAssignedReviewer(null);
                applicationRepository.save(application);
            }
        }

        /*
         * Remove this manager as document verifier.
         *
         * The documents themselves are NOT deleted.
         */
        List<Document> documents =
                documentRepository.findAll();

        for (Document document : documents) {

            if (document.getVerifiedBy() != null
                    && document.getVerifiedBy()
                        .getId()
                        .equals(user.getId())) {

                document.setVerifiedBy(null);
                documentRepository.save(document);
            }
        }

        /*
         * Delete notifications belonging to this manager.
         */
        List<Notification> notifications =
                notificationRepository
                    .findByUserUsernameOrderByCreatedDateDesc(
                        user.getUsername()
                    );

        if (!notifications.isEmpty()) {
            notificationRepository.deleteAll(notifications);
        }

        /*
         * Finally remove the manager from the users table.
         */
        userRepository.delete(user);
    }
}