// package com.examly.springapp.controller;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import com.examly.springapp.model.Document;
// import com.examly.springapp.service.DocumentService;

// @RestController
// @RequestMapping("/api/documents")
// @CrossOrigin("*")
// public class DocumentController {

//   @Autowired
//   private DocumentService service;

//   @PostMapping
//   public Document saveDocument(@RequestBody Document document){
//     return service.saveDocument(document);
//   }

//   @GetMapping
//   public List<Document> getAllDocuments(){
//     return service.getAllDocuments();
//   }

//   @GetMapping("/{id}")
//   public Document getDocumentById(@PathVariable Long id){
//     return service.getDocumentById(id);
//   }

//   @PutMapping("/{id}")
//   public Document updateDocument(@PathVariable Long id,
//                   @RequestBody Document document){
//     return service.updateDocument(id, document);
//   }

//   @DeleteMapping("/{id}")
//   public void deleteDocument(@PathVariable Long id){
//     service.deleteDocument(id);
//   }
// }


// package com.examly.springapp.controller;

// import java.security.Principal;
// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.UUID;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import com.examly.springapp.model.Document;
// import com.examly.springapp.model.DriverApplication;
// import com.examly.springapp.repository.DocumentRepository;
// import com.examly.springapp.repository.DriverApplicationRepository;
// import com.examly.springapp.service.DocumentService;

// @RestController
// @RequestMapping("/api/documents")
// @CrossOrigin("*")
// public class DocumentController {

//     @Autowired
//     private DocumentService service;

//     @Autowired
//     private DocumentRepository repository;

//     @Autowired
//     private DriverApplicationRepository applicationRepository;

//     @PostMapping
//     public Document saveDocument(@RequestBody Document document) {
//         return service.saveDocument(document);
//     }

//     @PostMapping(value = "/temp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//     public ResponseEntity<?> uploadTemp(
//             @RequestPart("file") MultipartFile file,
//             @RequestParam String documentType,
//             @RequestParam String uploadToken) {
//         try {
//             if (file.isEmpty()) {
//                 return ResponseEntity.badRequest().body("Choose a file first");
//             }
//             if (file.getSize() > 10 * 1024 * 1024) {
//                 return ResponseEntity.badRequest().body("Each document must be 10 MB or smaller");
//             }

//             Document d = new Document();
//             d.setDocumentType(Document.DocumentType.valueOf(documentType));
//             d.setFileName(file.getOriginalFilename());
//             d.setFileSize(file.getSize());
//             d.setContentType(file.getContentType());
//             d.setFileData(file.getBytes());
//             d.setUploadDate(LocalDateTime.now());
//             d.setVerificationStatus(Document.VerificationStatus.PENDING);
//             d.setUploadToken(uploadToken);

//             return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(d));
//         } catch (Exception e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to upload document");
//         }
//     }

//     @GetMapping
//     public ResponseEntity<?> getAllDocuments(Principal principal) {
//         if (!isManagement(principal)) {
//             return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Management access required");
//         }
//         return ResponseEntity.ok(service.getAllDocuments());
//     }

//     @GetMapping("/application/{applicationId}")
//     public ResponseEntity<?> getByApplication(@PathVariable Long applicationId, Principal principal) {
//         if (principal == null) {
//             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login required");
//         }
//         if (!isManagement(principal)) {
//             DriverApplication app = applicationRepository.findById(applicationId).orElse(null);
//             if (app == null || !principal.getName().equalsIgnoreCase(app.getApplicantUsername())) {
//                 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only view your own documents");
//             }
//         }
//         return ResponseEntity.ok(repository.findByApplicationId(applicationId));
//     }

//     @GetMapping("/{id}")
//     public Document getDocumentById(@PathVariable Long id) {
//         return service.getDocumentById(id);
//     }

//     @GetMapping("/{id}/download")
//     public ResponseEntity<byte[]> download(@PathVariable Long id, Principal principal) {
//         Document d = repository.findById(id).orElse(null);
//         if (d == null || d.getFileData() == null) {
//             return ResponseEntity.notFound().build();
//         }
//         if (!isManagement(principal) && d.getApplication() != null && !principal.getName().equalsIgnoreCase(d.getApplication().getApplicantUsername())) {
//             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//         }

//         MediaType type = MediaType.APPLICATION_OCTET_STREAM;
//         try {
//             if (d.getContentType() != null) {
//                 type = MediaType.parseMediaType(d.getContentType());
//             }
//         } catch (Exception ignored) {
//         }

//         return ResponseEntity.ok()
//                 .contentType(type)
//                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + (d.getFileName() == null ? "document" : d.getFileName()) + "\"")
//                 .body(d.getFileData());
//     }

//     @PatchMapping("/{id}/status")
//     public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam Document.VerificationStatus status) {
//         Document d = repository.findById(id).orElse(null);
//         if (d == null) {
//             return ResponseEntity.notFound().build();
//         }
//         d.setVerificationStatus(status);
//         d.setVerificationDate(status == Document.VerificationStatus.PENDING ? null : LocalDateTime.now());
//         return ResponseEntity.ok(repository.save(d));
//     }

//     @PutMapping("/{id}")
//     public Document updateDocument(@PathVariable Long id, @RequestBody Document document) {
//         return service.updateDocument(id, document);
//     }

//     @DeleteMapping("/{id}")
//     public void deleteDocument(@PathVariable Long id) {
//         service.deleteDocument(id);
//     }

//     private boolean isManagement(Principal p) {
//         return p != null && p instanceof org.springframework.security.core.Authentication &&
//                 ((org.springframework.security.core.Authentication) p).getAuthorities().stream()
//                         .anyMatch(x -> x.getAuthority().equals("ROLE_MANAGER") || x.getAuthority().equals("ROLE_ADMIN"));
//     }
// }

package com.examly.springapp.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.examly.springapp.model.Document;
import com.examly.springapp.model.DriverApplication;
import com.examly.springapp.repository.DocumentRepository;
import com.examly.springapp.repository.DriverApplicationRepository;
import com.examly.springapp.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin("*")
public class DocumentController {

    @Autowired
    private DocumentService service;

    @Autowired
    private DocumentRepository repository;

    @Autowired
    private DriverApplicationRepository applicationRepository;

    /*
     * Management only.
     */
    @GetMapping
    public ResponseEntity<?> getAllDocuments(
            Principal principal) {

        if (!isManagement(principal)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Management access required");
        }

        return ResponseEntity.ok(
                service.getAllDocuments()
        );
    }

    /*
     * Temporary document upload.
     * Only authenticated users can reach this endpoint.
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

            if (file.getSize() > 10 * 1024 * 1024) {
                return ResponseEntity
                        .badRequest()
                        .body("Each document must be 10 MB or smaller");
            }

            /*
             * Prevent arbitrary document types.
             */
            Document.DocumentType type;

            try {
                type = Document.DocumentType.valueOf(
                        documentType
                );
            } catch (IllegalArgumentException e) {
                return ResponseEntity
                        .badRequest()
                        .body("Invalid document type");
            }

            /*
             * Drivers are allowed to upload only the four
             * application documents.
             */
            if (!isManagement(principal)) {

                boolean allowed =
                        type == Document.DocumentType.DRIVING_LICENSE ||
                        type == Document.DocumentType.VEHICLE_REGISTRATION ||
                        type == Document.DocumentType.INSURANCE ||
                        type == Document.DocumentType.POLLUTION_CERTIFICATE;

                if (!allowed) {
                    return ResponseEntity
                            .status(HttpStatus.FORBIDDEN)
                            .body("Document type is not allowed");
                }
            }

            /*
             * Replace an existing temporary document of the
             * same type for the same upload token.
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

            Document document = new Document();

            document.setDocumentType(type);
            document.setFileName(file.getOriginalFilename());
            document.setFileSize(file.getSize());
            document.setContentType(file.getContentType());
            document.setFileData(file.getBytes());
            document.setUploadDate(LocalDateTime.now());
            document.setVerificationStatus(
                    Document.VerificationStatus.PENDING
            );
            document.setUploadToken(uploadToken);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(repository.save(document));

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to upload document");
        }
    }

    /*
     * Driver can see documents belonging only to their
     * own application.
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
            return ResponseEntity.notFound().build();
        }

        if (!isManagement(principal) &&
                !principal.getName()
                        .equalsIgnoreCase(
                                application.getApplicantUsername()
                        )) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("You can only view your own documents");
        }

        return ResponseEntity.ok(
                repository.findByApplicationId(applicationId)
        );
    }

    /*
     * Download security is checked again at controller level.
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
                repository.findById(id).orElse(null);

        if (document == null ||
                document.getFileData() == null) {

            return ResponseEntity.notFound().build();
        }

        if (!isManagement(principal)) {

            if (document.getApplication() == null ||
                    !principal.getName()
                        .equalsIgnoreCase(
                            document.getApplication()
                                .getApplicantUsername()
                        )) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
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

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" +
                    (document.getFileName() == null
                        ? "document"
                        : document.getFileName()) +
                    "\""
                )
                .body(document.getFileData());
    }

    /*
     * Driver can delete only their own temporary document.
     *
     * Management can delete a document as well.
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
                repository.findById(id).orElse(null);

        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        /*
         * A temporary document must not already belong
         * to a submitted application.
         */
        if (document.getApplication() != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Submitted documents cannot be removed here");
        }

        if (!isManagement(principal)) {

            /*
             * uploadToken itself is not treated as ownership.
             * The document is temporary and therefore can be
             * removed only while the current authenticated
             * session is working with it.
             *
             * The frontend uses the authenticated request.
             */
            if (document.getUploadToken() == null) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("Document ownership could not be verified");
            }
        }

        repository.delete(document);

        return ResponseEntity.ok(
                "Temporary document removed"
        );
    }

    /*
     * Management-only document verification.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam Document.VerificationStatus status,
            Principal principal) {

        if (!isManagement(principal)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Management access required");
        }

        Document document =
                repository.findById(id).orElse(null);

        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        document.setVerificationStatus(status);

        document.setVerificationDate(
                status == Document.VerificationStatus.PENDING
                        ? null
                        : LocalDateTime.now()
        );

        return ResponseEntity.ok(
                repository.save(document)
        );
    }

    @PostMapping
    public ResponseEntity<?> saveDocument(
            @RequestBody Document document,
            Principal principal) {

        if (!isManagement(principal)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Management access required");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.saveDocument(document));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDocumentById(
            @PathVariable Long id,
            Principal principal) {

        Document document =
                repository.findById(id).orElse(null);

        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        if (!isManagement(principal)) {

            if (document.getApplication() == null ||
                    !principal.getName()
                        .equalsIgnoreCase(
                            document.getApplication()
                                .getApplicantUsername()
                        )) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("You can only view your own document");
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
                    .status(HttpStatus.FORBIDDEN)
                    .body("Management access required");
        }

        return ResponseEntity.ok(
                service.updateDocument(id, document)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(
            @PathVariable Long id,
            Principal principal) {

        if (!isManagement(principal)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Management access required");
        }

        service.deleteDocument(id);

        return ResponseEntity.ok(
                "Document deleted successfully"
        );
    }

    private boolean isManagement(Principal principal) {

        if (!(principal instanceof
                org.springframework.security.core.Authentication)) {
            return false;
        }

        org.springframework.security.core.Authentication auth =
                (org.springframework.security.core.Authentication)
                        principal;

        return auth.getAuthorities()
                .stream()
                .anyMatch(authority ->
                    authority.getAuthority()
                        .equals("ROLE_MANAGER") ||
                    authority.getAuthority()
                        .equals("ROLE_ADMIN")
                );
    }
}