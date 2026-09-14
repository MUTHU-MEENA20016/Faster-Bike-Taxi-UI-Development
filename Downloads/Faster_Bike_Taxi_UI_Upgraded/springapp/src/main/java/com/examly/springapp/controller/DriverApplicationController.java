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
import com.examly.springapp.model.AuditLog;
import com.examly.springapp.repository.AuditLogRepository;
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

    @Autowired
private AuditLogRepository auditLogRepository;
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
         * The authenticated JWT user is the owner.
         */
        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        String owner = principal.getName();

        /*
         * Only DRIVER can submit an application.
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
                    .body(
                        "Only drivers can submit applications"
                    );
        }

        /*
         * Get the registered user.
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

        /*
         * Phone number comes from registration.
         */
        String phone =
                applicant.getPhoneNumber();

        if (phone == null ||
                phone.trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Phone number is not registered for this account"
                    );
        }

        phone = phone.trim();

        /*
         * One application per driver.
         */
        if (!repository
                .findByApplicantUsernameIgnoreCase(owner)
                .isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                        "You already have an application."
                    );
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
                    .body(
                        "Bike number already exists in system"
                    );
        }

        /*
         * Registered phone number must be unique
         * across applications.
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

        /*
         * Age validation.
         */
        if (age == null ||
                age < 18 ||
                age > 65) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Age must be between 18 and 65"
                    );
        }

        /*
         * Required fields.
         */
        if (name == null ||
                name.trim().isEmpty() ||
                address == null ||
                address.trim().isEmpty() ||
                emergencyContact == null ||
                emergencyContact.trim().isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "All required application fields must be provided"
                    );
        }

        /*
         * Get temporary documents belonging
         * to this upload token.
         */
        List<Document> tempDocs =
                documentRepository
                    .findByUploadTokenAndApplicationIsNull(
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

            application.setName(
                    name.trim()
            );

            application.setBikeNumber(
                    bike
            );

            application.setAge(
                    age
            );

            /*
             * Phone comes from registered User.
             */
            application.setPhoneNumber(
                    phone
            );

            application.setAddress(
                    address.trim()
            );

            application.setEmergencyContact(
                    emergencyContact.trim()
            );

            application.setDrivingLicenseNumber(
                    license
            );

            /*
             * Owner comes from JWT.
             */
            application.setApplicantUsername(
                    owner
            );

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

            application =
                    repository.save(application);
           AuditLog auditLog = new AuditLog();

auditLog.setAction(
        application.getName()
        + " has submitted the application"
);

auditLog.setEntityType(
        "DRIVER_APPLICATION"
);

auditLog.setEntityId(
        application.getId()
);

auditLog.setOldValue(null);

auditLog.setNewValue(
        application.getName()
);

auditLog.setTimestamp(
        LocalDateTime.now()
);

auditLogRepository.save(
        auditLog
);
            /*
             * Attach temporary documents
             * to this application.
             */
            for (Document document : tempDocs) {

                document.setApplication(
                        application
                );

                document.setUploadToken(
                        null
                );

                documentRepository.save(
                        document
                );
            }

            /*
             * Notify managers.
             */
            createManagerApplicationNotification(
                    application
            );

            /*
             * Notify applicant.
             */
            Notification notification =
                    new Notification();

            notification.setUser(
                    applicant
            );

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

            notification.setIsRead(
                    false
            );

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
                    .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                    )
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
                    .body(
                        "Management access required"
                    );
        }

        DriverApplication application =
                repository
                    .findById(id)
                    .orElse(null);

        if (application == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        DriverApplication.ApplicationStatus oldStatus =
                application.getStatus();

        DriverApplication.ApplicationStatus newStatus =
                request.getStatus();

        if (newStatus == null) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Application status is required"
                    );
        }

        /*
         * APPROVAL RULE:
         *
         * Manager cannot approve an application
         * until all four documents are verified.
         */
        if (newStatus ==
                DriverApplication.ApplicationStatus.APPROVED) {

            List<Document> documents =
                    documentRepository
                        .findByApplicationId(id);

            /*
             * Exactly four documents are required.
             */
            if (documents.size() != 4) {

                return ResponseEntity
                        .badRequest()
                        .body(
                            "All four documents must be submitted before approval"
                        );
            }

            /*
             * Every document must be VERIFIED.
             */
            boolean allVerified =
                    documents.stream()
                        .allMatch(
                            document ->
                                document.getVerificationStatus()
                                    ==
                                Document.VerificationStatus.VERIFIED
                        );

            if (!allVerified) {

                return ResponseEntity
                        .badRequest()
                        .body(
                            "All four documents must be verified before approval"
                        );
            }

            application.setStatus(
                    DriverApplication.ApplicationStatus.APPROVED
            );

            application.setVerificationStage(
                    "COMPLETED"
            );

            application.setRejectionReason(
                    null
            );
        }

        /*
         * Move application to document verification.
         */
        else if (newStatus ==
                DriverApplication.ApplicationStatus.DOCUMENT_VERIFICATION) {

            application.setStatus(
                    DriverApplication.ApplicationStatus.DOCUMENT_VERIFICATION
            );

            application.setVerificationStage(
                    "DOCUMENT_VERIFICATION"
            );
        }

        /*
         * Reject application.
         */
        else if (newStatus ==
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

            application.setStatus(
                    DriverApplication.ApplicationStatus.REJECTED
            );

            application.setRejectionReason(
                    reason.trim()
            );

            application.setVerificationStage(
                    "REJECTED"
            );
        }

        /*
         * Other statuses.
         */
        else {

            application.setStatus(
                    newStatus
            );
        }

        application.setLastModified(
                LocalDateTime.now()
        );

        application =
                repository.save(application);
       if (oldStatus != newStatus) {

    AuditLog auditLog = new AuditLog();

    String managerName =
            authentication.getName();

    String action;

    if (newStatus ==
            DriverApplication.ApplicationStatus.APPROVED) {

        action =
                application.getName()
                + "'s application has been approved by Manager "
                + managerName;

    } else if (newStatus ==
            DriverApplication.ApplicationStatus.REJECTED) {

        action =
                application.getName()
                + "'s application has been rejected by Manager "
                + managerName;

    } else {

        action =
                application.getName()
                + "'s application status changed to "
                + newStatus.name().replace("_", " ")
                + " by Manager "
                + managerName;
    }

    auditLog.setAction(action);

    auditLog.setEntityType(
            "DRIVER_APPLICATION"
    );

    auditLog.setEntityId(
            application.getId()
    );

    auditLog.setOldValue(
            oldStatus == null
            ? null
            : oldStatus.name()
    );

    auditLog.setNewValue(
            application.getName()
    );

    auditLog.setTimestamp(
            LocalDateTime.now()
    );

    auditLogRepository.save(
            auditLog
    );
}
        /*
         * Notify applicant only when
         * the status actually changes.
         */
        if (oldStatus != newStatus) {

            createStatusNotification(
                    application,
                    newStatus
            );
        }

        return ResponseEntity.ok(
                application
        );
    }

    /*
     * Get one application.
     */
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
                repository
                    .findById(id)
                    .orElse(null);

        if (application == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        /*
         * Manager/Admin can view any application.
         */
        if (isManagement(principal)) {
            return ResponseEntity.ok(
                    application
            );
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

        return ResponseEntity.ok(
                application
        );
    }

    /*
     * Delete application.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(
            @PathVariable Long id,
            Authentication authentication) {

        if (!hasManagement(authentication)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                        "Management access required"
                    );
        }

        if (!repository.existsById(id)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        service.deleteApplication(id);

        return ResponseEntity.ok(
                "Application deleted successfully"
        );
    }

    /*
     * Creates ONE review notification for each manager.
     */
    private void createManagerApplicationNotification(
            DriverApplication application) {

        long count =
                repository.countByStatus(
                    DriverApplication.ApplicationStatus.SUBMITTED
                );

        List<User> managers =
                userRepository.findAll()
                    .stream()
                    .filter(
                        user ->
                            user.getRole()
                                == User.Role.MANAGER
                    )
                    .collect(
                        java.util.stream.Collectors.toList()
                    );

        for (User manager : managers) {

            List<Notification>
                    existingNotifications =
                        notificationRepository
                            .findByUserUsernameOrderByCreatedDateDesc(
                                manager.getUsername()
                            );

            Notification managerNotification =
                    null;

            /*
             * Find the existing application-review
             * notification instead of creating
             * a new one every time.
             */
            for (
                Notification notification :
                    existingNotifications
            ) {

                if (
                    notification.getMessage() != null
                    &&
                    notification
                        .getMessage()
                        .contains(
                            "application"
                        )
                    &&
                    notification
                        .getMessage()
                        .contains(
                            "to review"
                        )
                ) {

                    managerNotification =
                            notification;

                    break;
                }
            }

            if (managerNotification == null) {

                managerNotification =
                        new Notification();

                managerNotification.setUser(
                        manager
                );

                managerNotification.setType(
                    Notification.NotificationType.INFO
                );

                managerNotification.setPriority(
                    Notification.NotificationPriority.HIGH
                );
            }

            /*
             * Notification becomes unread again
             * whenever the count changes.
             */
            managerNotification.setIsRead(
                    false
            );

            if (count == 1) {

                managerNotification.setMessage(
                    "1 application to review."
                );

            } else {

                managerNotification.setMessage(
                    count +
                    " applications to review."
                );
            }

            managerNotification.setCreatedDate(
                    LocalDateTime.now()
            );

            managerNotification.setRelatedApplication(
                    application
            );

            notificationRepository.save(
                    managerNotification
            );
        }
    }

    /*
     * Creates applicant notification
     * when application status changes.
     */
    private void createStatusNotification(
            DriverApplication application,
            DriverApplication.ApplicationStatus status) {

        if (application.getApplicantUsername() == null) {
            return;
        }

        User applicant =
                userRepository
                    .findByUsername(
                        application
                            .getApplicantUsername()
                    )
                    .orElse(null);

        if (applicant == null) {
            return;
        }

        Notification notification =
                new Notification();

        notification.setUser(
                applicant
        );

        notification.setRelatedApplication(
                application
        );

        notification.setIsRead(
                false
        );

        notification.setCreatedDate(
                LocalDateTime.now()
        );

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
        }

        else if (status ==
                DriverApplication.ApplicationStatus.REJECTED) {

            notification.setType(
                    Notification.NotificationType.ERROR
            );

            notification.setMessage(
                "Your application is rejected. Reason: "
                + application.getRejectionReason()
                + ". Please correct the required "
                + "information/documents."
            );
        }

        else if (status ==
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
        }

        else {

            notification.setType(
                    Notification.NotificationType.INFO
            );

            notification.setMessage(
                "Your application status has been updated to "
                + status.name().replace("_", " ")
                + "."
            );
        }

        notificationRepository.save(
                notification
        );
    }

    /*
     * Checks Manager/Admin access.
     */
    private boolean hasManagement(
            Authentication authentication) {

        return authentication != null
                &&
                authentication
                    .getAuthorities()
                    .stream()
                    .anyMatch(
                        authority ->
                            authority
                                .getAuthority()
                                .equals("ROLE_MANAGER")
                            ||
                            authority
                                .getAuthority()
                                .equals("ROLE_ADMIN")
                    );
    }

    /*
     * Checks Manager/Admin access from Principal.
     */
    private boolean isManagement(
            Principal principal) {

        if (!(principal instanceof Authentication)) {
            return false;
        }

        Authentication authentication =
                (Authentication) principal;

        return hasManagement(
                authentication
        );
    }
}