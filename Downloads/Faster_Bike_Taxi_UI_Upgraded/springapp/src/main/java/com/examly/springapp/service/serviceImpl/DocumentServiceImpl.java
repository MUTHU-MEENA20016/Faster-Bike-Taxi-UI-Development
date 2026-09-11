package com.examly.springapp.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Document;
import com.examly.springapp.repository.DocumentRepository;
import com.examly.springapp.service.DocumentService;

@Service
public class DocumentServiceImpl implements DocumentService {

  @Autowired
  private DocumentRepository repository;

  @Override
  public Document saveDocument(Document document) {
    return repository.save(document);
  }

  @Override
  public List<Document> getAllDocuments() {
    return repository.findAll();
  }

  @Override
  public Document getDocumentById(Long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public Document updateDocument(Long id, Document document) {

    Document existing = repository.findById(id).orElse(null);

    if(existing != null){
      existing.setDocumentType(document.getDocumentType());
      existing.setFileName(document.getFileName());
      existing.setFileSize(document.getFileSize());
      existing.setFilePath(document.getFilePath());
      existing.setVerificationStatus(document.getVerificationStatus());
      existing.setVerificationDate(document.getVerificationDate());
      existing.setExpiryDate(document.getExpiryDate());

      return repository.save(existing);
    }

    return null;
  }

  @Override
  public void deleteDocument(Long id) {
    repository.deleteById(id);
  }
}
