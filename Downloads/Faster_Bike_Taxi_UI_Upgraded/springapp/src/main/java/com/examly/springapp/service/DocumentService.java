package com.examly.springapp.service;
import java.util.List;
import com.examly.springapp.model.Document;

public interface DocumentService {

  Document saveDocument(Document document);

  List<Document> getAllDocuments();

  Document getDocumentById(Long id);

  Document updateDocument(Long id, Document document);

  void deleteDocument(Long id);
}