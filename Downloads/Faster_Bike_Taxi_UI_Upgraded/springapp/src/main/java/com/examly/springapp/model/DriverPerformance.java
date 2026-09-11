package com.examly.springapp.model;
import java.time.LocalDateTime;

import javax.persistence.*;
@Entity
@Table(name = "driver_performance")
public class DriverPerformance {

  public enum PerformanceStatus{
  ACTIVE,
  INACTIVE,
  SUSPENDED,
  TERMINATED
  }
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer totalRides;

  private Integer completedRides;

  private Integer cancelledRides;

  private Double averageRating;

  private Double totalEarnings;

  private LocalDateTime lastActiveDate;

  private Integer performanceScore;

  @Enumerated(EnumType.STRING)
  private PerformanceStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getTotalRides() {
    return totalRides;
  }

  public void setTotalRides(Integer totalRides) {
    this.totalRides = totalRides;
  }

  public Integer getCompletedRides() {
    return completedRides;
  }

  public void setCompletedRides(Integer completedRides) {
    this.completedRides = completedRides;
  }

  public Integer getCancelledRides() {
    return cancelledRides;
  }

  public void setCancelledRides(Integer cancelledRides) {
    this.cancelledRides = cancelledRides;
  }

  public Double getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(Double averageRating) {
    this.averageRating = averageRating;
  }

  public Double getTotalEarnings() {
    return totalEarnings;
  }

  public void setTotalEarnings(Double totalEarnings) {
    this.totalEarnings = totalEarnings;
  }

  public LocalDateTime getLastActiveDate() {
    return lastActiveDate;
  }

  public void setLastActiveDate(LocalDateTime lastActiveDate) {
    this.lastActiveDate = lastActiveDate;
  }

  public Integer getPerformanceScore() {
    return performanceScore;
  }

  public void setPerformanceScore(Integer performanceScore) {
    this.performanceScore = performanceScore;
  }

  public PerformanceStatus getStatus() {
    return status;
  }

  public void setStatus(PerformanceStatus status) {
    this.status = status;
  }

  
}

