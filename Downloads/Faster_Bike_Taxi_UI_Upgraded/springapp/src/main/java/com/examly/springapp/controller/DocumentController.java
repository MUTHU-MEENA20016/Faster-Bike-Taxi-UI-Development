package com.examly.springapp.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.examly.springapp.model.Document;
import com.examly.springapp.model.DriverApplication;
import com.examly.springapp.model.Notification;
import com.examly.springapp.model.User;

import com.examly.springapp.repository.DocumentRepository;
import com.examly.springapp.repository.DriverApplicationRepository;
import com.examly.springapp.repository.NotificationRepository;
import com.examly.springapp.repository.UserRepository;

import com.examly.springapp.service.DocumentService;
import com.examly.springapp.model.AuditLog;
import com.examly.springapp.repository.AuditLogRepository;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin("*")
public class DocumentController {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    private DocumentService service;

    @Autowired
    private DocumentRepository repository;

    @Autowired
    private DriverApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    DocumentController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /*
     * Management can see all submitted documents.
     */
    @GetMapping
    public ResponseEntity<?> getAllDocuments(
            Principal principal) {

        if (!isManagement(principal)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Management access required");
        }

        List<Document> documents =
                service.getAllDocuments();

        List<Map<String, Object>> result =
                documents.stream()
                        .map(document -> {

                            Map<String, Object> data =
                                    new HashMap<>();

                            data.put(
                                    "id",
                                    document.getId()
                            );

                            data.put(
                                    "documentType",
                                    document.getDocumentType()
                            );

                            data.put(
                                    "fileName",
                                    document.getFileName()
                            );

                            data.put(
                                    "fileSize",
                                    document.getFileSize()
                            );

                            data.put(
                                    "contentType",
                                    document.getContentType()
                            );

                            data.put(
                                    "uploadDate",
                                    document.getUploadDate()
                            );

                            data.put(
                                    "verificationStatus",
                                    document.getVerificationStatus()
                            );

                            DriverApplication application =
                                    document.getApplication();

                            if (application != null) {

                                data.put(
                                        "applicantName",
                                        application.getName()
                                );

                                data.put(
                                        "applicantPhone",
                                        application.getPhoneNumber()
                                );

                                data.put(
                                        "applicationId",
                                        application.getId()
                                );

                            } else {

                                data.put(
                                        "applicantName",
                                        "—"
                                );

                                data.put(
                                        "applicantPhone",
                                        "—"
                                );

                                data.put(
                                        "applicationId",
                                        null
                                );
                            }

                            return data;
                        })
                        .toList();

        return ResponseEntity.ok(result);
    }

    /*
     * Temporary document upload.
     *
     * Used while a driver is filling the application form.
     */
    @PostMapping(
            value = "/temp",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadTemp(
            @RequestPart("file") MultipartFile file,
            @RequestParam String documentType,
            @RequestParam String uploadToken,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        try {

            if (file == null || file.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Choose a file first");
            }

            if (file.getSize() >
                    10 * 1024 * 1024) {

                return ResponseEntity
                        .badRequest()
                        .body(
                            "Each document must be 10 MB or smaller"
                        );
            }

            Document.DocumentType type;

            try {

                type =
                        Document.DocumentType.valueOf(
                                documentType
                        );

            } catch (IllegalArgumentException e) {

                return ResponseEntity
                        .badRequest()
                        .body(
                            "Invalid document type"
                        );
            }

            /*
             * Drivers can upload only the four
             * required application documents.
             */
            if (!isManagement(principal)) {

                boolean allowed =
                        type ==
                            Document.DocumentType.DRIVING_LICENSE
                        ||
                        type ==
                            Document.DocumentType.VEHICLE_REGISTRATION
                        ||
                        type ==
                            Document.DocumentType.INSURANCE
                        ||
                        type ==
                            Document.DocumentType.POLLUTION_CERTIFICATE;

                if (!allowed) {

                    return ResponseEntity
                            .status(
                                HttpStatus.FORBIDDEN
                            )
                            .body(
                                "Document type is not allowed"
                            );
                }
            }

            /*
             * Replace temporary document of
             * the same type.
             */
            Document existing =
                    repository
                        .findByUploadTokenAndDocumentTypeAndApplicationIsNull(
                                uploadToken,
                                type
                        );

            if (existing != null) {
                repository.delete(existing);
            }

            Document document =
                    new Document();

            document.setDocumentType(type);

            document.setFileName(
                    file.getOriginalFilename()
            );

            document.setFileSize(
                    file.getSize()
            );

            document.setContentType(
                    file.getContentType()
            );

            document.setFileData(
                    file.getBytes()
            );

            document.setUploadDate(
                    LocalDateTime.now()
            );

            document.setVerificationStatus(
                    Document.VerificationStatus.PENDING
            );

            document.setUploadToken(
                    uploadToken
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                        repository.save(document)
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                        "Unable to upload document"
                    );
        }
    }

    /*
     * Driver replaces ONLY a rejected document.
     *
     * The other verified documents are untouched.
     */
    @PostMapping(
            value = "/{id}/replace",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> replaceRejectedDocument(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        if (file == null || file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Choose a file first");
        }

        if (file.getSize() >
                10 * 1024 * 1024) {

            return ResponseEntity
                    .badRequest()
                    .body(
                        "Each document must be 10 MB or smaller"
                    );
        }

        Document document =
                repository.findById(id)
                        .orElse(null);

        if (document == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        /*
         * Only the applicant who owns the
         * application can replace it.
         */
        if (isManagement(principal)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                        "Only the applicant can replace this document"
                    );
        }

        DriverApplication application =
                document.getApplication();

        if (application == null) {
            return ResponseEntity
                    .badRequest()
                    .body(
                        "Document is not attached to an application"
                    );
        }

        if (!principal.getName()
                .equalsIgnoreCase(
                    application.getApplicantUsername()
                )) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                        "You can only replace your own document"
                    );
        }

        if (document.getVerificationStatus() !=
                Document.VerificationStatus.REJECTED) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                        "Only a rejected document can be replaced"
                    );
        }

        try {

            /*
             * Keep the SAME document record.
             * Only replace its file data and status.
             */
            document.setFileName(
                    file.getOriginalFilename()
            );

            document.setFileSize(
                    file.getSize()
            );

            document.setContentType(
                    file.getContentType()
            );

            document.setFileData(
                    file.getBytes()
            );

            document.setUploadDate(
                    LocalDateTime.now()
            );

            document.setVerificationStatus(
                    Document.VerificationStatus.PENDING
            );

            document.setVerificationDate(null);

            repository.save(document);

            /*
             * Application goes back for document review.
             */
            application.setStatus(
                    DriverApplication.ApplicationStatus.SUBMITTED
            );

            application.setVerificationStage(
                    "DOCUMENT_VERIFICATION"
            );

            application.setRejectionReason(null);

            application.setLastModified(
                    LocalDateTime.now()
            );

            applicationRepository.save(application);

            /*
             * Notify managers that the application
             * is ready for review again.
             */
            createManagerApplicationNotification(
                    application
            );

            return ResponseEntity.ok(
                    "Rejected document replaced successfully"
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                        "Unable to replace document"
                    );
        }
    }

    /*
     * Driver sees only their own application documents.
     */
    @GetMapping("/application/{applicationId}")
    public ResponseEntity<?> getByApplication(
            @PathVariable Long applicationId,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        DriverApplication application =
                applicationRepository
                        .findById(applicationId)
                        .orElse(null);

        if (application == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        if (!isManagement(principal)
                &&
                !principal.getName()
                    .equalsIgnoreCase(
                        application.getApplicantUsername()
                    )) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                        "You can only view your own documents"
                    );
        }

        return ResponseEntity.ok(
                repository.findByApplicationId(
                        applicationId
                )
        );
    }

    /*
     * Download document.
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<?> download(
            @PathVariable Long id,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        Document document =
                repository.findById(id)
                        .orElse(null);

        if (document == null ||
                document.getFileData() == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        if (!isManagement(principal)) {

            if (document.getApplication() == null ||
                    !principal.getName()
                        .equalsIgnoreCase(
                            document.getApplication()
                                .getApplicantUsername()
                        )) {

                return ResponseEntity
                        .status(
                            HttpStatus.FORBIDDEN
                        )
                        .build();
            }
        }

        MediaType mediaType =
                MediaType.APPLICATION_OCTET_STREAM;

        try {

            if (document.getContentType() != null) {

                mediaType =
                        MediaType.parseMediaType(
                            document.getContentType()
                        );
            }

        } catch (Exception ignored) {
        }

        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" +
                    (
                        document.getFileName() == null
                        ? "document"
                        : document.getFileName()
                    ) +
                    "\""
                )
                .body(
                    document.getFileData()
                );
    }

    /*
     * Driver can delete temporary documents.
     */
    @DeleteMapping("/temp/{id}")
    public ResponseEntity<?> deleteTempDocument(
            @PathVariable Long id,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        Document document =
                repository.findById(id)
                        .orElse(null);

        if (document == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        if (document.getApplication() != null) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                        "Submitted documents cannot be removed here"
                    );
        }

        if (!isManagement(principal)) {

            if (document.getUploadToken() == null) {

                return ResponseEntity
                        .status(
                            HttpStatus.FORBIDDEN
                        )
                        .body(
                            "Document ownership could not be verified"
                        );
            }
        }

        repository.delete(document);

        return ResponseEntity.ok(
                "Temporary document removed"
        );
    }

    /*
     * Manager verifies or rejects a document.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam Document.VerificationStatus status,
            @RequestParam(
                required = false
            ) String rejectionReason,
            Principal principal) {

        if (!isManagement(principal)) {

            return ResponseEntity
                    .status(
                        HttpStatus.FORBIDDEN
                    )
                    .body(
                        "Management access required"
                    );
        }

        Document document =
                repository.findById(id)
                        .orElse(null);
        
        if (document == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        Document.VerificationStatus oldStatus =
        document.getVerificationStatus();
        

        /*
         * Rejection requires a reason.
         */
        if (status ==
                Document.VerificationStatus.REJECTED) {

            if (rejectionReason == null ||
                    rejectionReason.trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                            "Rejection reason is required"
                        );
            }
        }

        document.setVerificationStatus(
                status
        );

        document.setVerificationDate(
                status ==
                    Document.VerificationStatus.PENDING
                ? null
                : LocalDateTime.now()
        );

        Document savedDocument =
                repository.save(document);
        if (oldStatus != status) {

    AuditLog auditLog = new AuditLog();

    String managerName =
            principal.getName();

    DriverApplication application =
            document.getApplication();

    String applicantName =
            application != null
            ? application.getName()
            : "Unknown applicant";

    String action;

    if (status ==
            Document.VerificationStatus.VERIFIED) {

        action =
                applicantName
                + "'s document has been verified by Manager "
                + managerName;

    } else if (status ==
            Document.VerificationStatus.REJECTED) {

        action =
                applicantName
                + "'s document has been rejected by Manager "
                + managerName;

    } else {

        action =
                applicantName
                + "'s document status changed to "
                + status.name()
                + " by Manager "
                + managerName;
    }

    auditLog.setAction(action);

    /*
     * Keep the APPLICATION ID here,
     * because Admin wants to identify
     * the applicant/application.
     */
    auditLog.setEntityType(
            "DRIVER_APPLICATION"
    );

    auditLog.setEntityId(
            application != null
            ? application.getId()
            : null
    );

    auditLog.setOldValue(
            oldStatus == null
            ? null
            : oldStatus.name()
    );

    auditLog.setNewValue(
            applicantName
    );

    auditLog.setTimestamp(
            LocalDateTime.now()
    );

    auditLogRepository.save(
            auditLog
    );
}
        /*
         * If ONE document is rejected,
         * the entire application is rejected.
         */
        if (status ==
                Document.VerificationStatus.REJECTED) {

            DriverApplication application =
                    document.getApplication();

            if (application != null) {

                String documentName =
                        document.getDocumentType()
                                .name()
                                .replace(
                                    "_",
                                    " "
                                );

                String reason =
                        rejectionReason.trim();

                application.setStatus(
                        DriverApplication.ApplicationStatus.REJECTED
                );

                application.setVerificationStage(
                        "REJECTED"
                );

                application.setRejectionReason(
                        documentName +
                        " rejected: " +
                        reason
                );

                application.setLastModified(
                        LocalDateTime.now()
                );

                applicationRepository.save(
                        application
                );

                createDriverRejectionNotification(
                        application,
                        documentName,
                        reason
                );
            }
        }

        return ResponseEntity.ok(
                savedDocument
        );
    }

    /*
     * Management can create a document.
     */
    @PostMapping
    public ResponseEntity<?> saveDocument(
            @RequestBody Document document,
            Principal principal) {

        if (!isManagement(principal)) {

            return ResponseEntity
                    .status(
                        HttpStatus.FORBIDDEN
                    )
                    .body(
                        "Management access required"
                    );
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    service.saveDocument(document)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(
            @PathVariable Long id,
            Principal principal) {

        Document document =
                repository.findById(id)
                        .orElse(null);

        if (document == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        if (!isManagement(principal)) {

            if (document.getApplication() == null ||
                    !principal.getName()
                        .equalsIgnoreCase(
                            document.getApplication()
                                .getApplicantUsername()
                        )) {

                return ResponseEntity
                        .status(
                            HttpStatus.FORBIDDEN
                        )
                        .body(
                            "You can only view your own document"
                        );
            }
        }

        return ResponseEntity.ok(document);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDocument(
            @PathVariable Long id,
            @RequestBody Document document,
            Principal principal) {

        if (!isManagement(principal)) {

            return ResponseEntity
                    .status(
                        HttpStatus.FORBIDDEN
                    )
                    .body(
                        "Management access required"
                    );
        }

        return ResponseEntity.ok(
                service.updateDocument(
                    id,
                    document
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(
            @PathVariable Long id,
            Principal principal) {

        if (!isManagement(principal)) {

            return ResponseEntity
                    .status(
                        HttpStatus.FORBIDDEN
                    )
                    .body(
                        "Management access required"
                    );
        }

        service.deleteDocument(id);

        return ResponseEntity.ok(
                "Document deleted successfully"
        );
    }

    /*
     * Driver notification after document rejection.
     */
    private void createDriverRejectionNotification(
            DriverApplication application,
            String documentName,
            String reason) {

        if (application.getApplicantUsername() == null) {
            return;
        }

        User driver =
                userRepository
                    .findByUsername(
                        application
                            .getApplicantUsername()
                    )
                    .orElse(null);

        if (driver == null) {
            return;
        }

        Notification notification =
                new Notification();

        notification.setUser(driver);

        notification.setRelatedApplication(
                application
        );

        notification.setMessage(
                "Your " +
                documentName +
                " document has been rejected. Reason: " +
                reason
        );

        notification.setType(
                Notification.NotificationType.ERROR
        );

        notification.setPriority(
                Notification.NotificationPriority.HIGH
        );

        notification.setIsRead(false);

        notification.setCreatedDate(
                LocalDateTime.now()
        );

        notificationRepository.save(
                notification
        );
    }

    /*
     * One notification per manager.
     */
    private void createManagerApplicationNotification(
            DriverApplication application) {

        long count =
                applicationRepository.countByStatus(
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
                    .toList();

        for (User manager : managers) {

            List<Notification>
                    existingNotifications =
                        notificationRepository
                            .findByUserUsernameOrderByCreatedDateDesc(
                                manager.getUsername()
                            );

            Notification managerNotification =
                    null;

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

            managerNotification.setIsRead(false);

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

    private boolean isManagement(
            Principal principal) {

        if (!(principal instanceof
                org.springframework.security.core.Authentication)) {

            return false;
        }

        org.springframework.security.core.Authentication auth =
                (org.springframework.security.core.Authentication)
                        principal;

        return auth.getAuthorities()
                .stream()
                .anyMatch(
                    authority ->
                        authority.getAuthority()
                            .equals("ROLE_MANAGER")
                        ||
                        authority.getAuthority()
                            .equals("ROLE_ADMIN")
                );
    }
}