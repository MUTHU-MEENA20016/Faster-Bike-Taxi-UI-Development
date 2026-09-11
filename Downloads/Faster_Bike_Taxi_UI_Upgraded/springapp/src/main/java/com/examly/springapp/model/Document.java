// package com.examly.springapp.model;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import javax.persistence.*;

// @Entity
// @Table(name = "documents")
// public class Document {
//   public enum DocumentType{
//   DRIVING_LICENSE,VEHICLE_REGISTRATION,INSURANCE,POLLUTION_CERTIFICATE,PHOTO,AADHAR,PAN
//   }
//   public enum VerificationStatus{
//   PENDING,VERIFIED,REJECTED,EXPIRED
//   }
//   @Id
//   @GeneratedValue(strategy = GenerationType.IDENTITY)
//   private Long id;
//   @Enumerated(EnumType.STRING)
//   private DocumentType documentType;
//   private String fileName;
//   private Long fileSize;
//   private String filePath;
//   private LocalDateTime uploadDate;
//   @Enumerated(EnumType.STRING)
//   private VerificationStatus verificationStatus;
//   private LocalDateTime verificationDate;
//   private LocalDate expiryDate;
//   public Long getId() {
//     return id;
//   }
//   public void setId(Long id) {
//     this.id = id;
//   }
//   public DocumentType getDocumentType() {
//     return documentType;
//   }
//   public void setDocumentType(DocumentType documentType) {
//     this.documentType = documentType;
//   }
//   public String getFileName() {
//     return fileName;
//   }
//   public void setFileName(String fileName) {
//     this.fileName = fileName;
//   }
//   public Long getFileSize() {
//     return fileSize;
//   }
//   public void setFileSize(Long fileSize) {
//     this.fileSize = fileSize;
//   }
//   public String getFilePath() {
//     return filePath;
//   }
//   public void setFilePath(String filePath) {
//     this.filePath = filePath;
//   }
//   public LocalDateTime getUploadDate() {
//     return uploadDate;
//   }
//   public void setUploadDate(LocalDateTime uploadDate) {
//     this.uploadDate = uploadDate;
//   }
//   public VerificationStatus getVerificationStatus() {
//     return verificationStatus;
//   }
//   public void setVerificationStatus(VerificationStatus verificationStatus) {
//     this.verificationStatus = verificationStatus;
//   }
//   public LocalDateTime getVerificationDate() {
//     return verificationDate;
//   }
//   public void setVerificationDate(LocalDateTime verificationDate) {
//     this.verificationDate = verificationDate;
//   }
//   public LocalDate getExpiryDate() {
//     return expiryDate;
//   }
//   public void setExpiryDate(LocalDate expiryDate) {
//     this.expiryDate = expiryDate;
//   }
//    @ManyToOne
//   @JoinColumn(name = "verified_by")
//   private User verifiedBy;
//  @ManyToOne
// @JoinColumn(name = "application_id")
// private DriverApplication application;
// public DriverApplication getApplication() {
//   return application;
// }
// public void setApplication(DriverApplication application) {
//   this.application = application;
// }
// }

// package com.examly.springapp.model;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import javax.persistence.*;

// import com.fasterxml.jackson.annotation.JsonIgnore;

// @Entity
// @Table(name = "documents")
// public class Document {

//     public enum DocumentType {
//         DRIVING_LICENSE,
//         VEHICLE_REGISTRATION,
//         INSURANCE,
//         POLLUTION_CERTIFICATE,
//         PHOTO,
//         AADHAR,
//         PAN
//     }

//     public enum VerificationStatus {
//         PENDING,
//         VERIFIED,
//         REJECTED,
//         EXPIRED
//     }

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Enumerated(EnumType.STRING)
//     private DocumentType documentType;

//     private String fileName;
//     private Long fileSize;
//     private String contentType;
//     private String filePath;
//     private LocalDateTime uploadDate;

//     @Enumerated(EnumType.STRING)
//     private VerificationStatus verificationStatus;

//     private LocalDateTime verificationDate;
//     private LocalDate expiryDate;

//     private String uploadToken;

//     @Lob
//     @Basic(fetch = FetchType.LAZY)
//     @Column(columnDefinition = "LONGBLOB")
//     @JsonIgnore
//     private byte[] fileData;

//     @ManyToOne
//     @JoinColumn(name = "verified_by")
//     @JsonIgnore
//     private User verifiedBy;

//     @ManyToOne
//     @JoinColumn(name = "application_id")
//     @JsonIgnore
//     private DriverApplication application;

//     // Getters and Setters

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public DocumentType getDocumentType() {
//         return documentType;
//     }

//     public void setDocumentType(DocumentType documentType) {
//         this.documentType = documentType;
//     }

//     public String getFileName() {
//         return fileName;
//     }

//     public void setFileName(String fileName) {
//         this.fileName = fileName;
//     }

//     public Long getFileSize() {
//         return fileSize;
//     }

//     public void setFileSize(Long fileSize) {
//         this.fileSize = fileSize;
//     }

//     public String getContentType() {
//         return contentType;
//     }

//     public void setContentType(String contentType) {
//         this.contentType = contentType;
//     }

//     public String getFilePath() {
//         return filePath;
//     }

//     public void setFilePath(String filePath) {
//         this.filePath = filePath;
//     }

//     public LocalDateTime getUploadDate() {
//         return uploadDate;
//     }

//     public void setUploadDate(LocalDateTime uploadDate) {
//         this.uploadDate = uploadDate;
//     }

//     public VerificationStatus getVerificationStatus() {
//         return verificationStatus;
//     }

//     public void setVerificationStatus(VerificationStatus verificationStatus) {
//         this.verificationStatus = verificationStatus;
//     }

//     public LocalDateTime getVerificationDate() {
//         return verificationDate;
//     }

//     public void setVerificationDate(LocalDateTime verificationDate) {
//         this.verificationDate = verificationDate;
//     }

//     public LocalDate getExpiryDate() {
//         return expiryDate;
//     }

//     public void setExpiryDate(LocalDate expiryDate) {
//         this.expiryDate = expiryDate;
//     }

//     public String getUploadToken() {
//         return uploadToken;
//     }

//     public void setUploadToken(String uploadToken) {
//         this.uploadToken = uploadToken;
//     }

//     public byte[] getFileData() {
//         return fileData;
//     }

//     public void setFileData(byte[] fileData) {
//         this.fileData = fileData;
//     }

//     public User getVerifiedBy() {
//         return verifiedBy;
//     }

//     public void setVerifiedBy(User verifiedBy) {
//         this.verifiedBy = verifiedBy;
//     }

//     public DriverApplication getApplication() {
//         return application;
//     }

//     public void setApplication(DriverApplication application) {
//         this.application = application;
//     }
// }
package com.examly.springapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "documents")
public class Document {

    public enum DocumentType {
        DRIVING_LICENSE,
        VEHICLE_REGISTRATION,
        INSURANCE,
        POLLUTION_CERTIFICATE,
        PHOTO,
        AADHAR,
        PAN
    }

    public enum VerificationStatus {
        PENDING,
        VERIFIED,
        REJECTED,
        EXPIRED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String fileName;

    private Long fileSize;

    private String contentType;

    private String filePath;

    private LocalDateTime uploadDate;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;

    private LocalDateTime verificationDate;

    private LocalDate expiryDate;

    /*
     * Used only while the application is being created.
     */
    private String uploadToken;

    /*
     * Actual document bytes are stored in MySQL.
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB")
    @JsonIgnore
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name = "verified_by")
    @JsonIgnore
    private User verifiedBy;

    @ManyToOne
    @JoinColumn(name = "application_id")
    @JsonIgnore
    private DriverApplication application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(
            VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public LocalDateTime getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(
            LocalDateTime verificationDate) {
        this.verificationDate = verificationDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public DriverApplication getApplication() {
        return application;
    }

    public void setApplication(
            DriverApplication application) {
        this.application = application;
    }
}