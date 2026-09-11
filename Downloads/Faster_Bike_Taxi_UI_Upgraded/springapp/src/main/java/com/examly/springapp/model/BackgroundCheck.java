package com.examly.springapp.model;
import java.time.LocalDateTime;

import javax.persistence.*;
@Entity
@Table(name = "background_checks")
public class BackgroundCheck {

  public enum BackgroundCheckType{
  CRIMINAL_BACKGROUND,
  DRIVING_RECORD,
  IDENTITY_VERIFICATION,
  REFERENCE_CHECK
  }

  public enum BackgroundCheckStatus{
  PENDING,
  IN_PROGRESS,
  COMPLETED,
  FAILED
  }

  public enum BackgroundCheckResult{
  CLEAR,
  MINOR_ISSUES,
  MAJOR_ISSUES,
  DISQUALIFIED
  }
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private BackgroundCheckType checkType;

  @Enumerated(EnumType.STRING)
  private BackgroundCheckStatus status;

  private LocalDateTime initiatedDate;

  private LocalDateTime completionDate;

  @Enumerated(EnumType.STRING)
  private BackgroundCheckResult result;

  private String remarks;

  private String verificationAgency;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BackgroundCheckType getCheckType() {
    return checkType;
  }

  public void setCheckType(BackgroundCheckType checkType) {
    this.checkType = checkType;
  }

  public BackgroundCheckStatus getStatus() {
    return status;
  }

  public void setStatus(BackgroundCheckStatus status) {
    this.status = status;
  }

  public LocalDateTime getInitiatedDate() {
    return initiatedDate;
  }

  public void setInitiatedDate(LocalDateTime initiatedDate) {
    this.initiatedDate = initiatedDate;
  }

  public LocalDateTime getCompletionDate() {
    return completionDate;
  }

  public void setCompletionDate(LocalDateTime completionDate) {
    this.completionDate = completionDate;
  }

  public BackgroundCheckResult getResult() {
    return result;
  }

  public void setResult(BackgroundCheckResult result) {
    this.result = result;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getVerificationAgency() {
    return verificationAgency;
  }

  public void setVerificationAgency(String verificationAgency) {
    this.verificationAgency = verificationAgency;
  }
  
  @ManyToOne
@JoinColumn(name = "application_id")
private DriverApplication application;

public DriverApplication getApplication() {
  return application;
}

public void setApplication(DriverApplication application) {
  this.application = application;
}
}

