package com.examly.springapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.model.Document;

public interface DocumentRepository
        extends JpaRepository<Document, Long> {

    List<Document> findByDocumentType(
            Document.DocumentType documentType);

    List<Document> findByVerificationStatus(
            Document.VerificationStatus verificationStatus);

    List<Document> findByApplicationId(Long applicationId);

    List<Document> findByUploadTokenAndApplicationIsNull(
            String uploadToken);

    Document findByUploadTokenAndDocumentTypeAndApplicationIsNull(
            String uploadToken,
            Document.DocumentType documentType);
}