// package com.examly.springapp.service.serviceImpl;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.examly.springapp.model.AuditLog;
// import com.examly.springapp.repository.AuditLogRepository;
// import com.examly.springapp.service.AuditLogService;

// @Service
// public class AuditLogServiceImpl implements AuditLogService {

//   @Autowired
//   private AuditLogRepository repository;

//   @Override
//   public AuditLog saveAuditLog(AuditLog auditLog) {
//     return repository.save(auditLog);
//   }

//   @Override
//   public List<AuditLog> getAllAuditLogs() {
//     return repository.findAll();
//   }

//   @Override
//   public AuditLog getAuditLogById(Long id) {
//     return repository.findById(id).orElse(null);
//   }

//   @Override
//   public AuditLog updateAuditLog(Long id, AuditLog auditLog) {

//     AuditLog existing = repository.findById(id).orElse(null);

//     if(existing != null){
//       existing.setAction(auditLog.getAction());
//       existing.setEntityType(auditLog.getEntityType());
//       existing.setEntityId(auditLog.getEntityId());
//       existing.setOldValue(auditLog.getOldValue());
//       existing.setNewValue(auditLog.getNewValue());
//       existing.setIpAddress(auditLog.getIpAddress());
//       existing.setUserAgent(auditLog.getUserAgent());
//       existing.setSessionId(auditLog.getSessionId());

//       return repository.save(existing);
//     }

//     return null;
//   }

//   @Override
//   public void deleteAuditLog(Long id) {
//     repository.deleteById(id);
//   }
// }
package com.examly.springapp.service.serviceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.AuditLog;
import com.examly.springapp.repository.AuditLogRepository;
import com.examly.springapp.service.AuditLogService;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogRepository repository;

    @Override
    public AuditLog saveAuditLog(AuditLog auditLog) {
        return repository.save(auditLog);
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {

        return repository.findAll()
                .stream()
                .sorted(
                    Comparator.comparing(
                        AuditLog::getTimestamp,
                        Comparator.nullsLast(
                            Comparator.naturalOrder()
                        )
                    ).reversed()
                )
                .collect(Collectors.toList());
    }

    @Override
    public AuditLog getAuditLogById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public AuditLog updateAuditLog(
            Long id,
            AuditLog auditLog) {

        AuditLog existing =
                repository.findById(id).orElse(null);

        if (existing != null) {

            existing.setAction(
                    auditLog.getAction()
            );

            existing.setEntityType(
                    auditLog.getEntityType()
            );

            existing.setEntityId(
                    auditLog.getEntityId()
            );

            existing.setOldValue(
                    auditLog.getOldValue()
            );

            existing.setNewValue(
                    auditLog.getNewValue()
            );

            existing.setIpAddress(
                    auditLog.getIpAddress()
            );

            existing.setUserAgent(
                    auditLog.getUserAgent()
            );

            existing.setSessionId(
                    auditLog.getSessionId()
            );

            return repository.save(existing);
        }

        return null;
    }

    @Override
    public void deleteAuditLog(Long id) {
        repository.deleteById(id);
    }
}