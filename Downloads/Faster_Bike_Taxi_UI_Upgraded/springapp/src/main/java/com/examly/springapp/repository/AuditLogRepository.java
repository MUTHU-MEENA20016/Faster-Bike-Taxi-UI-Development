package com.examly.springapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.examly.springapp.model.AuditLog;

public interface AuditLogRepository
    extends JpaRepository<AuditLog, Long> {

  List<AuditLog> findByEntityType(String entityType);

  List<AuditLog> findByAction(String action);
}





