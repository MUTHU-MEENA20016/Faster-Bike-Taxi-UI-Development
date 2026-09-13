package com.examly.springapp.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.Document;
import com.examly.springapp.model.DriverApplication;
import com.examly.springapp.model.Notification;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.DocumentRepository;
import com.examly.springapp.repository.DriverApplicationRepository;
import com.examly.springapp.repository.NotificationRepository;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.DriverApplicationService;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin("*")
public class DriverApplicationController {

    @Autowired
    private DriverApplicationService service;

    @Autowired
    private DriverApplicationRepository repository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    /*
     * Management endpoint.
     */
    @GetMapping
    public ResponseEntity<?> getAllApplications(
            Authentication authentication) {

        if (!hasManagement(authentication)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Management access required");
        }

        return ResponseEntity.ok(
                service.getAllApplications()
        );
    }

    /*
     * Driver sees only their own applications.
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyApplications(
            Principal principal) {

        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        return ResponseEntity.ok(
                repository.findByApplicantUsernameIgnoreCase(
                        principal.getName()
                )
        );
    }

    /*
     * Submit application.
     */
    @PostMapping(
        value = "/submit",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Transactional
    public ResponseEntity<?> submit(
            @RequestParam String name,
            @RequestParam String bikeNumber,
            @RequestParam Integer age,
            @RequestParam String address,
            @RequestParam String emergencyContact,
            @RequestParam String drivingLicenseNumber,
            @RequestParam(required = false) String username,
            @RequestParam String uploadToken,
            Principal principal) {

        /*
         * IMPORTANT:
         * Never trust username or phone number sent by React.
         *
         * The authenticated JWT principal is the owner.
         */
        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        String owner = principal.getName();

        /*
         * Only DRIVER should submit a driver application.
         */
        if (!(principal instanceof Authentication)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Driver access required");
        }

        Authentication authentication =
                (Authentication) principal;

        boolean driver =
                authentication.getAuthorities()
                    .stream()
                    .anyMatch(a ->
                        a.getAuthority()
                            .equals("ROLE_DRIVER"));

        if (!driver) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Only drivers can submit applications");
        }

        /*
         * Get the registered user from the database.
         *
         * The phone number is taken from the User account
         * created during registration.
         */
        User applicant =
                userRepository
                    .findByUsername(owner)
                    .orElse(null);

        if (applicant == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("User account not found");
        }

        String phone = applicant.getPhoneNumber();

        if (phone == null || phone.trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(
                        "Phone number is not registered for this account"
                    );
        }

        phone = phone.trim();

        /*
         * One application per logged-in driver.
         */
        if (!repository
                .findByApplicantUsernameIgnoreCase(owner)
                .isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("You already have an application.");
        }

        String bike =
                bikeNumber.trim().toUpperCase();

        String license =
                drivingLicenseNumber.trim().toUpperCase();

        /*
         * Bike number must be unique.
         */
        if (repository.findByBikeNumber(bike) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Bike number already exists in system");
        }

        /*
         * Registered phone number must not already belong
         * to another application.
         */
        if (repository.findByPhoneNumber(phone) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                        "Phone number is already used by another application"
                    );
        }

        /*
         * Driving license must be unique.
         */
        if (repository
                .findByDrivingLicenseNumber(license) != null) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                        "Driving license number already exists in system"
                    );
        }

        if (age == null || age < 18 || age > 65) {
            return ResponseEntity
                    .badRequest()
                    .body("Age must be between 18 and 65");
        }

        if (name == null || name.trim().isEmpty() ||
            address == null || address.trim().isEmpty() ||
            emergencyContact == null ||
            emergencyContact.trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("All required application fields must be provided");
        }

        /*
         * Get only temporary documents belonging to
         * this upload token.
         */
        List<Document> tempDocs =
                documentRepository.findByUploadTokenAndApplicationIsNull(
                        uploadToken
                );

        /*
         * Exactly four required documents.
         */
        if (tempDocs.size() != 4) {
            return ResponseEntity
                    .badRequest()
                    .body(
                        "Upload all four required documents before final submission"
                    );
        }

        try {

            DriverApplication application =
                    new DriverApplication();

            application.setName(name.trim());
            application.setBikeNumber(bike);
            application.setAge(age);

            /*
             * IMPORTANT:
             * Phone number comes from the registered User.
             * It does NOT come from the Apply Form.
             */
            application.setPhoneNumber(phone);

            application.setAddress(address.trim());

            application.setEmergencyContact(
                    emergencyContact.trim()
            );

            application.setDrivingLicenseNumber(license);

            /*
             * Owner comes from JWT.
             */
            application.setApplicantUsername(owner);

            application.setStatus(
                    DriverApplication.ApplicationStatus.SUBMITTED
            );

            application.setVerificationStage(
                    "DOCUMENT_VERIFICATION"
            );

            application.setSubmissionDate(
                    LocalDateTime.now()
            );

            application.setLastModified(
                    LocalDateTime.now()
            );

            application = repository.save(application);
            createManagerApplicationNotification(application);
            /*
             * Attach the four temporary documents
             * to the submitted application.
             */
            for (Document document : tempDocs) {

                document.setApplication(application);

                document.setUploadToken(null);

                documentRepository.save(document);
            }

            /*
             * Notify the applicant.
             */
            Notification notification =
                    new Notification();

            notification.setUser(applicant);
            notification.setRelatedApplication(
                    application
            );

            notification.setMessage(
                "Your application has been submitted successfully."
            );

            notification.setType(
                Notification.NotificationType.SUCCESS
            );

            notification.setPriority(
                Notification.NotificationPriority.MEDIUM
            );

            notification.setIsRead(false);

            notification.setCreatedDate(
                LocalDateTime.now()
            );

            notificationRepository.save(
                    notification
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(application);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                        "Unable to store application and documents"
                    );
        }
    }

    /*
     * Management updates application status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(
            @PathVariable Long id,
            @RequestBody DriverApplication request,
            Authentication authentication) {

        if (!hasManagement(authentication)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Management access required");
        }

        DriverApplication application =
                repository.findById(id).orElse(null);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        DriverApplication.ApplicationStatus oldStatus =
                application.getStatus();

        DriverApplication.ApplicationStatus newStatus =
                request.getStatus();

        if (newStatus == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Application status is required");
        }

        application.setStatus(newStatus);
        application.setLastModified(
                LocalDateTime.now()
        );

        if (newStatus ==
                DriverApplication.ApplicationStatus.DOCUMENT_VERIFICATION) {

            application.setVerificationStage(
                    "DOCUMENT_VERIFICATION"
            );
        }

        if (newStatus ==
                DriverApplication.ApplicationStatus.APPROVED) {

            application.setVerificationStage(
                    "COMPLETED"
            );

            application.setRejectionReason(null);
        }

        if (newStatus ==
                DriverApplication.ApplicationStatus.REJECTED) {

            String reason =
                    request.getRejectionReason();

            if (reason == null ||
                    reason.trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                            "Rejection reason is required"
                        );
            }

            application.setRejectionReason(
                    reason.trim()
            );

            application.setVerificationStage(
                    "REJECTED"
            );
        }

        application =
                repository.save(application);

        /*
         * Send notification only when the status
         * actually changes.
         */
        if (oldStatus != newStatus) {
            createStatusNotification(
                    application,
                    newStatus
            );
        }

        return ResponseEntity.ok(application);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicationById(
            @PathVariable Long id,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        DriverApplication application =
                repository.findById(id).orElse(null);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        /*
         * Management can view any application.
         */
        if (isManagement(principal)) {
            return ResponseEntity.ok(application);
        }

        /*
         * Driver can view only their own application.
         */
        if (!principal.getName()
                .equalsIgnoreCase(
                    application.getApplicantUsername()
                )) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                        "You can only view your own application"
                    );
        }

        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(
            @PathVariable Long id,
            Authentication authentication) {

        if (!hasManagement(authentication)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Management access required");
        }

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        service.deleteApplication(id);

        return ResponseEntity.ok(
                "Application deleted successfully"
        );
    }
    private void createManagerApplicationNotification(
        DriverApplication application) {

    /*
     * Count all applications that are waiting for management review.
     */
    long count = repository.countByStatus(
            DriverApplication.ApplicationStatus.SUBMITTED
    );

    /*
     * Create ONE notification for each manager.
     */
    List<User> managers = userRepository.findAll()
            .stream()
            .filter(user -> user.getRole() == User.Role.MANAGER)
            .collect(java.util.stream.Collectors.toList());

    for (User manager : managers) {

        /*
         * Check whether a manager notification already exists.
         * If it exists, update it instead of creating another one.
         */
        List<Notification> existingNotifications =
                notificationRepository
                    .findByUserUsernameOrderByCreatedDateDesc(
                            manager.getUsername()
                    );

        Notification managerNotification = null;

        for (Notification n : existingNotifications) {
            if (n.getMessage() != null &&
                n.getMessage().contains("application") &&
                n.getMessage().contains("to review")) {

                managerNotification = n;
                break;
            }
        }

        if (managerNotification == null) {
            managerNotification = new Notification();
            managerNotification.setUser(manager);
            managerNotification.setIsRead(false);
            managerNotification.setType(
                    Notification.NotificationType.INFO
            );
            managerNotification.setPriority(
                    Notification.NotificationPriority.HIGH
            );
        }

        /*
         * Update the SAME notification with the latest count.
         */
        if (count == 1) {
            managerNotification.setMessage(
                    "1 application to review."
            );
        } else {
            managerNotification.setMessage(
                    count + " applications to review."
            );
        }

        managerNotification.setCreatedDate(
                LocalDateTime.now()
        );

        /*
         * Keep the latest submitted application related
         * to this notification.
         */
        managerNotification.setRelatedApplication(application);

        notificationRepository.save(managerNotification);
    }
}
    private void createStatusNotification(
            DriverApplication application,
            DriverApplication.ApplicationStatus status) {

        if (application.getApplicantUsername() == null) {
            return;
        }

        User applicant =
                userRepository
                    .findByUsername(
                        application.getApplicantUsername()
                    )
                    .orElse(null);

        if (applicant == null) {
            return;
        }

        Notification notification =
                new Notification();

        notification.setUser(applicant);
        notification.setRelatedApplication(application);
        notification.setIsRead(false);
        notification.setCreatedDate(LocalDateTime.now());
        notification.setPriority(
                Notification.NotificationPriority.HIGH
        );

        if (status ==
                DriverApplication.ApplicationStatus.APPROVED) {

            notification.setType(
                    Notification.NotificationType.SUCCESS
            );

            notification.setMessage(
                "Your application is approved."
            );

        } else if (status ==
                DriverApplication.ApplicationStatus.REJECTED) {

            notification.setType(
                    Notification.NotificationType.ERROR
            );

            notification.setMessage(
                "Your application is rejected. Reason: "
                + application.getRejectionReason()
                + ". Please correct the required "
                + "information/documents and submit again."
            );

        } else if (status ==
                DriverApplication.ApplicationStatus.DOCUMENT_VERIFICATION) {

            notification.setType(
                    Notification.NotificationType.INFO
            );

            notification.setPriority(
                    Notification.NotificationPriority.MEDIUM
            );

            notification.setMessage(
                "Your application has moved to document verification."
            );

        } else {
            notification.setType(
                    Notification.NotificationType.INFO
            );

            notification.setMessage(
                "Your application status has been updated to "
                + status.name().replace("_", " ") + "."
            );
        }

        notificationRepository.save(notification);
    }

    private boolean hasManagement(
            Authentication authentication) {

        return authentication != null &&
                authentication.getAuthorities()
                    .stream()
                    .anyMatch(a ->
                        a.getAuthority()
                            .equals("ROLE_MANAGER") ||
                        a.getAuthority()
                            .equals("ROLE_ADMIN"));
    }

    private boolean isManagement(
            Principal principal) {

        if (!(principal instanceof Authentication)) {
            return false;
        }

        Authentication authentication =
                (Authentication) principal;

        return hasManagement(authentication);
    }
}