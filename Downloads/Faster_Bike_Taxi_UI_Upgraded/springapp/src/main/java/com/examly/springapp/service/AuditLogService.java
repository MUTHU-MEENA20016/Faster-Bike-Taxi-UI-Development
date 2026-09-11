package com.examly.springapp.service;

import java.util.List;
import com.examly.springapp.model.AuditLog;

public interface AuditLogService {

  AuditLog saveAuditLog(AuditLog auditLog);

  List<AuditLog> getAllAuditLogs();

  AuditLog getAuditLogById(Long id);

  AuditLog updateAuditLog(
      Long id,
      AuditLog auditLog);

  void deleteAuditLog(Long id);
}







