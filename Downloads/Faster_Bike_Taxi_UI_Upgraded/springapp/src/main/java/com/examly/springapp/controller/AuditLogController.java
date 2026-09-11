package com.examly.springapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.AuditLog;
import com.examly.springapp.service.AuditLogService;

@RestController
@RequestMapping("/api/auditlogs")
@CrossOrigin("*")
public class AuditLogController {

  @Autowired
  private AuditLogService service;

  @PostMapping
  public AuditLog saveAuditLog(
      @RequestBody AuditLog auditLog){
    return service.saveAuditLog(auditLog);
  }

  @GetMapping
  public List<AuditLog> getAllAuditLogs(){
    return service.getAllAuditLogs();
  }

  @GetMapping("/{id}")
  public AuditLog getAuditLogById(
      @PathVariable Long id){
    return service.getAuditLogById(id);
  }

  @PutMapping("/{id}")
  public AuditLog updateAuditLog(
      @PathVariable Long id,
      @RequestBody AuditLog auditLog){
    return service.updateAuditLog(id, auditLog);
  }

  @DeleteMapping("/{id}")
  public void deleteAuditLog(@PathVariable Long id){
    service.deleteAuditLog(id);
  }
}
